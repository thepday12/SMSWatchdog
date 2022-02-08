package com.encapital.io.banksmsapp.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.telephony.SmsMessage;
import android.util.Log;

import com.encapital.io.banksmsapp.database.AppDatabaseHelper;
import com.encapital.io.banksmsapp.model.DepositEntity;
import com.encapital.io.banksmsapp.model.DepositMessage;
import com.encapital.io.banksmsapp.model.DepositResponse;
import com.encapital.io.banksmsapp.remote.APIService;
import com.encapital.io.banksmsapp.remote.ApiUtils;
import com.encapital.io.banksmsapp.utils.CommonUtils;
import com.encapital.io.banksmsapp.utils.SigningRequest;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.encapital.io.banksmsapp.utils.BankUtils.isSendFromBank;
import static com.encapital.io.banksmsapp.utils.CommonUtils.handlerSendSMS2Server;
import static com.encapital.io.banksmsapp.utils.FileUtils.writeErrSendDeposit;
import static com.encapital.io.banksmsapp.utils.FileUtils.writeFailSendDeposit;


public class SmsReceiverService extends JobIntentService {
    private AppDatabaseHelper mDb;
    private Gson gson = new Gson();
    private SigningRequest signingRequest = new SigningRequest();

    public final static String SMS_TAG = "SMS_TAG";

    public static final String SMS_EXTRA_NAME = "pdus";


    static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SmsReceiverService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Bundle data = intent.getExtras();

        Log.d(SMS_TAG, "SMS Received");

        if (data != null) {
            // Get received SMS array
            Object[] smsExtra = (Object[]) data.get(SMS_EXTRA_NAME);
            assert smsExtra != null;
            for (Object smsData : smsExtra) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsData);
                String smsMessage = sms.getMessageBody();
                String phoneNumber = sms.getOriginatingAddress();
                String branchName = sms.getDisplayOriginatingAddress();

                //gọi hàm xử lý nội dung và gửi kết quả trả về
                if (isSendFromBank(phoneNumber)) {
                    doSending(phoneNumber, branchName, smsMessage);
                }
            }

        }
    }


    public void doSending(String phoneNumber, String branchName, String smsMessage) {
        try {
            DepositMessage depositMessage = new DepositMessage(phoneNumber, branchName, smsMessage);
            sendDepositMessage2Server(depositMessage);
        } catch (Exception ex) {
            writeErrSendDeposit(ex.getMessage());
        }
    }


    public void sendDepositMessage2Server(DepositMessage depositMessage) {

        mDb = AppDatabaseHelper.getInstance(getApplicationContext());
        final DepositEntity depositEntity = new DepositEntity(depositMessage);
        mDb.addMessage(depositEntity, new AppDatabaseHelper.IOnInsertDepositComplete() {
            @Override
            public void onInsertDepositComplete(DepositEntity message) {
                handlerSendSMS2Server(message, new CommonUtils.ISendSMSResponse() {
                    @Override
                    public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {
                        if (response.isSuccessful()) {
                            updateMessageStatus(depositEntity, DepositEntity.SUCCESS_STATUS);
                        }
                    }

                    @Override
                    public void onFailure(Call<DepositResponse> call, Throwable t) {
                        updateMessageStatus(depositEntity, DepositEntity.FAIL_STATUS);
                        writeFailSendDeposit(t.getMessage()+"\n"+gson.toJson(depositEntity));
                    }
                });
            }
        });
    }

    public void handlerOnComplete(final DepositEntity depositEntity) {

        APIService apiService = ApiUtils.getAPIService();
        DepositMessage depositMessage = depositEntity.getDepositMessage();
        String signatureOfRequest = signingRequest.signRequest(gson.toJson(depositMessage));
        apiService.sendMessageDeposit(signatureOfRequest, depositMessage).enqueue(new Callback<DepositResponse>() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {
                if (response.isSuccessful()) {
                    updateMessageStatus(depositEntity, DepositEntity.SUCCESS_STATUS);
                }

            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                updateMessageStatus(depositEntity, DepositEntity.FAIL_STATUS);
            }
        });
    }

    private void updateMessageStatus(DepositEntity depositEntity, String status) {
        mDb = AppDatabaseHelper.getInstance(getApplicationContext());
        if (!Objects.isNull(depositEntity.getId())) {
            depositEntity.setStatus(status);
            mDb.updateMessageStatus(depositEntity);
        }
    }
}


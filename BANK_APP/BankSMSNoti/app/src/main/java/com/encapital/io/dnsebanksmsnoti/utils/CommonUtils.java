package com.encapital.io.dnsebanksmsnoti.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.encapital.io.dnsebanksmsnoti.model.BankNotification;
import com.encapital.io.dnsebanksmsnoti.model.DepositEntity;
import com.encapital.io.dnsebanksmsnoti.model.BankMessage;
import com.encapital.io.dnsebanksmsnoti.model.DepositResponse;
import com.encapital.io.dnsebanksmsnoti.remote.APIService;
import com.encapital.io.dnsebanksmsnoti.remote.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonUtils {
    public static final String FORMAT_DISPLAY_DATE = "dd-MM-yyyy HH:mm";

    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd");
    }

    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format).format(new java.util.Date());
    }

    public static long getCurrentTimeMilliseconds() {
        return new java.util.Date().getTime();
    }

    public static String formatDateTime(Calendar calendar,String format) {
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public static String formatDateTime(long millis,String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public interface IOnSendSMSComplete {
        void onSendSMSComplete(Boolean status);
    }

    public interface IOnSelectDate {
        void onSelectDateComplete(Calendar calendar);
    }

    public static void sendSMSError(Context context, String content, final IOnSendSMSComplete iOnSendSMSComplete) {
        final String msHoaPhoneNumber = "+84349611026";// "0989658636";

        final SmsManager sms = SmsManager.getDefault();
        Intent msgSent = new Intent("ACTION_MSG_SENT");
        final PendingIntent pendingMsgSent =
                PendingIntent.getBroadcast(context, 0, msgSent, 0);
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                iOnSendSMSComplete.onSendSMSComplete(result == Activity.RESULT_OK);
            }
        }, new IntentFilter("ACTION_MSG_SENT"));
//        sms.sendTextMessage(msHoaPhoneNumber, null, content,
//                pendingMsgSent, null);

    }


    public static  void showDateTimePicker(final Context context, final IOnSelectDate iOnSelectDate) {
        final Calendar currentDate = Calendar.getInstance();
        showDateTimePicker(context,iOnSelectDate,currentDate);
    }

    public static  void showDateTimePicker(final Context context, final IOnSelectDate iOnSelectDate, final Calendar defaultDateTime ) {
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        iOnSelectDate.onSelectDateComplete(date);
                    }
                }, defaultDateTime.get(Calendar.HOUR_OF_DAY), defaultDateTime.get(Calendar.MINUTE), false).show();
            }
        }, defaultDateTime.get(Calendar.YEAR), defaultDateTime.get(Calendar.MONTH), defaultDateTime.get(Calendar.DATE)).show();
    }


    public interface ISendSMSResponse{
        void onResponse(Call<DepositResponse> call, Response<DepositResponse> response);
        void onFailure(Call<DepositResponse> call, Throwable t);
    }
    public static void handlerSendSMS2Server(final DepositEntity depositEntity, final ISendSMSResponse sendSMSResponse){
        APIService apiService = ApiUtils.getAPIService();
        BankMessage bankMessage = depositEntity.getDepositMessage();
        BankNotification bankNotification = new BankNotification(bankMessage);
        apiService.sendMessageDeposit(bankNotification).enqueue(new Callback<DepositResponse>() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {
                sendSMSResponse.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                sendSMSResponse.onFailure(call,t);
            }
        });
    }

}


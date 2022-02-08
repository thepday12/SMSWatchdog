package com.encapital.io.dnsebanksmsnoti.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.encapital.io.dnsebanksmsnoti.R;
import com.encapital.io.dnsebanksmsnoti.adapter.ResendDepositMessageAdapter;
import com.encapital.io.dnsebanksmsnoti.model.DepositEntity;
import com.encapital.io.dnsebanksmsnoti.utils.CommonUtils;
import com.encapital.io.dnsebanksmsnoti.utils.SigningRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.encapital.io.dnsebanksmsnoti.utils.BankUtils.isSendFromBank;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.FORMAT_DISPLAY_DATE;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.formatDateTime;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.getCurrentTimeMilliseconds;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.showDateTimePicker;

public class ResendActivity extends AppCompatActivity {

    private EditText etFromDate, etEndDate;
    private Calendar fromDate, endDate;
    private Button btGetSMS,btnResend;
    private TextView tvCount;
    private RecyclerView rvListMessage;
    private ResendDepositMessageAdapter mAdapter;
    private Gson gson = new Gson();
    private SigningRequest signingRequest = new SigningRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_resend);

        findViewId();
        initializeView();
        initListener();
    }

    void findViewId() {
        etFromDate = findViewById(R.id.etFromDate);
        etEndDate = findViewById(R.id.etEndDate);
        btGetSMS = findViewById(R.id.btGetSMS);
        btnResend = findViewById(R.id.btnResend);
        tvCount = findViewById(R.id.tvCount);
        rvListMessage = findViewById(R.id.rvListMessage);
    }

    void initializeView() {
        endDate = Calendar.getInstance();
        fromDate = Calendar.getInstance();
        fromDate.add(Calendar.DAY_OF_MONTH, -1);

        etFromDate.setText(formatDateTime(fromDate, FORMAT_DISPLAY_DATE));
        etEndDate.setText(formatDateTime(endDate, FORMAT_DISPLAY_DATE));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ResendActivity.this);
        rvListMessage.setLayoutManager(layoutManager);
        mAdapter = new ResendDepositMessageAdapter();
        rvListMessage.setAdapter(mAdapter);
    }

    void initListener() {
        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(ResendActivity.this, new CommonUtils.IOnSelectDate() {
                    @Override
                    public void onSelectDateComplete(Calendar calendar) {
                        etFromDate.setText(formatDateTime(calendar, FORMAT_DISPLAY_DATE));
                        fromDate = calendar;
                    }
                }, fromDate);
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(ResendActivity.this, new CommonUtils.IOnSelectDate() {
                    @Override
                    public void onSelectDateComplete(Calendar calendar) {
                        etEndDate.setText(formatDateTime(calendar, FORMAT_DISPLAY_DATE));
                        endDate = calendar;
                    }
                }, endDate);
            }
        });

        btGetSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DepositEntity> depositEntities = getListSMS(fromDate.getTimeInMillis(), endDate.getTimeInMillis());
                tvCount.setText(Html.fromHtml("<b>Tổng số: </b>"+depositEntities.size()));
                mAdapter.set(depositEntities);
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ResendActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Thực hiện đồng bộ toàn bộ tin nhắn trong danh sách bên dưới?")
                        .setPositiveButton("Đồng ý",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAdapter.resendAllSMS2Server();
                                    }
                                })
                        .setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void finish() {
        if(mAdapter.isSending()){
            Toast.makeText(ResendActivity.this, "Tiến trình chưa hoàn tất", Toast.LENGTH_SHORT).show();
            return;
        }
        super.finish();
    }


    public List<DepositEntity> getListSMS(long fromDate, long endDate) {
        List<DepositEntity> sms = new ArrayList<>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur != null && cur.moveToNext()) {
            String phoneNumber = cur.getString(cur.getColumnIndex("address"));
            if (isSendFromBank(phoneNumber)) {
                long date = cur.getLong(cur.getColumnIndexOrThrow("date"));
                if (date >= fromDate && date <= endDate) {
                    String smsMessage = cur.getString(cur.getColumnIndexOrThrow("body"));
                    sms.add(new DepositEntity(sms.size(), phoneNumber, phoneNumber, smsMessage, "", getCurrentTimeMilliseconds()));
                }
            }
        }

        if (cur != null) {
            cur.close();
        }
        return sms;
    }




}

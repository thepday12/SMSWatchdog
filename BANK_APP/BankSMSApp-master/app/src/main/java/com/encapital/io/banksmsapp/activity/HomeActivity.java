package com.encapital.io.banksmsapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.encapital.io.banksmsapp.R;
import com.encapital.io.banksmsapp.adapter.DepositMessageAdapter;
import com.encapital.io.banksmsapp.database.AppDatabaseHelper;
import com.encapital.io.banksmsapp.database.DatabaseCallback;
import com.encapital.io.banksmsapp.model.DepositEntity;
import com.encapital.io.banksmsapp.utils.CommonUtils;
import com.encapital.io.banksmsapp.utils.NetworkUtil;

import java.util.Calendar;
import java.util.List;

import static com.encapital.io.banksmsapp.utils.CommonUtils.FORMAT_DISPLAY_DATE;
import static com.encapital.io.banksmsapp.utils.CommonUtils.formatDateTime;
import static com.encapital.io.banksmsapp.utils.CommonUtils.getCurrentDate;
import static com.encapital.io.banksmsapp.utils.CommonUtils.showDateTimePicker;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvDepositMessage;
    private EditText etFromDate,etEndDate;
    private Calendar fromDate,endDate;
    private Button btnResend,btGetSMS;
    private ImageButton btnReload;
    private TextView statusIcon, tvStatus;
    private DepositMessageAdapter mAdapter;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtil.getConnectivityStatusString(context);
            if (tvStatus == null|| statusIcon==null)
                return;
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    tvStatus.setText(R.string.status_disconnected);
                    statusIcon.setBackgroundResource(R.drawable.ic_disconnected);
                } else {
                    tvStatus.setText(R.string.status_connected);
                    statusIcon.setBackgroundResource(R.drawable.ic_connected);

                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewId();
        initializeView();
        initListener();
    }

    void findViewId() {
        statusIcon = findViewById(R.id.statusIcon);
        tvStatus = findViewById(R.id.tvStatus);
        etFromDate = findViewById(R.id.etFromDate);
        etEndDate = findViewById(R.id.etEndDate);
        btGetSMS = findViewById(R.id.btGetSMS);
        rvDepositMessage = findViewById(R.id.rvDepositMessage);
        btnResend = findViewById(R.id.btnResend);
        btnReload = findViewById(R.id.btnReload);
    }


    void initializeView() {
        endDate = Calendar.getInstance();
        fromDate = Calendar.getInstance();
        fromDate.add(Calendar.DAY_OF_MONTH, -7);

        etFromDate.setText(formatDateTime(fromDate, FORMAT_DISPLAY_DATE));
        etEndDate.setText(formatDateTime(endDate, FORMAT_DISPLAY_DATE));

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.status_animation);
        statusIcon.startAnimation(animation);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        rvDepositMessage.setLayoutManager(layoutManager);

        mAdapter = new DepositMessageAdapter();
        getLocalMessages();
        rvDepositMessage.setAdapter(mAdapter);


    }

    void initListener() {
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ResendActivity.class));
            }
        });

        btGetSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocalMessages();
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate = Calendar.getInstance();
                etEndDate.setText(formatDateTime(endDate, FORMAT_DISPLAY_DATE));
                getLocalMessages();
            }
        });

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(HomeActivity.this, new CommonUtils.IOnSelectDate() {
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
                showDateTimePicker(HomeActivity.this, new CommonUtils.IOnSelectDate() {
                    @Override
                    public void onSelectDateComplete(Calendar calendar) {
                        etEndDate.setText(formatDateTime(calendar, FORMAT_DISPLAY_DATE));
                        endDate = calendar;
                    }
                }, endDate);
            }
        });
    }

    void registerReceiverNetworkChange() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void getLocalMessages() {
        AppDatabaseHelper mDb = AppDatabaseHelper.getInstance(HomeActivity.this);
        mDb.getAllMessages(fromDate.getTimeInMillis(),endDate.getTimeInMillis(),new DatabaseCallback() {
            @Override
            public void onMessagesLoaded(List<DepositEntity> messages) {
                if (messages.isEmpty()) {
                    return;
                }
                mAdapter.set(messages);
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiverNetworkChange();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (final Exception exception) {
            // networkChangeReceiver is not register
        }
    }


}
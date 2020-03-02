package com.encapital.io.banksmsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.encapital.io.banksmsapp.adapter.DepositMessageAdapter;
import com.encapital.io.banksmsapp.database.AppDatabaseHelper;
import com.encapital.io.banksmsapp.database.DatabaseCallback;
import com.encapital.io.banksmsapp.model.DepositEntity;
import com.encapital.io.banksmsapp.utils.NetworkUtil;

import java.util.List;

import static com.encapital.io.banksmsapp.utils.CommonUtils.getCurrentDate;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvDepositMessage;
    private Button btnReload;
    private TextView statusIcon,tvStatus;
    private DepositMessageAdapter mAdapter;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtil.getConnectivityStatusString(context);
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
        rvDepositMessage = findViewById(R.id.rvDepositMessage);
        btnReload = findViewById(R.id.btnReload);
    }


    void initializeView() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.status_animation);
        statusIcon.startAnimation(animation);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        rvDepositMessage.setLayoutManager(layoutManager);

        mAdapter = new DepositMessageAdapter();
        getLocalMessages();
        rvDepositMessage.setAdapter(mAdapter);

        registerReceiverNetworkChange();
    }

    void initListener() {
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocalMessages();
            }
        });
    }

    void registerReceiverNetworkChange(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void getLocalMessages() {
        AppDatabaseHelper mDb = AppDatabaseHelper.getInstance(HomeActivity.this);
        mDb.getAllMessages(new DatabaseCallback() {
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }
}
package com.encapital.io.banksmsapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.encapital.io.banksmsapp.R;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1;
    final String[] PERMISSION_LIST = new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_SMS, RECEIVE_SMS, SEND_SMS, ACCESS_NETWORK_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isPermissionsGranted = isPermissionsGranted();
        if (isPermissionsGranted) {
            handlerPermissionsGranted();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    PERMISSION_LIST,
                    REQUEST_PERMISSION_CODE);
        }
    }


    public boolean isPermissionsGranted() {
        for (String permission : PERMISSION_LIST
        ) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void handlerPermissionsGranted() {
        handlerPermissionsGranted(1000);
    }

    private void handlerPermissionsGranted(long delayMillis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        }, delayMillis);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && isGranted(grantResults)) {
                    handlerPermissionsGranted(500);
                } else {
//                    Toast.makeText(this, "Bạn đã từ chối cấp quyền cho ứng dụng", Toast.LENGTH_SHORT).show();
//                    finish();
                }
                return;
            }
        }
    }

    private boolean isGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
package com.tokimthep.smswatchdog.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.tokimthep.smswatchdog.R;
import com.tokimthep.smswatchdog.view.scan.ScanActivity;
import com.tokimthep.smswatchdog.view.utils.MySharedPreferences;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    final int SMS_PERMISSION_CODE = 1;
    final String[] PERMISSION_LIST = new String[]{RECEIVE_SMS,READ_SMS,SEND_SMS,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,CAMERA};
    private boolean mIsFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySharedPreferences.init(getApplicationContext());

        mIsFirst= MySharedPreferences.read(MySharedPreferences.IS_FIRST, true);
        Fragment fragment = SplashScreenFragment.newInstance();
        if (savedInstanceState == null) {
            if(mIsFirst){
                fragment =FirstSplashScreenFragment.newInstance();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, fragment)
                    .commit();
        }
        if(isPermissionsGranted()){
            handlerPermissionsGranted();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    PERMISSION_LIST,
                    SMS_PERMISSION_CODE);
        }
    }





    public boolean isPermissionsGranted() {
        for (String permission:PERMISSION_LIST
             ) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private void handlerPermissionsGranted(){
        handlerPermissionsGranted(1000);
    }

    private void handlerPermissionsGranted(long delayMillis){
        if(!mIsFirst) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, ScanActivity.class));
                    finish();
                }
            }, delayMillis);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && isGranted(grantResults)) {

                    handlerPermissionsGranted(500);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private boolean isGranted(int[] grantResults){
        for(int grantResult:grantResults){
            if(grantResult != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}

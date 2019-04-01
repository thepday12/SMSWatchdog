package com.tokimthep.smswatchdog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";

//    private final String serviceProviderNumber;
//    private final String serviceProviderSmsCondition;

    private Listener listener;


    public SmsBroadcastReceiver(){
        super();
    }

//    public SmsBroadcastReceiver(String serviceProviderNumber, String serviceProviderSmsCondition) {
//        this.serviceProviderNumber = serviceProviderNumber;
//        this.serviceProviderSmsCondition = serviceProviderSmsCondition;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.getDisplayOriginatingAddress();
                    smsBody += smsMessage.getMessageBody();
                }
            } else {
                Bundle smsBundle = intent.getExtras();
                if (smsBundle != null) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (pdus == null) {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += messages[i].getMessageBody();
                    }
                    smsSender = messages[0].getOriginatingAddress();
                }
            }

//            if (smsSender && smsBody) {
//                if (listener != null) {
//                    listener.onTextReceived(smsBody);
//                }
                extractLogToFile(smsSender,smsBody);
            Toast.makeText(context, smsBody, Toast.LENGTH_SHORT).show();
//            }
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onTextReceived(String text);
    }

    private static  String getFileName(){
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
        return df.format(date)+".txt";
    }
    public static String extractLogToFile(String phone,String data) {


        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
//        String path = Environment.getExternalStorageDirectory() + "/" + "LML/";
        String fileName = getFileName();
//        String fullName =  "LOG_LML"+System.currentTimeMillis()+".txt";
//
//        // Extract to file.
//        File file = new File (fullName);
        File myDir = new File(Environment.getExternalStorageDirectory() + "/watchdog_data/");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        File file = new File(myDir, fileName);
//        if(!file.exists()){
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        FileWriter writer = null;
        try {


            // write output stream
            writer = new FileWriter(file,true);
            writer.append(Calendar.getInstance().getTime()+"-------------------------\n");
            writer.append(phone + "\n");
            writer.append(data + "\n");

//            char[] buffer = new char[10000];
//            do {
//                int n = reader.read(buffer, 0, buffer.length);
//                if (n == -1)
//                    break;
//                writer.write(buffer, 0, n);
//            } while (true);

            writer.close();

        } catch (IOException e) {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                }

            // You might want to write a failure message to the log here.
            return null;
        }

        return fileName;
    }
}
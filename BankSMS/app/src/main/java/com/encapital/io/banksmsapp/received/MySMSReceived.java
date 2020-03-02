package com.encapital.io.banksmsapp.received;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.encapital.io.banksmsapp.service.SmsReceiverService;

public class MySMSReceived extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsReceiverService.enqueueWork(context, intent);
    }


}
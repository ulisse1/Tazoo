package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {

        String phoneNumber= intent.getStringExtra("phoneNumber");
        String message= intent.getStringExtra("message");
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        //TODO: PC TEST
        //Toast.makeText(context," A message has been sent to " + phoneNumber + " message: " + message, Toast.LENGTH_LONG).show();
    }



}
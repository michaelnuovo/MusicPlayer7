package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("TAG","Broadcast received"); //Doesn't print anything

        // Create Intent
        Intent serviceIntent = new Intent(context, BackgroundService.class);
        // Start service
        context.startService(serviceIntent);



    }
}
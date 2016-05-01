package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BackgroundService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        /**
         * Registers the custom receiver with the Android system.
         * This receiver cannot be registered in thmanifestst, and must
         * be set programmatically.
         */

        MusicIntentReceiver receiver = new MusicIntentReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart(Intent intent, int startid) {

    }
}
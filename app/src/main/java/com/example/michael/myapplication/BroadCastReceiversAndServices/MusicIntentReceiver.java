package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This receiver will be set either in main when the application
 * is run or by a background service that starts on boot-up.
 *
 * This receiver Toggles the pause and play state.
 */
public class MusicIntentReceiver extends BroadcastReceiver {

    /**
     * This receiver will return receive the first intent
     * as soon as it is registered. So we need a local
     * counter variable that ignores the first intent received.
     * The variable will remain in scope throughout the life-cycle of the receiver.
     * If the receiver is restarted, the first intent will be ignored.
     */

    int intentCounter = 0;

    @Override
    public void onReceive(Context ctx, Intent intent) {

        if (intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG)) {

            /**
             * De-bug logs
             */
            Log.v("TAG","Custom intent received: ");
            Log.v("TAG","Pre-increment counter value is: "+intentCounter);

            if(intentCounter++ != 0){

                /**
                 * De-bug logs
                 */
                Log.v("TAG","Post-increment counter value is : "+intentCounter);
                Log.v("TAG", "intent state is " + String.valueOf(intent.getIntExtra("state", 0))); //Prints

                /**
                 * Headset becomes unplugged.
                 */
                if(intent.getIntExtra("state",0)==0){ // 0 for unplugged (if it becomes unplugged)

                    /**
                     * If music is playing, pause it.
                     */
                    if(StaticMusicPlayer.musicIsPlaying()){
                        //StaticMusicPlayer.togglePauseState();
                    }
                }

                /**
                 * Headset becomes plugged in.
                 */
                if(intent.getIntExtra("state",0)==1){ // 1 for plugged (if it becomes plugged)

                    /**
                     * If music is not playing, play it.
                     */
                    if(StaticMusicPlayer.getPausedState()){
                        //StaticMusicPlayer.togglePauseState();
                    }
                }
            }
        }
    }
}
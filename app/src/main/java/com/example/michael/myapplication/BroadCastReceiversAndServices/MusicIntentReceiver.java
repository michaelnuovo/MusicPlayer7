package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class MusicIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {

        if (intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG)) {

            Log.v("TAG", "intent state is " + String.valueOf(intent.getIntExtra("state", 0))); //Prints

            if(intent.getIntExtra("state",0)==0){ // 0 for unplugged (if it becomes unplugged)




                    //if(StaticMusicPlayer.musicIsPlaying){
                      //  StaticMusicPlayer.togglePauseState();
                    //}
            }

            if(intent.getIntExtra("state",0)==1){ // 1 for plugged (if it becomes plugged)


                    //if(StaticMusicPlayer.isPaused){
                      //  StaticMusicPlayer.togglePauseState();
                    //}
            }
        }
    }
}
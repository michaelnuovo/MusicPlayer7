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

/*
 * Toggles the pause and play state
 * of the internal music player.
 * If headphones are unplugged, the music will pause.
 * If headphones are plugged in, the music will resume if paused.
 */
public class MusicIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {

        if (intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG)) {

            Log.v("TAG", "intent state is " + String.valueOf(intent.getIntExtra("state", 0))); //Prints

            if(intent.getIntExtra("state",0)==0){ // 0 for unplugged (if it becomes unplugged)


                    /*
                     * Headset is unplugged.
                     * If music playing, music pauses.
                     */

                    if(StaticMusicPlayer.musicIsPlaying()){
                        StaticMusicPlayer.togglePauseState();
                    }
            }

            if(intent.getIntExtra("state",0)==1){ // 1 for plugged (if it becomes plugged)


                    /*
                     * Headset is plugged in.
                     * If music was paused, music resumes.
                     * StaticMusicPlayer class maintains a copy of the most current
                     * playlist its in private folder.
                     * StaticMusicPlayer also maintains a copy of the current song index.
                     */

                    if(StaticMusicPlayer.getPausedState()){
                      StaticMusicPlayer.togglePauseState();
                    }
            }
        }
    }

}
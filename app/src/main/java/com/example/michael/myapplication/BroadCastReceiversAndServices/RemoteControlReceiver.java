package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

/**
 * Created by micha on 5/23/2016.
 */
public class RemoteControlReceiver extends BroadcastReceiver {

    public String ComponentName() {
        return this.getClass().getName();
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {

        Log.v("TAG","RemoteControlReceiver intent received");

        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {

            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);

            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    //if(!PlayerConstants.SONG_PAUSED){
                    if(!StaticMusicPlayer.isPaused()){
                        //Controls.pauseControl(context);
                        StaticMusicPlayer.pause();
                    }else{
                        //Controls.playControl(context);
                        StaticMusicPlayer.resume();
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
                    //Controls.nextControl(context);
                    StaticMusicPlayer.playNext();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
                    //Controls.previousControl(context);
                    StaticMusicPlayer.playPrevious();
                    break;
            }
        }

        else {
            if (intent.getAction().equals(BackgroundService.NOTIFY_PLAY)) {
                //Controls.playControl(context);
                StaticMusicPlayer.tryToPlaySong(StaticMusicPlayer.getPlayList().get(0));
                Log.v("TAG","!@#DF");
            } else if (intent.getAction().equals(BackgroundService.NOTIFY_PAUSE)) {
                //Controls.pauseControl(context);
                StaticMusicPlayer.pause();
            } else if (intent.getAction().equals(BackgroundService.NOTIFY_NEXT)) {
                //Controls.nextControl(context);
                StaticMusicPlayer.playNext();
            } else if (intent.getAction().equals(BackgroundService.NOTIFY_DELETE)) {
                Intent i = new Intent(ctx, BackgroundService.class);
                ctx.stopService(i);
                Intent in = new Intent(ctx, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(in);
            } else if (intent.getAction().equals(BackgroundService.NOTIFY_PREVIOUS)) {
                StaticMusicPlayer.playPrevious();
            }
        }
    }
}

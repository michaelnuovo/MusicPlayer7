package com.example.michael.myapplication.Utilities;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.michael.myapplication.Objects.SongObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class StaticMusicPlayer {

    private static ArrayList<SongObject> songObjectList = new ArrayList<>();
    private static MediaPlayer musicPlayer = new MediaPlayer();

    private static Boolean noSongHasBeenPlayedYet = true;
    private static Boolean musicIsPlaying = false;
    private static Boolean isPaused = false;
    private static int currentIndex = 0;

    public static void setPlayList(ArrayList<SongObject> _songObjectList){songObjectList = _songObjectList;}
    public static ArrayList<SongObject> getPlayList(){return songObjectList;}
    public static int getCurrentIndex(){return currentIndex;}

    public static void tryToPlaySong(final SongObject songObject) {

        new Thread() {
            public void run() {
                try {
                    playSong(songObject);
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        noSongHasBeenPlayedYet = false;
        currentIndex = songObjectList.indexOf(songObject);
        Log.v("TAG", "current index is " + StaticMusicPlayer.getCurrentIndex());
        musicIsPlaying = true;
        isPaused = false;

    }

    private static void playSong(SongObject songObject) throws IllegalArgumentException, IllegalStateException, IOException {

        musicPlayer.reset();
        musicPlayer.reset();
        musicPlayer.setDataSource(songObject.songPath);
        musicPlayer.prepare();
        musicPlayer.start();

        setSongCompletionListener();
    }

    public static void setSongCompletionListener(){

        musicPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {

            public void onCompletion(android.media.MediaPlayer mp) {

                musicIsPlaying = false;

                // we also to start playing the next song if there is one
                if (currentIndex != songObjectList.size() - 1) {
                    tryToPlaySong(songObjectList.get(currentIndex + 1));
                }
            }
        });
    }

    static public ArrayList<SongObject> returnShuffledList(ArrayList<SongObject> songObjectList){

        // Create new List with same capacity as original (for efficiency).
        ArrayList<SongObject> copy = new ArrayList<SongObject>(songObjectList.size());

        for (SongObject foo: songObjectList) {
            copy.add(foo.copy(new SongObject()));
        }

        Collections.shuffle(copy);

        return copy;
    }
}

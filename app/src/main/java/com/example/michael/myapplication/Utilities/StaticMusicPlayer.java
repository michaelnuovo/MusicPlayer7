package com.example.michael.myapplication.Utilities;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.michael.myapplication.BroadCastReceiversAndServices.SerializeAndSaveObject;
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

    private static Context ctx;

    /**
     * Sets the play list.
     * Saves the play list object to private file
     * as a serialized object.
     * @param _songObjectList
     */
    public static void setPlayList(ArrayList<SongObject> _songObjectList){

        songObjectList = _songObjectList;

        //Play list object is serialized and saved to file.
        //Key is string value "list"
        SerializeAndSaveObject.saveObject("list", songObjectList);
    }


    public static boolean getPausedState(){

        return isPaused == true;
    }

    /**
     * Getter method returns current playlist.
     * @return
     */
    public static ArrayList<SongObject> getPlayList(){
        return songObjectList;
    }

    /**
     * Getter method returns current play list index.
     * @return
     */
    public static int getCurrentIndex(){
        return currentIndex;
    }

    /**
     * Getter method return true if music is currently playing;
     * otherwise it returns false.
     */
    public static boolean musicIsPlaying(){

        //Returns true if music is playing
        return musicIsPlaying = true;
    }

    /**
     * Tries to play a songObject in a new thread by making a call to method playSong().
     * This method will catch errors on failure.
     * Method
     * @param songObject
     */
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

    /**
     * This method will play a song and save the current index of the song to memory.
     * @param songObject
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws IOException
     */
    private static void playSong(SongObject songObject) throws IllegalArgumentException, IllegalStateException, IOException {

        //Log.v("TAG","Current album set is : "+songObjectList.get(currentIndex).albumTitle);
        //Log.v("TAG","Current song set is : "+songObjectList.get(currentIndex).songTitle);

        musicPlayer.reset();
        musicPlayer.reset();
        musicPlayer.setDataSource(songObject.songPath);
        musicPlayer.prepare();
        musicPlayer.start();

        // Update the current song index saved in private app folder.
        // The index key will be the string value
        // of the index, the value will be the int value
        SerializeAndSaveObject.saveObject(String.valueOf(currentIndex), currentIndex);

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

    /**
     * If this method is called from background service,
     * than the application has been opened at least once,
     * and there is a playlist. That playlist may be empty
     * if the user had no music files on their phone. This method
     * deals with both of these two use cases
     *
     */
    static public void togglePauseState(){

        /**
         * If there is no play list
         * a call to SerializeAndSaveObject.loadObject(list)
         * should generate a NPE so we can try for and
         * catch the error.
         *
         * Otherwise, we reset the current list to the saved list.
         * We also set the index.
         */

        try{

            setPlayList((ArrayList<SongObject>)SerializeAndSaveObject.loadObject("list"));
            currentIndex = (int) SerializeAndSaveObject.loadObject("currentIndex");
            tryToPlaySong(songObjectList.get(currentIndex));

        } catch (NullPointerException e){
            Log.e("play non-existent",Log.getStackTraceString(e));
        }

    }
}

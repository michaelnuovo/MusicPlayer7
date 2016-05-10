package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;


import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.Objects.ArtistObject;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.util.ArrayList;

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


        ArrayList<SongObject> songObjectList = new ArrayList<>();
        ArrayList<AlbumObject> albumObjectList = new ArrayList<>();
        ArrayList<ArtistObject> artistObjectList = new ArrayList<>();

        Cursor songListCursor = GetSongListCursor();
        MakeLists(songListCursor, songObjectList, albumObjectList, artistObjectList);
        StaticMusicPlayer mp = new StaticMusicPlayer();

        mp.setPlayList(songObjectList, this);
        mp.tryToPlaySong(songObjectList.get(0));

        runAsForeground();

    }

    /**
     * There are three versions of NotificationCompat.
     * android.app.Notification
     * android.support.v7.app.NotificationCompat
     * android.support.v4.app.NotificationCompat
     * This method currently uses v4
     * See the difference
     * http://stackoverflow.com/questions/18271429/difference-between-android-support-v7-appcompat-and-android-support-v4
     */
    private void runAsForeground(){

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setOngoing(true);

        b.setContentTitle("playing...")
                .setContentText("content")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("ticker");

        startForeground(1337, b.build());
    }



    /**
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getExtras().equals("songObjectList"))
        {
            //Set song
            ArrayList<SongObject> songObjectList =
                    (ArrayList<SongObject>) intent.getExtras().get("data");
            StaticMusicPlayer mp = new StaticMusicPlayer();
            mp.setPlayList(songObjectList);
        }

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
     **/

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart(Intent intent, int startid) {

    }

    private Cursor GetSongListCursor() {
        Uri contentURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String order = MediaStore.Audio.Media.TITLE + " ASC";
        final Cursor mCursor = getContentResolver().query(contentURI, projection, selection, null, order);// Run getContentResolver query
        //DatabaseUtils.dumpCursor(mCursor);
        return mCursor;
    }

    private String GetAlbumArtURI(String[] albumID) {
        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                albumID,
                null
        );
        if (mCursor.moveToFirst()) {

            //DatabaseUtils.dumpCursor(mCursor);
            String str = mCursor.getString(0);
            mCursor.close();
            return str;
        }
        else {
            mCursor.close();
            return null;
        }
    }

    private void MakeLists(Cursor mCursor,
                           final ArrayList<SongObject> songObjectList,
                           final ArrayList<AlbumObject> albumObjectList,
                           final ArrayList<ArtistObject> artistObjectList) {

        try {
            if (mCursor.moveToFirst()) {
                do {
                    final SongObject songObject = new SongObject();

                    /** Making the song objects and adding them to the lists **/

                    // define artist property
                    String artist = mCursor.getString(1);
                    if (artist.equals("<unknown>")) {
                        artist = "Unknown Artist";
                        songObject.artist = artist;
                    } else {
                        artist = mCursor.getString(1);
                        songObject.artist = artist;
                    }
                    final String artistFinal = artist;

                    // define title, duration, path, albumId, title properties
                    final String albumTitle = mCursor.getString(0);
                    songObject.albumTitle = albumTitle;

                    final String songTitle = mCursor.getString(2);
                    songObject.songTitle = songTitle;
                    final String songPath = mCursor.getString(3);
                    songObject.songPath = songPath;
                    final String songDuration = mCursor.getString(4);
                    songObject.songDuration = songDuration;

                    // define albumId property
                    String[] albumID = {mCursor.getString(5)};
                    songObject.albumID = albumID[0];

                    // define albumArtUri property
                    songObject.albumArtURI = GetAlbumArtURI(albumID);


                    /** Download images **/
                    //if(songObject.albumArtURI.equals(targetValue)){ //If the data is an empty string
                    //updatePath = new MediaStoreInterface(ctx);
                    //updatePath.updateMediaStoreAudioAlbumsDataByAlbumId(Long.parseLong(albumID[0]), "Y"); //So we only do one album at a time
                    // requestList.add(songObject);
                    //}

                    // add song object to lists
                    songObjectList.add(songObject);


                    final String albumArtist = songObject.artist; // can an album have more than one artist? yes...
                    final String albumArtURI = songObject.albumArtURI;

                    /** Making the album object and adding it to the list **/
                    if (albumObjectList.size() != 0) {
                        boolean songAdded = false;
                        for (int i = 0; i < albumObjectList.size(); i++) {
                            if (albumObjectList.get(i).albumId == Integer.parseInt(songObject.albumID)) {
                                albumObjectList.get(i).songObjectList.add(songObject);
                                songAdded = true;
                                //Log.v("TAG", "song added");
                                break;
                            }
                        }

                        if (songAdded == false) {
                            //Log.v("TAG", "new album created");
                            albumObjectList.add(new AlbumObject(songObject));
                        }

                    } else {
                        albumObjectList.add(new AlbumObject(songObject));
                    }

                    /** Making the artist object and adding it to the list **/
                    if (artistObjectList.isEmpty()) {
                        artistObjectList.add(new ArtistObject(songObject));
                    } else {

                        boolean bool = false;
                        for (int i = 0; i < artistObjectList.size(); i++) {

                            if (artistObjectList.get(i).artistName.equals(artistFinal)) {
                                artistObjectList.get(i).songObjectList.add(songObject);
                                artistObjectList.get(i).artistTrackCount += 1;

                                bool = true;
                            }
                        }

                        if (bool == false) {
                            ArtistObject newArtistObject = new ArtistObject(songObject);
                            newArtistObject.artistTrackCount += 1;
                            artistObjectList.add(newArtistObject);
                        }
                    }

                } while (mCursor.moveToNext());
            }
        } finally {
            mCursor.close();
        }
    }
}
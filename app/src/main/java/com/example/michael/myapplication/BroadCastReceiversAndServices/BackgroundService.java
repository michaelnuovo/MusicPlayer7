package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import android.support.v4.app.NotificationCompat;

import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.Objects.ArtistObject;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;
import com.example.michael.myapplication.Utilities.Support;

import java.util.ArrayList;

public class BackgroundService extends Service implements AudioManager.OnAudioFocusChangeListener {

    @Override
    public void onAudioFocusChange(int focusChange) {}



    /**
     * Broadcast messages for notification click listeners
     */
    public static final String NOTIFY_PREVIOUS = "com.micha.musicplayertut.previous";
    public static final String NOTIFY_DELETE = "com.micha.musicplayertut.delete";
    public static final String NOTIFY_PAUSE = "com.micha.musicplayertut.pause";
    public static final String NOTIFY_PLAY = "com.micha.musicplayertut.play";
    public static final String NOTIFY_NEXT = "com.micha.musicplayertut.next";

    // Identifier for a specific application component (Activity, Service, BroadcastReceiver, or ContentProvider)
    // that is available. Two pieces of information, encapsulated here, are required to identify a component:
    // the package (a String) it exists in, and the class (a String) name inside of that package.
    // https://developer.android.com/reference/android/content/ComponentName.html
    private ComponentName remoteComponentName;

    private RemoteControlClient remoteControlClient;

    private static boolean currentVersionSupportBigNotification = false;
    private static boolean currentVersionSupportLockScreenControls = false;

    AudioManager audioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * onCreate is called only once to
     * initialize the service class.
     * Subsequent calls to startService() call
     * onStartCommand().
     */
    @Override
    public void onCreate() {

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        /**
         * Registers the custom receiver with the Android system.
         * This receiver cannot be registered in the manifest, and must
         * be set programmatically.
         */

        MusicIntentReceiver receiver = new MusicIntentReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        currentVersionSupportBigNotification = Support.currentVersionSupportBigNotification();
        currentVersionSupportLockScreenControls = Support.currentVersionSupportLockScreenControls();



        ArrayList<SongObject> songObjectList = new ArrayList<>();
        ArrayList<AlbumObject> albumObjectList = new ArrayList<>();
        ArrayList<ArtistObject> artistObjectList = new ArrayList<>();

        Cursor songListCursor = GetSongListCursor();
        MakeLists(songListCursor, songObjectList, albumObjectList, artistObjectList);
        //StaticMusicPlayer mp = new StaticMusicPlayer();

        StaticMusicPlayer.setPlayList(songObjectList, this);
        //StaticMusicPlayer.tryToPlaySong(songObjectList.get(0));


        /**
         * We need to skip this method somehow service is already in foreground.
         * This avoids creating new objects etc. no point in calling the method
         */
        //runAsForeground();

        //bringToForeground();

    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(currentVersionSupportLockScreenControls)
            RegisterRemoteClient();



        bringToForeground();
        return START_STICKY;
    }

    /**
     * Brings background service to foreground
     * with custom notification.
     */
    @SuppressLint("NewApi")
    public void bringToForeground(){

        //String songName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getTitle();
        //String albumName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbum();

        SongObject so = StaticMusicPlayer.getPlayList().get(StaticMusicPlayer.getCurrentIndex());
        String songName = so.songTitle;
        String albumName = so.albumTitle;
        //int albumId =  Integer.parseInt(so.albumID);

        Bitmap albumArt = BitmapFactory.decodeFile(so.albumArtURI);
        boolean SONG_PAUSED = StaticMusicPlayer.getPausedState();

        Log.v("TAG","Title is "+so.songTitle);
        Log.v("TAG","Album art path is "+so.albumArtURI);
        Log.v("TAG","Song path path is "+so.songPath);
        Log.v("TAG","Bitmap reference is "+albumArt);


        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(),R.layout.custom_notification);
        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_notification);


        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_heart)
                .setContentTitle(songName).build();

        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if(currentVersionSupportBigNotification){
            notification.bigContentView = expandedView;
            Log.v("TAG","R$#G%");
        }

        try{
            //long albumId = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAlbumId();
            //Bitmap albumArt = UtilFunctions.getAlbumart(getApplicationContext(), albumId);
            if(albumArt != null){

                notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);
                if(currentVersionSupportBigNotification){
                    notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, albumArt);

                }
            }else{

                notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
                if(currentVersionSupportBigNotification){
                    notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.default_album_art);
                    Log.v("TAG","@#!F$@");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        //if(PlayerConstants.SONG_PAUSED){
        if(SONG_PAUSED){
            notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

            if(currentVersionSupportBigNotification){
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.GONE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            }
        }else{
            notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);

            if(currentVersionSupportBigNotification){
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            }
        }

        notification.contentView.setTextViewText(R.id.textSongName, songName);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumName);
        if(currentVersionSupportBigNotification){
            notification.bigContentView.setTextViewText(R.id.textSongName, songName);
            notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);
        }

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        int NOTIFICATION_ID = 1337;
        startForeground(NOTIFICATION_ID, notification);
    }

    /**
     * Notification click listeners
     * @param view
     */
    public void setListeners(RemoteViews view) {

        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart(Intent intent, int startid) {

    }

    private void RegisterRemoteClient(){

        remoteComponentName = new ComponentName(getApplicationContext(), new BroadCastReceiver().ComponentName());
        try {
            if(remoteControlClient == null) {

                Log.v("TAG","remoteControlClient == null");

                audioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                audioManager.registerRemoteControlClient(remoteControlClient);
            }

            /**
            remoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
             **/
        }catch(Exception ex) {
        }
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
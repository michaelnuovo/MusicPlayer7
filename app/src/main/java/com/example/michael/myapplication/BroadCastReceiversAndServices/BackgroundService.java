package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.Objects.ArtistObject;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.goncalves.pugnotification.interfaces.ImageLoader;
import br.com.goncalves.pugnotification.notification.PugNotification;

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
        //StaticMusicPlayer mp = new StaticMusicPlayer();

        StaticMusicPlayer.setPlayList(songObjectList, this);
        //StaticMusicPlayer.tryToPlaySong(songObjectList.get(0));


        /**
         * We need to skip this method somehow service is already in foreground.
         * This avoids creating new objects etc. no point in calling the method
         */
        //runAsForeground();
        //runAsForeground2();
        //runAsForeground3();
        runAsForeground4();

    }

    private void runAsForeground4(){

        PugNotification.with(context)
                .load()
                .title(title)
                .message(message)
                .bigTextStyle(bigtext)
                .smallIcon(R.drawable.pugnotification_ic_launcher)
                .largeIcon(R.drawable.pugnotification_ic_launcher)
                .flags(Notification.DEFAULT_ALL)
                .color(android.R.color.background_dark)
                .custom()
                .background(url)
                .setImageLoader(Callback)
                .setPlaceholder(R.drawable.pugnotification_ic_placeholder)
                .build();
    }

    private void runAsForeground3(){
        // Set the style
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText("strMessage");

// Build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("title")
                .setContentText("message")
                .setTicker("ticker")
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.button_heart_on)
                //.setContentIntent(intent)
                .setStyle(bigStyle)
                .setWhen(0)
                .setAutoCancel(true)
                .setStyle(bigStyle)
                //.addAction(R.drawable.icon_heart, "string", "pending volume")
                //.addAction(R.drawable.icon_heart, "string", "pendingStop")
                .setOngoing(true);

        //priority max is 2
        //http://stackoverflow.com/questions/13808939/the-method-setpriority-int-is-undefined-for-the-type-notification
        //battery charged status takes higher priority though
        notificationBuilder.setPriority(2);

        //notificationBuilder.build();

        startForeground(1337, notificationBuilder.build());
    }

    private void runAsForeground2(){

        int icon = R.drawable.button_heart_on;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, "Custom Notification", when);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.drawable.button_heart_transparent);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");
        notification.contentView = contentView;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND; // Sound

        mNotificationManager.notify(1, notification);
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

        //Get a bitmap for the notification
        //java.lang.OutOfMemoryError: Failed to allocate a 150994956 byte allocation with 16777120 free bytes and 76MB until OOM
        Bitmap appicon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.button_heart_on);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setOngoing(true);

        /**
         * Set the small icon resource, which will be used to represent the notification in the
         * status bar. The platform template for the expanded view will draw this icon in the left,
         * unless a large icon has also been specified, in which case the small icon will be moved
         * to the right-hand side.
         * http://stackoverflow.com/questions/13847297/notificationcompat-4-1-setsmallicon-and-setlargeicon
         */
        b.setContentTitle("playing...")
                .setColor(Color.parseColor("#191919")) // Set small icon background color (accent color) // Convert hex to int value
                .setPriority(0) // Sets notification's vertical position
                .setContentText("conteasdasdassddasdasdas\ndasdasdasdasdasdasdasdaaaaaa\naaaaa\naa\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasdasdasdasdddddddddddddddddddddddddddddddddddnt")
                .setSmallIcon(R.drawable.button_heart_on) // Set small icon
                //.setLargeIcon(appicon)
                //.setStyle(new Notification.BigPictureStyle().bigPicture(appicon)) // Requires API level 16
                .setTicker("ticker");
                //.build();

        //Expanded notifications only for => API 16
        //My min API is 15
        //RemoteViews bigView = new RemoteViews(getApplicationContext().getPackageName(),
        //        R.layout.notification_layout_big);
        //b.bigContentView  = bigView;

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
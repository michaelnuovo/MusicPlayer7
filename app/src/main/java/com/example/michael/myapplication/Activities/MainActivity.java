package com.example.michael.myapplication.Activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.michael.myapplication.BroadCastReceiversAndServices.MusicIntentReceiver;
import com.example.michael.myapplication.Networking.LastFmAlbumLookup;
import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.Objects.ArtistObject;
import com.example.michael.myapplication.Fragments.FragmentAlbumList;
import com.example.michael.myapplication.Fragments.FragmentArtistList;
import com.example.michael.myapplication.Fragments.FragmentSongList;
import com.example.michael.myapplication.Adapters.PageAdapterMainActivity;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.Utilities.AlbumArt;
import com.example.michael.myapplication.Utilities.MediaStoreInterface;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;
import com.example.michael.myapplication.BroadCastReceiversAndServices.BackgroundService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<SongObject> songObjectList = new ArrayList<>();
    static ArrayList<AlbumObject> albumObjectList = new ArrayList<>();
    static ArrayList<ArtistObject> artistObjectList = new ArrayList<>();

    PageAdapterMainActivity pageAdapter;
    ViewPager viewPager;

    static public ArrayList<ArtistObject> getArtistObjectList(){
        return artistObjectList;
    }

    static public ArrayList<AlbumObject> getAlbumObjectList(){
        return albumObjectList;
    }

    public void doNetworkingStuff(){
        MediaStoreInterface mint = new MediaStoreInterface(ctx);
        //mint.clearFolder("myalbumart");

        AlbumArt aa = new AlbumArt(this,albumObjectList);
        aa.resetPaths(); // set to album path to null if there are no native images
        aa.dumpAlbumColumns();

        LastFmAlbumLookup lf = new LastFmAlbumLookup(this,albumObjectList);
        lf.makeRequest();

        printAlbums();
    }

    public void printAlbums(){
        for(int i=0;i<albumObjectList.size();i++){
            Log.v("TAG","albumObjectList.get(i).albumTitle is "+albumObjectList.get(i).albumTitle);
            Log.v("TAG","albumObjectList.get(i).albumArtURI uri is "+albumObjectList.get(i).albumArtURI);
            Log.v("TAG","albumObjectList.get(i).albumArtURICenterCroppedToScreen is "+albumObjectList.get(i).albumArtURICenterCroppedToScreen);
            Log.v("TAG","albumArtURIScaledToScreenWidth is "+albumObjectList.get(i).albumArtURIScaledToScreenWidth);
        }
    }

    static Context ctx;
    public static Context getAppContext() {
        return ctx;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //pageAdapter.notifyDataSetChanged();
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(isMyServiceRunning(BackgroundService.class)){
            Log.v("TAG","My service is running");
        } else {
            Log.v("TAG","My service is not running");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = getApplicationContext();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        List<Fragment> fragments = getFragments(songObjectList, artistObjectList, albumObjectList);
        pageAdapter = new PageAdapterMainActivity(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //viewPager.setPageTransformer(true, new DepthPageTransformer());
        //viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icon_playlist);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_heart);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_musicalnotes);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_head);
        tabLayout.getTabAt(4).setIcon(R.drawable.icon_album);

        //TextView tab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        //tab.setText("Library");
        //tab.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_heart, 0, 0);
        //tabLayout.getTabAt(2).setCustomView(tab);

        Cursor songListCursor = GetSongListCursor();
        MakeLists(songListCursor, songObjectList, albumObjectList, artistObjectList);
        StaticMusicPlayer.setPlayList(songObjectList);
        //AlbumArt.cropAndSave(songObjectList);

        for(int i=0;i<albumObjectList.size();i++){
            Log.v("TAG", "album title: " + albumObjectList.get(i).albumTitle);
        }

        doNetworkingStuff();
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private List<Fragment> getFragments(ArrayList<SongObject> songObjectList,
                                        ArrayList<ArtistObject> artistObjectList,
                                        ArrayList<AlbumObject> albumObjectList) {
        // An empty array list
        List<Fragment> fList = new ArrayList<Fragment>();
        // Initialize and add fragment objects to the array list
        fList.add(FragmentSongList.newInstance(songObjectList));      //lists
        fList.add(FragmentArtistList.newInstance(artistObjectList));  //likes
        fList.add(FragmentSongList.newInstance(songObjectList));     //songs
        fList.add(FragmentArtistList.newInstance(artistObjectList));     //artists
        fList.add(FragmentAlbumList.newInstance(albumObjectList));    //albums
        //fList.add(AlbumList.newInstance(albumObjectList));
        // Return the fragment object array list for adaption to the pager view
        return fList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        try{
            if (mCursor.moveToFirst()) {
                do {
                    final SongObject songObject = new SongObject();

                    /** Making the song objects and adding them to the lists **/

                    // define artist property
                    String artist = mCursor.getString(1);
                    if (artist.equals("<unknown>")) {artist = "Unknown Artist"; songObject.artist = artist;}
                    else {artist = mCursor.getString(1);songObject.artist = artist;}
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
                    if(albumObjectList.size() != 0){
                        boolean songAdded = false;
                        for(int i=0;i<albumObjectList.size();i++){
                            if(albumObjectList.get(i).albumId == Integer.parseInt(songObject.albumID)){
                                albumObjectList.get(i).songObjectList.add(songObject);
                                songAdded = true;
                                //Log.v("TAG", "song added");
                                break;
                            }
                        }

                        if(songAdded == false){
                            //Log.v("TAG", "new album created");
                            albumObjectList.add(new AlbumObject(songObject));
                        }

                    } else {
                        albumObjectList.add(new AlbumObject(songObject));
                    }

                    /** Making the artist object and adding it to the list **/
                    if(artistObjectList.isEmpty()){
                        artistObjectList.add(new ArtistObject(songObject));
                    } else {

                        boolean bool = false;
                        for(int i = 0; i < artistObjectList.size(); i++) {

                            if(artistObjectList.get(i).artistName.equals(artistFinal)) {
                                artistObjectList.get(i).songObjectList.add(songObject);
                                artistObjectList.get(i).artistTrackCount += 1;

                                bool = true;
                            }
                        }

                        if(bool == false){
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

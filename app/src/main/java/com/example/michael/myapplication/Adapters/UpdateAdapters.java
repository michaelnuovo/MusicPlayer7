package com.example.michael.myapplication.Adapters;

import android.util.Log;
import android.widget.ListView;

public class UpdateAdapters {

    private static UpdateAdapters instance = null;

    private FragmentListAdapterArtistList fragmentListAdapterArtistList;
    private FragmentListAdapterSongsList fragmentListAdapterSongsList;
    private FragmentGridViewAdapterAlbumList fragmentGridViewAdapterAlbumList;

    private ListView MyListViewTracks;

    //Constructor
    protected UpdateAdapters() { }

    //Get the current instance
    public static UpdateAdapters getInstance() {
        if (instance == null) {
            // create a new one if it doesn't exist
            instance = new UpdateAdapters();
        }
        return instance;
    }

    //Setters
    public void setAdapterOne(FragmentListAdapterSongsList fragmentListAdapterSongsList, ListView MyListViewTracks){
        this.fragmentListAdapterSongsList = fragmentListAdapterSongsList;
        this.MyListViewTracks=MyListViewTracks;
    }

    public void setAdapterTwo(FragmentListAdapterArtistList fragmentListAdapterArtistList){
        this.fragmentListAdapterArtistList = fragmentListAdapterArtistList;
    }

    public void setAdapterThree(FragmentGridViewAdapterAlbumList fragmentGridViewAdapterAlbumList){
        this.fragmentGridViewAdapterAlbumList = fragmentGridViewAdapterAlbumList;
    }

    //Public methods
    public void update(){

        if(null != fragmentListAdapterArtistList) {
            fragmentListAdapterArtistList.notifyDataSetChanged();
            Log.v("TAG", "myListAdapterArtists successfully updated ");
        } else {
            Log.v("TAG","myListAdapterArtists is a null reference ");}
        //http://stackoverflow.com/questions/32079931/difference-between-listview-invalidate-vs-invalidateviews
        if(null != fragmentListAdapterSongsList) {                 //http://stackoverflow.com/questions/10676720/is-there-any-difference-between-listview-invalidateviews-and-adapter-notify
            //refreshes the views in the listview
            fragmentListAdapterSongsList.notifyDataSetChanged();   //Notifying the dataset changed will cause the listview to invoke your adapters methods again to adjust scrollbars, regenerate item views, etc...
            //MyListViewTracks.invalidate();
            Log.v("TAG", "myListAdapterTracks successfully updated ");
        } else {
            Log.v("TAG","myListAdapterTracks is a null reference ");}

        if(null != fragmentGridViewAdapterAlbumList) {
            fragmentGridViewAdapterAlbumList.notifyDataSetChanged();
            Log.v("TAG", "myGridViewAdapter successfully updated ");
        } else {
            Log.v("TAG","myGridViewAdapter is a null reference ");
        }
    }
}
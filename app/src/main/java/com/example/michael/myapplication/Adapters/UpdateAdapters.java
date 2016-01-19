package com.example.michael.myapplication.Adapters;

import android.util.Log;
import android.widget.ListView;

/**
 * This class is called from the image downloader class(es)
 * every time a new image is downloaded for an album.
 * The purpose is to update the application's adapters within a single class
 * every time a new image is downloaded. Therefore, every adapter, and every new adapter
 * should be set as fields.
 *
 * This class may have been made simpler using a hash map to contain the adapters.
 */

public class UpdateAdapters {

    private static UpdateAdapters instance = null;

    //Fragment adapters
    private FragmentListAdapterArtistList fragmentListAdapterArtistList;
    private FragmentListAdapterSongsList fragmentListAdapterSongsList;
    private FragmentGridViewAdapterAlbumList fragmentGridViewAdapterAlbumList;

    //Information panel adapter
    private PageAdapterInfoPanel pageAdapterInfoPanel;


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

    public void setAdapterFour(PageAdapterInfoPanel pageAdapterInfoPanel){
        this.pageAdapterInfoPanel=pageAdapterInfoPanel;
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

        if(null != pageAdapterInfoPanel) {
            pageAdapterInfoPanel.notifyDataSetChanged();
            Log.v("TAG", "pageAdapterInfoPanel successfully updated ");
        } else {
            Log.v("TAG","pageAdapterInfoPanel is a null reference ");
        }
    }
}
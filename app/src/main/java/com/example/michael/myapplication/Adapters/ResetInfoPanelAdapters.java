package com.example.michael.myapplication.Adapters;

import android.support.v4.view.ViewPager;

import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

/**
 * Created by Michael on 1/13/2016.
 */
public class ResetInfoPanelAdapters {

    static private PageAdapterInfoPanelMain singleArtistActivityAdapter;
    static private PageAdapterInfoPanelMain singleAlbumActivityAdapter;
    static private PageAdapterInfoPanelMain songsListFragmentAdapter;

    static private ViewPager singleArtistActivityViewPager;
    static private ViewPager singleAlbumActivityViewPager;
    static private ViewPager songsListFragmentViewPager;

    public static void setSingleArtistActivityPager(PageAdapterInfoPanelMain _singleArtistActivityAdapter){
        singleArtistActivityAdapter=_singleArtistActivityAdapter;
    }

    public static void setSingleAlbumActivityPager(PageAdapterInfoPanelMain _singleAlbumActivityAdapter){
        singleAlbumActivityAdapter=_singleAlbumActivityAdapter;
    }

    public static void setSongListFragmentPager(PageAdapterInfoPanelMain _songsListFragmentAdapter){
        songsListFragmentAdapter=_songsListFragmentAdapter;
    }

    public static void setSingleArtistActivityViewPager(ViewPager _singleArtistActivityViewPager){
        singleArtistActivityViewPager=_singleArtistActivityViewPager;
    }

    public static void setSingleAlbumActivityViewPager(ViewPager _singleAlbumActivityViewPager){
        singleAlbumActivityViewPager=_singleAlbumActivityViewPager;
    }

    public static void setSongsListFragmentViewPager(ViewPager _songsListFragmentViewPager){
        songsListFragmentViewPager=_songsListFragmentViewPager;
    }

    //Reset the adapter to the view pager and set the item
    public static void resetAllInfoPanelAdaptersIfTheyExist(){



        if(null != singleArtistActivityAdapter){singleArtistActivityViewPager.setAdapter(singleArtistActivityAdapter);singleArtistActivityAdapter.notifyDataSetChanged();singleArtistActivityViewPager.setCurrentItem(StaticMusicPlayer.getCurrentIndex());}
        if(null != singleAlbumActivityAdapter){singleAlbumActivityViewPager.setAdapter(singleAlbumActivityAdapter);singleAlbumActivityAdapter.notifyDataSetChanged();singleAlbumActivityViewPager.setCurrentItem(StaticMusicPlayer.getCurrentIndex());}
        if(null != songsListFragmentAdapter){songsListFragmentViewPager.setAdapter(songsListFragmentAdapter);songsListFragmentAdapter.notifyDataSetChanged();songsListFragmentViewPager.setCurrentItem(StaticMusicPlayer.getCurrentIndex()); }
    }
}

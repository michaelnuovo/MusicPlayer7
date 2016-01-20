package com.example.michael.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.michael.myapplication.Activities.PlayPanel;
import com.example.michael.myapplication.Adapters.FragmentListAdapterSongsList;
import com.example.michael.myapplication.Adapters.PageAdapterInfoPanelMain;
import com.example.michael.myapplication.Adapters.ResetInfoPanelAdapters;
import com.example.michael.myapplication.Adapters.UpdateAdapters;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.util.ArrayList;

public class FragmentSongList extends Fragment {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private ArrayList<SongObject> songObjectList;
    boolean onPageScrollStateChanged = false;

    static public PageAdapterInfoPanelMain pageAdapterInfoPanelMain;
    public static ViewPager infoPanelViewPager;

    public static final FragmentSongList newInstance(ArrayList<SongObject> arrayList){

        FragmentSongList f = new FragmentSongList();
        Bundle bdl = new Bundle(1);
        bdl.putParcelableArrayList(EXTRA_MESSAGE, arrayList);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.fragmentListView);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();

                Intent intent = new Intent(getActivity(), PlayPanel.class);
                startActivity(intent);
                ArrayList<SongObject> shuffedPlayList = StaticMusicPlayer.returnShuffledList(songObjectList);
                StaticMusicPlayer.setPlayList(shuffedPlayList);
                StaticMusicPlayer.tryToPlaySong(shuffedPlayList.get(0));
            }
        });

        songObjectList = getArguments().getParcelableArrayList(EXTRA_MESSAGE);

        //List adapter
        FragmentListAdapterSongsList listAdapter = new FragmentListAdapterSongsList(getActivity(), R.layout.list_item_songs_fragment, songObjectList);
        listView = (ListView) rootView.findViewById(R.id.fragmentListView);
        listView.setAdapter(listAdapter);

        //Information Panel Page Adapter
        pageAdapterInfoPanelMain = new PageAdapterInfoPanelMain(getActivity(), StaticMusicPlayer.getPlayList());
        infoPanelViewPager = (ViewPager) rootView.findViewById(R.id.infoPanelPager);
        infoPanelViewPager.setAdapter(pageAdapterInfoPanelMain);
        infoPanelViewPager.setCurrentItem(0);

        //Pass the view pager and the adapter to the ResetInfoPanelAdapters class for future resetting (when the play panel opens)
        ResetInfoPanelAdapters.setSongListFragmentPager(pageAdapterInfoPanelMain);
        ResetInfoPanelAdapters.setSongsListFragmentViewPager(infoPanelViewPager);

        //Update Adapters
        UpdateAdapters.getInstance().setAdapterOne(listAdapter, listView);
        UpdateAdapters.getInstance().setAdapterFour(pageAdapterInfoPanelMain);

        //Set list item click listener
        setListItemClickListener(listView);


        setPagerListener(infoPanelViewPager);

        return rootView;
    }

    private void setPagerListener(ViewPager infoPanelPager){



        infoPanelPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) { //this doest not return true during another fragment as expected
                Log.v("TAG","Page onPageScrollStateChanged here");
                onPageScrollStateChanged = true;
            }

            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                Log.v("TAG","Page scrolled here");
            }

            public void onPageSelected(int position) { //this returns true during another fragment for some reason!
                Log.v("TAG","Page selected here");
                if(onPageScrollStateChanged == true){ // this also needs to be true, and will be true first
                    //StaticMusicPlayer.tryToPlaySong(StaticMusicPlayer.getPlayList().get(position));
                    onPageScrollStateChanged = false; // reset to false
                }
            }
        });
    }

    public void setListItemClickListener(ListView listView){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Log.v("TAG", "List item click position is : " + String.valueOf(arg2));
                Log.v("TAG", "Song object list size is : " + String.valueOf(StaticMusicPlayer.getPlayList().size()));

                System.gc();

                Intent intent = new Intent(getActivity(), PlayPanel.class);
                startActivity(intent);
                StaticMusicPlayer.setPlayList(songObjectList);
                StaticMusicPlayer.tryToPlaySong(songObjectList.get(arg2));
            }
        });
    }
}

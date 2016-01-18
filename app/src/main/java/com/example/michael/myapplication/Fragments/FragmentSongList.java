package com.example.michael.myapplication.Fragments;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.example.michael.myapplication.Adapters.PageAdapterInfoPanel;
import com.example.michael.myapplication.Adapters.ResetInfoPanelAdapters;
import com.example.michael.myapplication.Adapters.UpdateAdapters;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.util.ArrayList;

public class FragmentSongList extends Fragment {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private ArrayList<SongObject> songObjectList;

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

                Intent intent = new Intent(getActivity(), PlayPanel.class);
                startActivity(intent);
                ArrayList<SongObject> shuffedPlayList = StaticMusicPlayer.returnShuffledList(songObjectList);
                StaticMusicPlayer.setPlayList(shuffedPlayList);
                StaticMusicPlayer.tryToPlaySong(shuffedPlayList.get(0));
            }
        });

        songObjectList = getArguments().getParcelableArrayList(EXTRA_MESSAGE);

        //List adapter
        FragmentListAdapterSongsList adapter = new FragmentListAdapterSongsList(getActivity(), R.layout.list_item_songs_fragment, songObjectList);
        listView = (ListView) rootView.findViewById(R.id.fragmentListView);
        listView.setAdapter(adapter);

        //Page Adapter
        PageAdapterInfoPanel pageAdapterInfoPanel = new PageAdapterInfoPanel(getActivity(), songObjectList);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.infoPanelPager);
        viewPager.setAdapter(pageAdapterInfoPanel);
        viewPager.setCurrentItem(StaticMusicPlayer.getCurrentIndex());

        //Pass the view pager and the adapter to the ResetInfoPanelAdapters class for future resetting (when the play panel opens)
        ResetInfoPanelAdapters.setSongListFragmentPager(pageAdapterInfoPanel);
        ResetInfoPanelAdapters.setSongsListFragmentViewPager(viewPager);

        //Update Adapters
        UpdateAdapters.getInstance().setAdapterOne(adapter, listView);

        //Set list item click listener
        setListItemClickListener(listView);


        setPagerListener(viewPager);

        return rootView;
    }

    private void setPagerListener(ViewPager viewPager){

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                //backgroundViewPager.setCurrentItem(position);
                Log.v("TAG", "!@#F#$Wf");
                StaticMusicPlayer.tryToPlaySong(StaticMusicPlayer.getPlayList().get(position));
            }
        });
    }

    public void setListItemClickListener(ListView listView){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent(getActivity(), PlayPanel.class);
                startActivity(intent);
                StaticMusicPlayer.tryToPlaySong(songObjectList.get(arg2));
            }
        });
    }
}

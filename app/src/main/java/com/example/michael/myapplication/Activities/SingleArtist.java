package com.example.michael.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.michael.myapplication.Adapters.FragmentListAdapterSongsList;
import com.example.michael.myapplication.Adapters.PageAdapterInfoPanelOther;
import com.example.michael.myapplication.Adapters.UpdateAdapters;
import com.example.michael.myapplication.Fragments.FragmentArtistList;
import com.example.michael.myapplication.Fragments.FragmentSongList;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.util.ArrayList;

public class SingleArtist extends AppCompatActivity {

    boolean SONGSTARTED = false;
    ArrayList<SongObject> songObjectList;

    @Override
    public void onBackPressed() {

        System.gc();
        finish();

        if(SONGSTARTED==true){
            //ResetInfoPanelAdapters.resetAllInfoPanelAdaptersIfTheyExist();
            FragmentSongList.pageAdapterInfoPanelMain.resetData(songObjectList);
            //FragmentSongList.infoPanelViewPager.setAdapter(FragmentSongList.pageAdapterInfoPanelMain);
            FragmentSongList.pageAdapterInfoPanelMain.notifyDataSetChanged(); //should only do if a song has been played
            FragmentSongList.infoPanelViewPager.setCurrentItem(StaticMusicPlayer.getCurrentIndex());
        }
        SONGSTARTED=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_artist);

        songObjectList = FragmentArtistList.artistSelectedFromListView.songObjectList;

        //Set the play list

        //PageAdapterInfoPanel.pageAdapterInfoPanel.notifyDataSetChanged();
        //StaticMusicPlayer.setPlayList(songObjectList);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.fragmentListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.gc();
                Intent intent = new Intent(getApplicationContext(), PlayPanel.class);
                startActivity(intent);
                songObjectList = StaticMusicPlayer.returnShuffledList(songObjectList);
                StaticMusicPlayer.setPlayList(songObjectList);
                StaticMusicPlayer.tryToPlaySong(songObjectList.get(0));
                SONGSTARTED=true;
            }
        });


        //List adapter
        FragmentListAdapterSongsList adapter = new FragmentListAdapterSongsList(this, R.layout.list_item_songs_fragment, songObjectList);
        listView = (ListView) findViewById(R.id.fragmentListView);
        listView.setAdapter(adapter);

        //Information Panel Page Adapter
        PageAdapterInfoPanelOther pageAdapterInfoPanelOther = PageAdapterInfoPanelOther.getInstance(this, songObjectList);
        ViewPager infoPanelPager = (ViewPager) findViewById(R.id.infoPanelPager);
        infoPanelPager.setAdapter(pageAdapterInfoPanelOther);
        infoPanelPager.setCurrentItem(0);

        //Update Adapters
        UpdateAdapters.getInstance().setAdapterOne(adapter, listView);

        //Set list item click listener
        setListItemClickListener(listView);
    }

    public void setListItemClickListener(ListView listView){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent(getApplicationContext(), PlayPanel.class);
                startActivity(intent);
                StaticMusicPlayer.setPlayList(songObjectList);
                StaticMusicPlayer.tryToPlaySong(StaticMusicPlayer.getPlayList().get(arg2));
            }
        });
    }
}

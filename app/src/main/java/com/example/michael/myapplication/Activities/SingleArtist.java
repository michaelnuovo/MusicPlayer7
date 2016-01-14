package com.example.michael.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.michael.myapplication.Adapters.FragmentListAdapterSongsList;
import com.example.michael.myapplication.Adapters.PageAdapterInfoPanel;
import com.example.michael.myapplication.Adapters.ResetInfoPanelAdapters;
import com.example.michael.myapplication.Adapters.UpdateAdapters;
import com.example.michael.myapplication.Fragments.FragmentArtistList;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.util.ArrayList;

public class SingleArtist extends AppCompatActivity {

    ArrayList<SongObject> songObjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_artist);

        //Set the play list
        songObjectList = FragmentArtistList.getSelectedArtist().songObjectList;
        StaticMusicPlayer.setPlayList(songObjectList);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.fragmentListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //List adapter
        FragmentListAdapterSongsList adapter = new FragmentListAdapterSongsList(this, R.layout.list_item_songs_fragment, songObjectList);
        listView = (ListView) findViewById(R.id.fragmentListView);
        listView.setAdapter(adapter);

        //Page Adapter
        PageAdapterInfoPanel pageAdapterInfoPanel = new PageAdapterInfoPanel(this, songObjectList);
        ViewPager viewPager = (ViewPager) findViewById(R.id.infoPanelPager);
        viewPager.setAdapter(pageAdapterInfoPanel);

        //Pass the view pager and the adapter to the ResetInfoPanelAdapters class for future resetting (when the play panel opens)
        ResetInfoPanelAdapters.setSongListFragmentPager(pageAdapterInfoPanel);
        ResetInfoPanelAdapters.setSongsListFragmentViewPager(viewPager);


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
                StaticMusicPlayer.tryToPlaySong(songObjectList.get(arg2));
            }
        });
    }
}

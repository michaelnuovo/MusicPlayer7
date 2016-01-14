package com.example.michael.myapplication.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.michael.myapplication.Adapters.PageAdapterInfoPanel;
import com.example.michael.myapplication.Adapters.ResetInfoPanelAdapters;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

public class PlayPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_panel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Reset info panel pager so that song title and song artist refresh
        //PageAdapterInfoPanel pageAdapterInfoPanel = new PageAdapterInfoPanel(this, StaticMusicPlayer.getPlayList());
        //ViewPager viewPager = (ViewPager) findViewById(R.id.infoPanelPager);
        //viewPager.setAdapter(pageAdapterInfoPanel);

        ResetInfoPanelAdapters.resetAllInfoPanelAdaptersIfTheyExist();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

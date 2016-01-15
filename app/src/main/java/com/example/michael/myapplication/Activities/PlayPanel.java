package com.example.michael.myapplication.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.michael.myapplication.Adapters.PageAdapterInfoPanel;
import com.example.michael.myapplication.Adapters.PageAdapterPlayPanel;
import com.example.michael.myapplication.Adapters.ResetInfoPanelAdapters;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Tranformations.FadePageTransformer;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.io.IOException;

public class PlayPanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_panel);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Reset info panel pager so that song title and song artist refresh
        //PageAdapterInfoPanel pageAdapterInfoPanel = new PageAdapterInfoPanel(this, StaticMusicPlayer.getPlayList());
        //ViewPager viewPager = (ViewPager) findViewById(R.id.infoPanelPager);
        //viewPager.setAdapter(pageAdapterInfoPanel);

        ResetInfoPanelAdapters.resetAllInfoPanelAdaptersIfTheyExist();

        final RelativeLayout playPanelLayout = (RelativeLayout) findViewById(R.id.playPanelLayout);
        Bitmap bm = BitmapFactory.decodeFile(StaticMusicPlayer.getPlayList().get(StaticMusicPlayer.getCurrentIndex()).albumArtURI);
        Drawable dw = new BitmapDrawable(bm);
        Bitmap blurred = Bitmap.createScaledBitmap(bm, 5, 5, true);
        Drawable dw2 = new BitmapDrawable(blurred);
        playPanelLayout.setBackground(dw2);

        final PageAdapterPlayPanel adapter = new PageAdapterPlayPanel(this, StaticMusicPlayer.getPlayList());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.playPanelViewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new FadePageTransformer());

        final ViewPager viewPager2 = (ViewPager) findViewById(R.id.playPanelViewPager2);
        viewPager2.setAdapter(adapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                fab.setBackground(getDrawable(R.drawable.button_pause_notfocused_pressed));
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager2.onTouchEvent(event);
                return false;
            }
        });

        viewPager2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.onTouchEvent(event);
                return false;
            }
        });

        /*
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {

            }
        });*/
    }


}

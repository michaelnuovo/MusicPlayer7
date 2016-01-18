package com.example.michael.myapplication.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eftimoff.viewpagertransformers.RotateUpTransformer;
import com.example.michael.myapplication.Adapters.PageAdapterBackground;
import com.example.michael.myapplication.Adapters.PageAdapterMini;
import com.example.michael.myapplication.Adapters.ResetInfoPanelAdapters;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Tranformations.DepthPageTransformer;
import com.example.michael.myapplication.Tranformations.FadePageTransformer;
import com.example.michael.myapplication.Tranformations.ZoomOutPageTransformer;
import com.example.michael.myapplication.Utilities.Dimensions;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

public class PlayPanel extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        //ResetInfoPanelAdapters.resetAllInfoPanelAdaptersIfTheyExist();
        finish();
    }

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



        /*
        final RelativeLayout playPanelLayout = (RelativeLayout) findViewById(R.id.playPanelLayout);
        Bitmap bm = BitmapFactory.decodeFile(StaticMusicPlayer.getPlayList().get(StaticMusicPlayer.getCurrentIndex()).albumArtURI);
        Drawable dw = new BitmapDrawable(bm);
        Bitmap blurred = Bitmap.createScaledBitmap(bm, 5, 5, true);
        Drawable dw2 = new BitmapDrawable(blurred);
        playPanelLayout.setBackground(dw2);*/

        //First pager and adapter
        final PageAdapterBackground backgroundAdapter = new PageAdapterBackground(this, StaticMusicPlayer.getPlayList());
        final ViewPager backgroundViewPager = (ViewPager) findViewById(R.id.backgroundViewPager);
        backgroundViewPager.setAdapter(backgroundAdapter);
        backgroundViewPager.setPageTransformer(false, new FadePageTransformer());
        backgroundViewPager.setCurrentItem(StaticMusicPlayer.getCurrentIndex());

        //Second pager and adapter
        final PageAdapterMini miniAdapter = new PageAdapterMini(this, StaticMusicPlayer.getPlayList());
        final ViewPager miniViewPager = (ViewPager) findViewById(R.id.miniViewPager);
        miniViewPager.setAdapter(miniAdapter);
        miniViewPager.setCurrentItem(StaticMusicPlayer.getCurrentIndex());
        //miniViewPager.setPageTransformer(false, new DepthPageTransformer());
        miniViewPager.setPageTransformer(true, new RotateUpTransformer());
        //Second pager dimensions
        //miniViewPager.getLayoutParams().width = Dimensions.getWidth();
        //miniViewPager.getLayoutParams().height = Dimensions.getHeight();
        //Second pager padding
        //float scale = getResources().getDisplayMetrics().density;
        //int sizeInDp = 15; //Set padding in dp here
        //int dpAsPixels = (int) (sizeInDp*scale + 0.5f);//Converts dp to pixels
        //miniViewPager.setPadding(0,dpAsPixels,0,0);//Sets padding in pixels
        setPagerListeners(backgroundViewPager,miniViewPager);

        sliderListener sldListener = new sliderListener();
        SeekBar songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songProgressBar.setOnSeekBarChangeListener(sldListener);

        try {
            Thread.sleep(1000);
            //ResetInfoPanelAdapters.resetAllInfoPanelAdaptersIfTheyExist();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class sliderListener implements SeekBar.OnSeekBarChangeListener {
        private int smoothnessFactor = 10;
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //progress = Math.round(progress / smoothnessFactor);
            //TextView lblProgress = (TextView) findViewById(R.id.songProgressBar);
            //lblProgress.setText(String.valueOf(progress));
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            seekBar.setProgress(Math.round((seekBar.getProgress() + (smoothnessFactor / 2)) / smoothnessFactor) * smoothnessFactor);
        }
    }

    public void setPagerListeners(final ViewPager backgroundViewPager, final ViewPager miniViewPager){

        backgroundViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                miniViewPager.onTouchEvent(event);
                return false;
            }
        });

        miniViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                backgroundViewPager.onTouchEvent(event);
                return false;
            }
        });

        miniViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                backgroundViewPager.setCurrentItem(position);
                StaticMusicPlayer.tryToPlaySong(StaticMusicPlayer.getPlayList().get(position));
            }
        });
    }
}


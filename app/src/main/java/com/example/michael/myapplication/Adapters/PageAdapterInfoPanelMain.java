package com.example.michael.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.michael.myapplication.Activities.PlayPanel;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.MyBitmaps;
import com.example.michael.myapplication.Utilities.ScaleCenterCrop;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.io.File;
import java.util.ArrayList;


public class PageAdapterInfoPanelMain extends PagerAdapter {

    Context ctx;
    static ArrayList<SongObject> songObjectList;
    public static PageAdapterInfoPanelMain pageAdapterInfoPanelMain;

    public PageAdapterInfoPanelMain(Context ctx, ArrayList<SongObject> songObjectList) {

        this.ctx = ctx;
        this.songObjectList=songObjectList;
    }

    public void resetData(ArrayList<SongObject> _songObjectList){
        songObjectList=_songObjectList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {


        LayoutInflater inflater = LayoutInflater.from(ctx);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_layout_infopanel, collection, false); //a view group is a layout, a view is a child of a view group
        collection.addView(layout);

        TextView footer_song_title_pager = (TextView) layout.findViewById(R.id.footer_song_title_pager);
        TextView footer_song_artist_pager = (TextView) layout.findViewById(R.id.footer_song_artist_pager);

        footer_song_title_pager.setText(songObjectList.get(position).songTitle);
        footer_song_artist_pager.setText(songObjectList.get(position).artist);

        setBackground(position, layout);

        return layout;
    }

    public void setBackground(int position, ViewGroup layout){

        File file = new File(songObjectList.get(position).albumArtURI);
        if(isImage(file)){
            Log.v("TAG", "songObjectList.get(position).albumArtURI is not null " + songObjectList.get(position).albumArtURI);
            String path = getInfoPanelArtUri(songObjectList.get(position).albumArtURI);
            Bitmap bm = BitmapFactory.decodeFile(path);
            BitmapDrawable drbl = new BitmapDrawable(bm);
            layout.setBackground(drbl);
        }else{
            Log.v("TAG", "songObjectList.get(position).albumArtURI is null");
            Bitmap filler = MyBitmaps.getFillerAlbum();
            BitmapDrawable dw = new BitmapDrawable(filler);
            layout.setBackgroundDrawable(dw);
        }
    }

    public static boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        return options.outWidth != -1 && options.outHeight != -1;
    }

    private String getInfoPanelArtUri(String path){

        String[] array = path.split(".jpg");
        Log.v("TAG", "path is " + path);
        Log.v("TAG", "array[0] is " + array[0]);
        String destination = array[0] + "_informationPanel_" + ".jpg";
        return destination;
    }

    public void setPanelClickListener(ViewGroup layout, final int position){

        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ctx, PlayPanel.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
                StaticMusicPlayer.tryToPlaySong(songObjectList.get(position));
            }
        });
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE; //This makes the view pager update on notifyDataSetChanged(); see "ViewPager PaderAdapter no updating the View" on SO
                              //The view pager only updates a view when the position returns none
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return songObjectList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

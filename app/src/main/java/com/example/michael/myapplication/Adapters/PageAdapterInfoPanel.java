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


public class PageAdapterInfoPanel extends PagerAdapter {

    Context ctx;
    static ArrayList<SongObject> songObjectList;
    public static PageAdapterInfoPanel pageAdapterInfoPanel;

    public PageAdapterInfoPanel(Context ctx, ArrayList<SongObject> songObjectList) {

        this.ctx = ctx;
        this.songObjectList=songObjectList;
    }

    public static PageAdapterInfoPanel getInstance(Context ctx, ArrayList<SongObject> _songObjectList){
        if(pageAdapterInfoPanel==null){
            pageAdapterInfoPanel=new PageAdapterInfoPanel(ctx, _songObjectList);
            pageAdapterInfoPanel.notifyDataSetChanged();
            return pageAdapterInfoPanel;
        }else{
            songObjectList = _songObjectList; //need to reset the list that will be adapted
            pageAdapterInfoPanel.notifyDataSetChanged();
            return pageAdapterInfoPanel;
        }
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

        setPanelClickListener(layout, position);

        layout.setTag(position);
        Drawable drbl = getDrawable(layout, position);
        layout.setBackground(drbl);
        System.gc();

        return layout;

    }

    public Drawable getDrawable(final ViewGroup layout, int position){

        if(!MyBitmaps.hashMap.containsKey(String.valueOf(position))){ //if the hash map does not contain the drawable then make one
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            Bitmap bm = BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI,options);
            BitmapDrawable drbl = new BitmapDrawable(bm);
            MyBitmaps.hashMap.put(String.valueOf(position),drbl);
            MyBitmaps.trimHashList(MyBitmaps.hashMap, position);
            return drbl;
        } else {
            return (Drawable) MyBitmaps.hashMap.get(String.valueOf(position));
        }
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

    public void setViewPagerBackground(ViewGroup layout, int position){

        if(null!=songObjectList.get(position).albumArtURI){
            File imageFile = new File(songObjectList.get(position).albumArtURI);
            if(imageFile.exists()){

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;

                Point size = new Point();
                Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                display.getSize(size);
                Bitmap bm = BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI, options); //h/w

                if(null==bm){
                    ScaleCenterCrop scaleCenterCrop = new ScaleCenterCrop();
                    Bitmap filler = MyBitmaps.getFillerAlbum();

                    BitmapDrawable dw = new BitmapDrawable(filler);
                    layout.setBackgroundDrawable(dw);
                    //scaleCenterCrop.recycleBitmaps();
                    //filler.recycle();
                }
                if(null!=bm){

                    //Bitmap bmd = BitmapDarken.darkenBitMap(bm);
                    BitmapDrawable dw = new BitmapDrawable(bm);
                    layout.setBackgroundDrawable(dw);
                }
            }
        }
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE; //This makes the view pager update; see "ViewPager PaderAdapter no updating the View" on SO
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

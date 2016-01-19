package com.example.michael.myapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.Dimensions;
import com.example.michael.myapplication.Utilities.MyBitmaps;
import com.example.michael.myapplication.Utilities.ScaleCenterCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Michael on 1/14/2016.
 */
public class PageAdapterBackground extends PagerAdapter {

    Context ctx;
    ArrayList<SongObject> songObjectList;

    public PageAdapterBackground(Context ctx, ArrayList<SongObject> songObjectList) {

        this.ctx = ctx;
        this.songObjectList=songObjectList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_layout_background, collection, false); //a view group is a layout, a view is a child of a view group
        collection.addView(layout);

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
            return MyBitmaps.hashMap.get(String.valueOf(position));
        }
    }

    public void setViewPagerBackground_1(final ViewGroup layout, int position){
        if(null!=songObjectList.get(position).albumArtURI){
            File imageFile = new File(songObjectList.get(position).albumArtURI);
            if(imageFile.exists()){

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;

                Bitmap bm = BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI,options);
                if(null==bm){
                    Bitmap filler = MyBitmaps.getFillerAlbum();
                    BitmapDrawable drbl = new BitmapDrawable(filler);
                    layout.setBackground(drbl);
                }
                if(null!=bm){
                    ScaleCenterCrop scaleCenterCrop = new ScaleCenterCrop();
                    Bitmap background = scaleCenterCrop.scale(bm);
                    bm.recycle();
                    double scaleFactor = ((double)Dimensions.getWidth()) / ((double)Dimensions.getHeight());
                    double scale = 5; //Must be at least two pixels long (a shorter length will collapse width to zero)
                    Bitmap blurred = Bitmap.createScaledBitmap(background, (int) (scale * scaleFactor), (int) scale, false); //width/height
                    background.recycle();
                    BitmapDrawable drbl = new BitmapDrawable(blurred);
                    layout.setBackground(drbl);
                }
            }
        }
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

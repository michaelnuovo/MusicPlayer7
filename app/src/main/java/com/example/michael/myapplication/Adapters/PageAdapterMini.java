package com.example.michael.myapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.MyBitmaps;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 1/14/2016.
 */
public class PageAdapterMini extends PagerAdapter {

    Context ctx;
    ArrayList<SongObject> songObjectList;

    public PageAdapterMini(Context ctx, ArrayList<SongObject> songObjectList) {

        this.ctx = ctx;
        this.songObjectList=songObjectList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {


        LayoutInflater inflater = LayoutInflater.from(ctx);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_layout_mini, collection, false); //a view group is a layout, a view is a child of a view group
        collection.addView(layout);

        layout.setTag(position);
        Drawable drbl = getDrawable(layout, position);
        //layout.setBackground(drbl);
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
            MyBitmaps.hashMap.put(String.valueOf(position), drbl);
            MyBitmaps.trimHashList(MyBitmaps.hashMap, position);
            return drbl;
        } else {
            return (Drawable) MyBitmaps.hashMap.get(String.valueOf(position));
        }
    }

    public void setViewPagerBackground(final ViewGroup layout, final int position){

        ImageView iv = (ImageView) layout.findViewById(R.id.miniPagerImageView); //set the background to this

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
                    setViewDimensions(500, 500, layout);
                    //iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    BitmapDrawable drbl = new BitmapDrawable(filler);
                    iv.setBackground(drbl);
                }
                if(null!=bm){
                    //int[] heightWidthArray = getWidthAndHeightOfBitmapWithoutLoadingItIntoMemory(position); //height and width
                    setViewDimensions(bm.getHeight(), bm.getWidth(), layout);
                    //iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    BitmapDrawable drbl = new BitmapDrawable(bm);
                    iv.setBackground(drbl);
                }
            }
        }
    }

    public void setViewDimensions(int height, int width, ViewGroup layout){

        RelativeLayout container = (RelativeLayout) layout.findViewById(R.id.container);
        ImageView iv = (ImageView) layout.findViewById(R.id.miniPagerImageView);

        iv.getLayoutParams().height = height;
        iv.getLayoutParams().width = width;

        container.getLayoutParams().height = height;
        container.getLayoutParams().width = width;
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

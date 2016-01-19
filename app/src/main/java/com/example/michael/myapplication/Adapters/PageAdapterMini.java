package com.example.michael.myapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Tranformations.CircleTransform;
import com.example.michael.myapplication.Utilities.Dimensions;
import com.example.michael.myapplication.Utilities.ScaleCenterCrop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
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

        //setViewPagerBackgroundWithPicasso(layout, position);
        setViewPagerBackground(layout, position);

        return layout;

    }

    public void setViewPagerBackground(final ViewGroup layout, final int position){

        ImageView iv = (ImageView) layout.findViewById(R.id.miniPagerImageView); //set the background to this

        if(null!=songObjectList.get(position).albumArtURI){
            File imageFile = new File(songObjectList.get(position).albumArtURI);
            if(imageFile.exists()){
                Bitmap bm = BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI);
                if(null==bm){
                    Bitmap filler = ScaleCenterCrop.getFillerAlbum();
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

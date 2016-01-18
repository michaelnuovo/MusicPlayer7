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

import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.Dimensions;
import com.example.michael.myapplication.Utilities.ScaleCenterCrop;

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

        setViewPagerBackground(layout, position);

        return layout;

    }

    public void setViewPagerBackground(final ViewGroup layout, final int position){

        if(null!=songObjectList.get(position).albumArtURI){
            File imageFile = new File(songObjectList.get(position).albumArtURI);
            if(imageFile.exists()){
                RelativeLayout container = (RelativeLayout) layout.findViewById(R.id.container);
                ImageView iv = (ImageView) layout.findViewById(R.id.miniPagerImageView);
                Bitmap bm = BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI);
                if(null==bm){
                    Bitmap filler = ScaleCenterCrop.getFillerAlbum();
                    setViewPagerDimensions(iv, filler,container);
                    //iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    BitmapDrawable drbl = new BitmapDrawable(filler);
                    iv.setBackground(drbl);
                }
                if(null!=bm){
                    setViewPagerDimensions(iv, bm, container);
                    //iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    BitmapDrawable drbl = new BitmapDrawable(bm);
                    iv.setBackground(drbl);
                }
            }
        }
    }

    public void setViewPagerDimensions(ImageView iv, Bitmap bm, RelativeLayout container){

        double imageWidth = bm.getWidth();
        double imageHeight = bm.getHeight();
        double imageViewWidth = Dimensions.getWidth();
        double imageViewHeight = imageHeight * imageViewWidth / imageWidth;

        iv.getLayoutParams().height = (int) imageViewHeight;
        iv.getLayoutParams().width = (int) imageViewWidth;

        container.getLayoutParams().height = (int) imageViewHeight;
        container.getLayoutParams().width = (int) imageViewWidth;
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

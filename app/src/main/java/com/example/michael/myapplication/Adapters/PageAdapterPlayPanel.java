package com.example.michael.myapplication.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.R;

import java.util.ArrayList;

/**
 * Created by Michael on 1/14/2016.
 */
public class PageAdapterPlayPanel extends PagerAdapter {

    Context ctx;
    ArrayList<SongObject> songObjectList;

    public PageAdapterPlayPanel(Context ctx, ArrayList<SongObject> songObjectList) {

        this.ctx = ctx;
        this.songObjectList=songObjectList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {


        LayoutInflater inflater = LayoutInflater.from(ctx);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_layout_playpanel, collection, false); //a view group is a layout, a view is a child of a view group
        collection.addView(layout);

        setViewPagerBackground(layout, position);

        return layout;

    }

    public void setViewPagerBackground(final ViewGroup layout, final int position){

        Point size = new Point();
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getSize(size);
        int width = size.x;
        //int height = size.y;

        Bitmap bm = BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI);
        //Bitmap bm = ScaleCenterCrop.scaleCenterCrop(BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI), dpToPx(150), width); //h/w
        //Bitmap bmb = FastBlur.fastblur(bm, 1, 5);
        //Bitmap bmd = BitmapDarken.darkenBitMap(bm);
        final BitmapDrawable dw = new BitmapDrawable(bm);
        layout.setBackgroundDrawable(dw);

        /*
        new Thread() {
            public void run() {

                Bitmap bm = BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI);
                //Bitmap bm = ScaleCenterCrop.scaleCenterCrop(BitmapFactory.decodeFile(songObjectList.get(position).albumArtURI), dpToPx(150), width); //h/w
                //Bitmap bmb = FastBlur.fastblur(bm, 1, 5);
                //Bitmap bmd = BitmapDarken.darkenBitMap(bm);
                final BitmapDrawable dw = new BitmapDrawable(bm);

                ((Activity) ctx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //stuff that updates ui
                        layout.setBackgroundDrawable(dw);
                    }
                });

            }
        }.start();*/


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

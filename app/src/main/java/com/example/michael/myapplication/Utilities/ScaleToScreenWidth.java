package com.example.michael.myapplication.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Michael on 1/15/2016.
 */
public class ScaleToScreenWidth {

    public static Bitmap scale(Bitmap source){

        //Get screen dimensions
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        //Get scale factor
        double scaleFactor = (double) metrics.widthPixels/source.getWidth();

        //Do conversions
        double finalHeight = source.getWidth()*scaleFactor;
        double finalWidth = source.getHeight()*scaleFactor;
        int finalHeighInt = (int) finalHeight;
        int finalWidthInt = (int) finalWidth;

        //Log calculations
        Log.v("TAG", "metrics.widthPixels is "+String.valueOf(metrics.widthPixels));
        Log.v("TAG", "source.getWidth is " + String.valueOf(source.getWidth()));
        Log.v("TAG", "metrics.widthPixels/source.getWidth() is " + String.valueOf(metrics.widthPixels/source.getWidth()));
        Log.v("TAG", "scaleFactor is "+String.valueOf(scaleFactor));
        Log.v("TAG", "source.getHeight is "+String.valueOf(source.getHeight()));

        //Scale bitmap
        Bitmap finalBitmap = Bitmap.createScaledBitmap(source, finalHeighInt, finalWidthInt, false);

        //Return bitmap
        return finalBitmap;
    }
}

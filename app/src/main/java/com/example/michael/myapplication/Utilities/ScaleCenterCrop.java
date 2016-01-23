package com.example.michael.myapplication.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.R;

public class ScaleCenterCrop {

    /*
    public static Bitmap cropBitmap(Bitmap source, int newHeight, int newWidth) {

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }*/


    public static Bitmap TopCenterCropBitmapWithoutScaling(Bitmap source, int viewWidth, int viewHeight) {

        float xScale = (float) viewWidth / source.getWidth();
        float yScale = (float) viewHeight / source.getHeight();

        Bitmap dest;

        int cropHeight;
        int cropWidth;
        int shift;

        if(xScale < yScale){

            cropWidth = source.getWidth()*viewWidth/viewHeight;
            cropHeight = source.getHeight();
            shift = (int)((double)source.getWidth()/2 - (double)cropWidth/2); //use doubles to maintain precision for centering

            dest = Bitmap.createBitmap(
                    source,
                    0+shift,
                    0,
                    cropWidth, //shift is added by default
                    cropHeight
            ); //createBitmap(Bitmap source, int x, int y, int width, int height)

        } else {

            cropWidth = source.getWidth();
            cropHeight = (int)(source.getHeight() * (double)viewHeight / viewWidth);

            dest = Bitmap.createBitmap(
                    source,
                    0,
                    0,
                    cropWidth,
                    cropHeight
            );
        }

        return dest;
    }

    public static Bitmap scaleCenterCrop2(Bitmap source, int newHeight, int newWidth) { //Dimensions.getHeight(), Dimensions.getWidth()
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.min(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be

        //RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        //RectF targetRect = new RectF(0, 0, source.getWidth(), 150*Dimensions.getWidth()/source.getWidth()); //the frame we crop around
        RectF targetRect = new RectF(0, 0, source.getWidth(), source.getHeight()); // this is the frame the image scales into so we leave the dimensions alone


        Log.v("TAG","source.getWidth() is "+String.valueOf(source.getWidth()));
        Log.v("TAG","newWidth is "+String.valueOf(newWidth));
        Log.v("TAG", "targetRect.width() is " + String.valueOf(targetRect.width()));


        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.

        Bitmap dest;
        if(Math.min(xScale, yScale)==xScale){
            dest = Bitmap.createBitmap(source.getWidth()*newWidth/newHeight, source.getHeight(), source.getConfig()); //this is the frame that clips the image
        }else{
            //dest = Bitmap.createBitmap(source.getWidth(), source.getHeight()*newHeight/newWidth, source.getConfig()); //this is the frame that clips the image
            dest = Bitmap.createBitmap(source.getWidth(), source.getHeight()*newHeight/newWidth, source.getConfig()); //this is the frame that clips the image
        }


        Canvas canvas = new Canvas(dest);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(source, null, targetRect, null); //drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint)

        return dest;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) { //150, Dimensions.getWidth()); // h/w
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.min(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be

        //RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        //RectF targetRect = new RectF(0, 0, source.getWidth(), 150*Dimensions.getWidth()/source.getWidth()); //the frame we crop around
        RectF targetRect = new RectF(0, 0, newWidth, newHeight*Dimensions.getWidth()/source.getWidth()); // this is the frame the image scales into


        Log.v("TAG","source.getWidth() is "+String.valueOf(source.getWidth()));
        Log.v("TAG","newWidth is "+String.valueOf(newWidth));
        Log.v("TAG", "targetRect.width() is " + String.valueOf(targetRect.width()));


        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig()); //this is the frame that clips the image

        Canvas canvas = new Canvas(dest);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }
}
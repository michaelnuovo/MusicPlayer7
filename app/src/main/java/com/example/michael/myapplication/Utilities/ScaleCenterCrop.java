package com.example.michael.myapplication.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.R;

public class ScaleCenterCrop {

    static Bitmap dest;
    static Bitmap fillerAlbum;

    public void recycleBitmaps(){

        dest.recycle();
        dest=null;
    }

    static public Bitmap getFillerAlbum(){
        if(null==fillerAlbum){
            fillerAlbum = BitmapFactory.decodeResource(MainActivity.getAppContext().getResources(), R.drawable.filler_empty_black_album);
            return fillerAlbum;
        } else {
            return fillerAlbum;
        }
    }

    public Bitmap scale(Bitmap source) {


        int screenHeight = Dimensions.getHeight();
        int screenWidth = Dimensions.getWidth();

        double scaleFactorHeight = screenHeight/screenWidth;

        int newHeight=source.getHeight();

        int newWidth=(int)(source.getHeight()*scaleFactorHeight);


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

        //RectF targetRect = new RectF(left, top, scaledWidth, scaledHeight);          //CENTER CROP
        RectF targetRect = new RectF(0, 0, scaledWidth, scaledHeight);                 //TOP-DOWN CROP

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.

        dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }
}
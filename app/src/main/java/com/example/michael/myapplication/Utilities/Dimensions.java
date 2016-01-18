package com.example.michael.myapplication.Utilities;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.example.michael.myapplication.Activities.MainActivity;

/**
 * Created by Michael on 1/16/2016.
 */
public class Dimensions {

    static public int getHeight(){

        WindowManager wm = (WindowManager) MainActivity.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        return screenHeight;
    }

    static public int getWidth(){

        WindowManager wm = (WindowManager) MainActivity.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        return screenWidth;
    }
}

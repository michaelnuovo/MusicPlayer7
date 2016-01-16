package com.example.michael.myapplication.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.michael.myapplication.R;

//http://stackoverflow.com/questions/2991110/android-how-to-stretch-an-image-to-the-screen-width-while-maintaining-aspect-ra
public class FitBannerToScreen extends View {

    //private final Drawable logo;

    public FitBannerToScreen(Context context) {
        super(context);
        //logo = context.getResources().getDrawable(R.drawable.banner);
        //setBackgroundDrawable(logo);
    }

    public FitBannerToScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        //logo = context.getResources().getDrawable(R.drawable.banner);
        //setBackgroundDrawable(logo);
    }

    public FitBannerToScreen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //logo = context.getResources().getDrawable(R.drawable.banner);
        //setBackgroundDrawable(logo);
    }

    @Override protected void onMeasure(int widthMeasureSpec,
                                       int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //int height = width * logo.getIntrinsicHeight() / logo.getIntrinsicWidth();
        //setMeasuredDimension(width, height);
    }
}
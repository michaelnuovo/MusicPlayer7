package com.example.michael.myapplication.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Michael on 1/18/2016.
 */
public class MyBitmaps {

    static Bitmap fillerAlbum;
    static Bitmap bm;
    static ArrayList<Bitmap> albumArtArray_originals;
    static ArrayList<Bitmap> albumArtArray_scaledToBlur;

    static public HashMap<String,Drawable> hashMap = new HashMap<String,Drawable>();
    //map.put("titre", "Orange TN");

    static public Bitmap getFillerAlbum(){
        if(null==fillerAlbum){
            fillerAlbum = BitmapFactory.decodeResource(MainActivity.getAppContext().getResources(), R.drawable.filler_empty_black_album);
            return fillerAlbum;
        } else {
            return fillerAlbum;
        }
    }

    public static void createAlbumArtBitmapArrayList(ArrayList<AlbumObject> albumObjectList){
        for(int i=0;i<albumObjectList.size(); i++) {
            albumArtArray_originals.add(BitmapFactory.decodeFile(albumObjectList.get(i).albumArtURI));
            //albumArtArray_scaledToBlur.add(Bitmap.createScaledBitmap());
        }
    }

    public static void trimHashList(HashMap hashMap, int position){

        SortedSet<String> keys = new TreeSet<>(hashMap.keySet());

        for (String key : keys) {
            // ...
            if(key != String.valueOf(position-1) && key != String.valueOf(position) && key != String.valueOf(position+1)){
                //do nothing
            } else {
                Log.v("TAG", key + "deleted");
                hashMap.remove(String.valueOf(position));
            }
        }

        System.gc();
    }
}

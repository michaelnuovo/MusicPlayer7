package com.example.michael.myapplication.Utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Objects.AlbumObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This class will save a bitmap to disk as a jpg.
 * This class will also return the file path of the image with a getter method.
 */
public class SaveBitMapToDisk {

    String imagePath1;
    String imagePath2;
    String imagePath3;

    Bitmap croppedToScreen;
    Bitmap scaledToScreenWidth;

    public String getImagePath1(){
        return imagePath1;
    }

    public void SaveImage(final Bitmap source, String folderName, AlbumObject albumObject) {

        WindowManager wm = (WindowManager) MainActivity.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + folderName);
        myDir.mkdirs();

        String fileName1 = albumObject.albumTitle + "_source_"              + albumObject.albumId + ".jpg";
        String fileName2 = albumObject.albumTitle + "_croppedToScreen_"     + albumObject.albumId + ".jpg";
        String fileName3 = albumObject.albumTitle + "_scaledToScreenWidth_" + albumObject.albumId + ".jpg";

        //saveToInternalSorage(source, fileName1);
        //saveToInternalSorage(croppedToScreen, fileName2);
        //saveToInternalSorage(scaledToScreenWidth, fileName3);

        albumObject.albumArtURI = fileName1;
        albumObject.albumArtURI_Center_Cropped_To_Sreen = fileName2;
        albumObject.getAlbumArtURI_Scaled_To_Screen_Width = fileName3;

        imagePath1 = myDir + "/" + fileName1;
        imagePath2 = myDir + "/" + fileName2;
        imagePath3 = myDir + "/" + fileName3;

        File file1 = new File(myDir, fileName1);
        File file2 = new File(myDir, fileName2);
        File file3 = new File(myDir, fileName3);

        if (file1.exists()) file1.delete();
        if (file2.exists()) file2.delete();
        if (file3.exists()) file3.delete();

        //We need to save memory here when we use bitmaps
        saveToExternalStorage(source, file1);

        croppedToScreen = ScaleCenterCrop.scaleCenterCrop(source, screenHeight, screenWidth);
        saveToExternalStorage(croppedToScreen, file2);
        croppedToScreen.recycle();
        croppedToScreen=null;

        scaledToScreenWidth = ScaleToScreenWidth.scale(source);
        saveToExternalStorage(scaledToScreenWidth, file3);
        scaledToScreenWidth.recycle();
        scaledToScreenWidth=null;

    }

    private void saveToExternalStorage(Bitmap source, File file){
        try {

            FileOutputStream out1 = new FileOutputStream(file);
            source.compress(Bitmap.CompressFormat.JPEG, 90, out1); // Converts the bitmap into a JPEG (a compressed variant of the bitmap)

            out1.flush(); // flushes anything which is still buffered by the OutputStream. See why JAVA uses a "buffer" for output streams https://docs.oracle.com/javase/tutorial/essential/io/buffers.html
            out1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveToInternalSorage(Bitmap bitmapImage, String fileName){

        ContextWrapper cw = new ContextWrapper(MainActivity.getAppContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(Color.RED, 1);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // to lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000); // to darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }

    public void deleteByFileName(String folderName, String fileName){

        //Delete all files in directory if there were any (for development purposes)

        String root = Environment.getExternalStorageDirectory().toString(); // get external directory
        File myDir = new File(root + "/" + folderName); // get file path from external directory + folder name
        for(File f : myDir.listFiles()) {
            if(String.valueOf(f).equals(fileName)){
                f.delete();
            }
        }
    }
}

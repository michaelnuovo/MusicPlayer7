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
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Objects.AlbumObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class will save a bitmap to disk as a jpg.
 * This class will also return the file path of the image with a getter method.
 */
public class SaveBitMapToDisk {

    String imagePathSource;
    String imagePathBackground;
    String imagePathInfoPanel;

    public String getImagePathSource(){
        return imagePathSource;
    }

    public void SaveImage(Bitmap source, String folderName, AlbumObject albumObject) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + folderName);
        myDir.mkdirs();

        //Save source image to drive
        String fileName1 = albumObject.albumTitle + "_source_" + ".jpg";
        albumObject.albumArtURI = fileName1;
        imagePathSource = myDir + "/" + fileName1;
        File file1 = new File(myDir, fileName1);
        saveToExternalStorage(source, file1);

        //Save a version of the image that is cropped to match the aspect ratio of the screen
        String centerCropped = albumObject.albumTitle + "_source__centerCropped_" + ".jpg";
        Bitmap topCenterCropped = ScaleCenterCrop.TopCenterCropBitmapWithoutScaling(source, Dimensions.getWidth(), Dimensions.getHeight()); // h/w
        File centerCroppedFile = new File(myDir, centerCropped);
        saveToExternalStorage(topCenterCropped, centerCroppedFile);

        //Save a blurred version of the cropped image
        String fileName2 = albumObject.albumTitle + "_source__background_" + ".jpg";
        imagePathBackground = myDir + "/" + fileName2;
        File file2 = new File(myDir, fileName2);
        int scaleToABlurWidth = 20;
        int scaleToABlurHeight = (int)(topCenterCropped.getHeight()*((double)scaleToABlurWidth/topCenterCropped.getWidth())); //maintains original aspect ratio
        Bitmap topCenterCroppedBlurred = Bitmap.createScaledBitmap(topCenterCropped, scaleToABlurWidth, scaleToABlurHeight, false); //createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter)
        saveToExternalStorage(topCenterCroppedBlurred, file2);

        //Recycle
        topCenterCropped.recycle();
        topCenterCroppedBlurred.recycle();

        //Crop a save a version of the source file that fits to the information panel
        String fileName3 = albumObject.albumTitle + "_source__informationPanel_" + ".jpg";
        imagePathInfoPanel = myDir + "/" + fileName3;
        File file3 = new File(myDir, fileName3);
        Bitmap croppedToInfoPanel = ScaleCenterCrop.TopCenterCropBitmapWithoutScaling(source, source.getWidth(), 150); // h/w
        saveToExternalStorage(croppedToInfoPanel, file3);

        //Recycle
        croppedToInfoPanel.recycle();
        source.recycle();
    }

    private void saveToExternalStorage(Bitmap source, File file){

        if (file.exists()) file.delete();
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
}

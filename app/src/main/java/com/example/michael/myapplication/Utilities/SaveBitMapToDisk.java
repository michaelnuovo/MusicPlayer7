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

    String imagePath1;

    public String getImagePath1(){
        return imagePath1;
    }

    public void SaveImage(Bitmap source, String folderName, AlbumObject albumObject) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + folderName);
        myDir.mkdirs();
        String fileName1 = albumObject.albumTitle + "_source_" + albumObject.albumId + ".jpg";
        albumObject.albumArtURI = fileName1;
        imagePath1 = myDir + "/" + fileName1;
        File file1 = new File(myDir, fileName1);
        if (file1.exists()) file1.delete();

        Bitmap scaledBitMap = scaleToAcceptableSize(source); //scales proportionally to width of 500 pixels
        Bitmap convertedBitmap = convert(scaledBitMap, Bitmap.Config.RGB_565); //converts to RGB_565 to reduce memory size by a factor of 2 if the image is ARGB_8888; see http://stackoverflow.com/questions/6480182/how-can-i-specify-the-bitmap-format-e-g-rgba-8888-with-bitmapfactory-decode
        saveToExternalStorage(convertedBitmap, file1);

        scaledBitMap.recycle();
        convertedBitmap.recycle();
        System.gc();
    }


    private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
    }


    public Bitmap scaleToAcceptableSize(Bitmap source){

        //All bit maps are scaled before they are saved to disk
        int maxWidth = 400; //Must be at least two pixels long (a shorter length will collapse width to zero)
        double heightFactor = (double) maxWidth / (double) source.getWidth();
        int sourceHeight = source.getHeight();
        Bitmap finalBm = Bitmap.createScaledBitmap(source,maxWidth,(int)(sourceHeight*heightFactor),false); //width/height
        //printDimensions(source, heightFactor, maxWidth, sourceHeight);
        //source.recycle();
        return finalBm;
    }

    public void printDimensions(Bitmap source, double heightFactor, int maxWidth, int sourceHeight){

        Log.v("TAG","---------------------------");
        Log.v("TAG","source height :"+String.valueOf(source.getHeight()));
        Log.v("TAG","source width  :"+String.valueOf(source.getWidth()));
        Log.v("TAG","max width     :"+String.valueOf(maxWidth));
        Log.v("TAG","heightFactor  :"+String.valueOf(heightFactor));
        Log.v("TAG","---------------------------");
    }

    public void writeUrisToSongObjects(AlbumObject albumObject, String fileName1, String fileName2, String fileName3){

        for(int i=0;i<albumObject.songObjectList.size();i++){
            albumObject.songObjectList.get(i).albumArtURI = fileName1;
            albumObject.songObjectList.get(i).albumArtURICenterCroppedToScreen = fileName2;
            albumObject.songObjectList.get(i).albumArtURIScaledToScreenWidth = fileName3;
        }
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

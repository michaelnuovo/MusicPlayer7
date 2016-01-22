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

        //File 1
        String fileName1 = albumObject.albumTitle + "_source_" + ".jpg";
        albumObject.albumArtURI = fileName1;
        imagePathSource = myDir + "/" + fileName1;
        File file1 = new File(myDir, fileName1);
        if (file1.exists()) file1.delete();
        Bitmap scaledSourceMap = scaleToAcceptableSize(source); //scales proportionally to width of 500 pixels
        //Bitmap convertedBitmap = convert(scaledBitMap, Bitmap.Config.RGB_565); //converts to RGB_565 to reduce memory size by a factor of 2 if the image is ARGB_8888; see http://stackoverflow.com/questions/6480182/how-can-i-specify-the-bitmap-format-e-g-rgba-8888-with-bitmapfactory-decode
        saveToExternalStorage(scaledSourceMap, file1);

        //File center cropped
        String centerCropped = albumObject.albumTitle + "_source__centerCropped_" + ".jpg";
        Bitmap centerCroppedMap = ScaleCenterCrop.CenterCropBitmapWithoutScaling(scaledSourceMap, Dimensions.getWidth(), Dimensions.getHeight()); // h/w
        File centerCroppedFile = new File(myDir, centerCropped);
        if (centerCroppedFile.exists()) centerCroppedFile.delete();
        saveToExternalStorage(centerCroppedMap, centerCroppedFile);

        //File 2
        String fileName2 = albumObject.albumTitle + "_source__background_" + ".jpg";
        imagePathBackground = myDir + "/" + fileName2;
        File file2 = new File(myDir, fileName2);
        if (file2.exists()) file2.delete();
        //Bitmap centerCropped = CenterCrop.crop(scaledSourceMap);

        int scaleToABlurWidth = 20;
        int scaleToABlurHeight = (int)(centerCroppedMap.getHeight()*((double)scaleToABlurWidth/centerCroppedMap.getWidth())); //maintains original aspect ratio
        //Log.v("TAG","(double)(scaleToABlurWidth/scaledSourceMap.getWidth()) is "+String.valueOf((double)(scaleToABlurWidth/scaledSourceMap.getWidth())));
        //Log.v("TAG","scaledSourceMap.getWidth() is "+String.valueOf(scaledSourceMap.getWidth()));
        //Log.v("TAG","scaleToABlurWidth is "+String.valueOf(scaleToABlurWidth));
        //Log.v("TAG","scaledSourceMap.getHeight() is "+String.valueOf(scaledSourceMap.getHeight()));
        //Log.v("TAG","scaleToABlurHeight is "+String.valueOf(scaleToABlurHeight));
        Bitmap scaleToABlur = Bitmap.createScaledBitmap(centerCroppedMap, scaleToABlurWidth, scaleToABlurHeight, false); //createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter)
        saveToExternalStorage(scaleToABlur, file2);
        scaleToABlur.recycle();
        centerCroppedMap.recycle();

        //File 3
        String fileName3 = albumObject.albumTitle + "_source__informationPanel_" + ".jpg";
        imagePathInfoPanel = myDir + "/" + fileName3;
        File file3 = new File(myDir, fileName3);
        if (file3.exists()) file3.delete();
        Bitmap infoPanel = ScaleCenterCrop.CenterCropBitmapWithoutScaling(scaledSourceMap, scaledSourceMap.getWidth(), 150); // h/w
        saveToExternalStorage(infoPanel, file3);

        scaledSourceMap.recycle();

        //convertedBitmap.recycle();
        System.gc();
    }

    public Bitmap centerCrop(Bitmap source){

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

        Bitmap dest = Bitmap.createBitmap(Dimensions.getWidth(), Dimensions.getHeight(), source.getConfig()); //width, int height, http://developer.android.com/intl/zh-cn/reference/android/graphics/Bitmap.html
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    public Bitmap cropToInfoPanel(Bitmap source){

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
        RectF targetRect = new RectF(0, 0, source.getWidth(), 150*Dimensions.getWidth()/500);                 //TOP-DOWN CROP

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.



        Bitmap dest = Bitmap.createBitmap(source.getWidth(), 150*Dimensions.getWidth()/500, source.getConfig()); //width, int height, http://developer.android.com/intl/zh-cn/reference/android/graphics/Bitmap.html
        //Canvas canvas = new Canvas(dest);
        //canvas.drawBitmap(source, null, targetRect, null);

        return dest;
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

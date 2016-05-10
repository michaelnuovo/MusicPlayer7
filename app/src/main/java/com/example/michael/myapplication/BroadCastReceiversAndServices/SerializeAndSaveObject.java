package com.example.michael.myapplication.BroadCastReceiversAndServices;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.michael.myapplication.Activities.MainActivity;
import com.example.michael.myapplication.Objects.SongObject;
import com.example.michael.myapplication.Objects.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The two methods in this class save
 * and load objects to and from memory
 * as strings.
 *
 * This class is to be used
 * by the internal music player such that it can
 * save and recall play list objects.
 * Play list objects are array lists of SongObjects.
 *
 * The music player will save an int type index object
 * and also the play list object.
 *
 * The play list object will be saved in the main class.
 * The music engine will update the index.
 */
public class SerializeAndSaveObject {

    static public void saveObject(String key, Object value, Context ctx){

        try{
            Log.v("TAG","here");

            //Application application = new Application();

            // Get the file output stream
            //FileOutputStream fos = getApplicationContext().openFileOutput(key, Context.MODE_PRIVATE);
            //getAppContext()

            /**
             * I won't be able to call main activity when it's closed.
             * The app itself won't be closed if the background service is running
             * but main activity will be closed. I need another way of getting the context.
             */

            FileOutputStream fos = ctx.openFileOutput(key, Context.MODE_PRIVATE);

            // Line up the object output stream with the file output stream
            ObjectOutputStream oos= new ObjectOutputStream(fos);

            // Write the object with the output stream.
            // ObjectOutputStream is a specialized OutputStream that is able to write
            // (serialize) Java objects as well as primitive data types (int, byte, char etc.).
            // The data can later be loaded using an ObjectInputStream.
            oos.writeObject(value);

            oos.flush();
            oos.close();
            fos.flush();
            fos.close();

        }catch(IOException ioe){
            Log.e("TAG", "Stack trace is " + Log.getStackTraceString(ioe));
        }
    }

    static public Object loadObject(String key){

        try {

            Application application = new Application();
            FileInputStream fis = MainActivity.getAppContext().openFileInput(key);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object object = is.readObject();
            is.close();
            fis.close();
            return object;

        }catch(IOException ioe){
            Log.e("TAG","Stack trace is "+Log.getStackTraceString(ioe));
        } catch (ClassNotFoundException e) {
            Log.e("TAG", "Stack trace is "+Log.getStackTraceString(e));
        }

        return null;
    }
}

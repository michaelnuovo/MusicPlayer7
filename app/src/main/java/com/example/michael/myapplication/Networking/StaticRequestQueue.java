package com.example.michael.myapplication.Networking;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.michael.myapplication.Activities.MainActivity;

/**
 * Created by Michael on 1/15/2016.
 */
public class StaticRequestQueue {

    public static RequestQueue mRequestQueue;

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(MainActivity.getAppContext());
        }
        return mRequestQueue;
    }
}

package com.example.michael.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.myapplication.Tranformations.CircleTransform;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Objects.SongObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michael on 1/12/2016.
 */
public class FragmentListAdapterSongsList extends ArrayAdapter<SongObject> {

    int layoutResourceId;
    ArrayList<SongObject> songObjectList;
    Activity activity;
    
    public FragmentListAdapterSongsList(Context ctx, int layoutResourceId, ArrayList<SongObject> songObjectList) {

        super(ctx, layoutResourceId, songObjectList);
        this.layoutResourceId = layoutResourceId;
        this.songObjectList = songObjectList;
        this.activity = (Activity) ctx;

    }

    static class ViewHolder {

        static ImageView albumArt;
        static TextView artist;
        static TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SongObject songObject = songObjectList.get(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_songs_fragment, parent, false);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
        viewHolder.title = (TextView) convertView.findViewById(R.id.title);
        viewHolder.albumArt = (ImageView) convertView.findViewById(R.id.album_art);
        viewHolder.artist.setText(songObject.artist);
        viewHolder.title.setText(songObject.songTitle);

        setPicassoLibrary(songObject, viewHolder);

        return convertView;
    }

    public void setPicassoLibrary(SongObject songObject, ViewHolder viewHolder){

        if(songObject.albumArtURI != null){
            File f = new File(songObject.albumArtURI);
            Picasso.with(viewHolder.albumArt.getContext())
                    .load(f)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.blackcircle).fit().centerCrop()
                    .into(viewHolder.albumArt);
        } else {
            Picasso.with(viewHolder.albumArt.getContext())
                    .load(R.drawable.blackcircle)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.blackcircle)
                    .into(viewHolder.albumArt);
        }
    }
}


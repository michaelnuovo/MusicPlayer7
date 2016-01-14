package com.example.michael.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.Tranformations.CircleTransform;
import com.example.michael.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FragmentGridViewAdapterAlbumList extends ArrayAdapter<AlbumObject> {


    Context context;
    int layoutResourceId;
    ArrayList<AlbumObject> albumObjectList;

    public FragmentGridViewAdapterAlbumList(Context context, int layoutResourceId, ArrayList<AlbumObject> albumObjectList) {

        super(context, layoutResourceId, albumObjectList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.albumObjectList = albumObjectList;
    }

    static class ViewHolder {

        static ImageView albumArt;
        static TextView albumTitle;
        static TextView artist;
        static TextView title;
        //static TextView data;
        static TextView duration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        AlbumObject albumObject = albumObjectList.get(position);

        ViewHolder viewHolder; // view lookup cache stored in tag

        // if an existing view is not being reused
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.gridview_item_albums_fragment, parent, false);
            convertView.setTag(viewHolder);

            // if an existing view is being reused
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //viewHolder.albumArt = null;
        }

        // Set view holder references

        //viewHolder.album = (TextView) convertView.findViewById(R.id.album);
        //viewHolder.artist = (TextView) convertView.findViewById(R.id.gridViewAlbumTitle);
        viewHolder.albumTitle = (TextView) convertView.findViewById(R.id.gridViewAlbumTitle);
        viewHolder.albumArt = (ImageView) convertView.findViewById(R.id.gridViewAlbumArt);
        //viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);

        // Set values to referenced view objects

        viewHolder.albumTitle.setText(albumObject.albumTitle);
        //viewHolder.artist.setText(songObject.artist);
        //viewHolder.title.setText(songObject.title);
        //viewHolder.duration.setText(FormatTime(songObject.duration));

        // viewHolder.albumArt.setBackgroundResource(R.drawable.blackcircle);

        //viewHolder.albumArt.setImageBitmap(BitmapFactory.decodeFile(songObject.albumArtURI));

        // Set a temporary gray background to the image view

        //viewHolder.albumArt.setBackgroundResource(R.drawable.grayalbumart);

        // Load album art asynchronously for smoother scrolling experience

        //new ImageLoader(viewHolder.albumArt).execute(songObject.albumArtURI);

        /** SET ALBUM ART TO LIST ITEMS USING PICASSO IMAGE LIBRARY **/
        if(albumObject.albumArtURI != null){

            File f = new File(albumObject.songObjectList.get(0).albumArtURI);
            Picasso.with(viewHolder.albumArt.getContext())
                    .load(f)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.blackcircle).fit().centerCrop()
                    .into(viewHolder.albumArt);
        } else {

            Picasso.with(viewHolder.albumArt.getContext())
                    .load(R.drawable.blackcircle)
                            //.transform(new CircleTransform())
                    .placeholder(R.drawable.blackcircle)
                    .into(viewHolder.albumArt);
        }

        return convertView;
    }
}
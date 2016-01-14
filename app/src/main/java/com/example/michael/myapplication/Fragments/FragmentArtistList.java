package com.example.michael.myapplication.Fragments;

/**
 * Created by Michael on 1/12/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.michael.myapplication.Activities.PlayPanel;
import com.example.michael.myapplication.Activities.SingleArtist;
import com.example.michael.myapplication.Adapters.FragmentListAdapterArtistList;
import com.example.michael.myapplication.Adapters.UpdateAdapters;
import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.Objects.ArtistObject;
import com.example.michael.myapplication.R;
import com.example.michael.myapplication.Utilities.StaticMusicPlayer;

import java.util.ArrayList;

public class FragmentArtistList extends Fragment {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private ArrayList<ArtistObject> artistObjectList;
    static private ArtistObject artistSelectedFromListView;

    static public ArtistObject getSelectedArtist(){return artistSelectedFromListView;}

    public static final FragmentArtistList newInstance(ArrayList<ArtistObject> arrayList)
    {
        FragmentArtistList f = new FragmentArtistList();
        Bundle bdl = new Bundle(1);
        bdl.putParcelableArrayList(EXTRA_MESSAGE, arrayList);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        artistObjectList = getArguments().getParcelableArrayList(EXTRA_MESSAGE);

        View rootView = inflater.inflate(R.layout.fragment_artists, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.fragment_artists_listview);

        FragmentListAdapterArtistList adapter = new FragmentListAdapterArtistList(getActivity(), R.layout.list_item_artist_fragment, artistObjectList);
        listView.setAdapter(adapter);

        Log.v("TAG", "artist adapter value is " + String.valueOf(adapter));

        UpdateAdapters.getInstance().setAdapterTwo(adapter);

        setListItemClickListener(listView);

        return rootView;
    }

    public void setListItemClickListener(ListView listView){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent(getActivity(), SingleArtist.class);
                startActivity(intent);

                artistSelectedFromListView = artistObjectList.get(arg2);
            }
        });
    }
}

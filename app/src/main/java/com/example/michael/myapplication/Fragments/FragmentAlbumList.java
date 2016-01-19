package com.example.michael.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.michael.myapplication.Activities.SingleAlbum;
import com.example.michael.myapplication.Activities.SingleArtist;
import com.example.michael.myapplication.Adapters.FragmentGridViewAdapterAlbumList;
import com.example.michael.myapplication.Adapters.UpdateAdapters;
import com.example.michael.myapplication.Objects.AlbumObject;
import com.example.michael.myapplication.Objects.ArtistObject;
import com.example.michael.myapplication.R;

import java.util.ArrayList;

public class FragmentAlbumList extends Fragment {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private ArrayList<AlbumObject> albumObjectList;
    static public AlbumObject albumSelectedFromListView;

    static public AlbumObject getSelectedAlbum(){return albumSelectedFromListView;}

    public static final FragmentAlbumList newInstance(ArrayList<AlbumObject> arrayList)
    {
        FragmentAlbumList f = new FragmentAlbumList();
        Bundle bdl = new Bundle(1);
        bdl.putParcelableArrayList(EXTRA_MESSAGE, arrayList);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        albumObjectList = getArguments().getParcelableArrayList(EXTRA_MESSAGE);

        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.fragment_albums_gridview);

        FragmentGridViewAdapterAlbumList gridAdapter = new FragmentGridViewAdapterAlbumList(getActivity(), R.layout.gridview_item_albums_fragment, albumObjectList);
        gridView.setAdapter(gridAdapter);

        Log.v("TAG", "gridAdapter value is " + String.valueOf(gridAdapter));

        UpdateAdapters.getInstance().setAdapterThree(gridAdapter);

        setGridItemClickListener(gridView);

        return rootView;
    }

    private void setGridItemClickListener(GridView gridView){

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent(getActivity(), SingleAlbum.class);
                startActivity(intent);

                albumSelectedFromListView = albumObjectList.get(arg2);
            }
        });
    }
}

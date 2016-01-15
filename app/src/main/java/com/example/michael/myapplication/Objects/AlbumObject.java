package com.example.michael.myapplication.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AlbumObject implements Parcelable {

    public String albumTitle;
    public String albumArtist;
    public int albumTrackCount = 0;
    public int albumId;
    public String albumArtURI;
    public String albumArtURI_Center_Cropped_To_Sreen;
    public ArrayList<SongObject> songObjectList = new ArrayList<>();

    public int lastFmRequestErrorNumber = 0;
    public int spotifyRequestErrorNumber = 0;
    public int amazonRequestErrorNumber = 0;

    public AlbumObject(String albumTitle, String albumArtist, String albumArtURI, SongObject songObject){

        this.albumTitle = albumTitle;
        this.albumArtist = albumArtist;
        this.albumArtURI = albumArtURI;
        this.songObjectList.add(songObject);

    }
    public AlbumObject(SongObject songObject){

        this.albumTitle = songObject.albumTitle;
        this.albumArtist = songObject.artist;
        this.albumArtURI = songObject.albumArtURI;
        this.albumId = Integer.valueOf(songObject.albumID);
        this.songObjectList.add(songObject);

    }


    // Parcelable Code

    public AlbumObject(Parcel in) {
        //super(); // super() calls the parent constructor with no arguments.
        //readFromParcel(in);

        albumTitle = in.readString();
        albumArtist = in.readString();
        albumArtURI = in.readString();
        albumArtURI_Center_Cropped_To_Sreen = in.readString();
        albumTrackCount = in.readInt();
        amazonRequestErrorNumber = in.readInt();
        spotifyRequestErrorNumber = in.readInt();
        lastFmRequestErrorNumber = in.readInt();
        albumId=in.readInt();
        in.readTypedList(songObjectList, SongObject.CREATOR);
    }

    public static final Parcelable.Creator<AlbumObject> CREATOR = new Parcelable.Creator<AlbumObject>() {

        public AlbumObject createFromParcel(Parcel in) {

            return new AlbumObject(in);
        }

        public AlbumObject[] newArray(int size) {

            return new AlbumObject[size];
        }

    };

    public void readFromParcel(Parcel in) {
        albumArtURI = in.readString();
        albumArtURI_Center_Cropped_To_Sreen = in.readString();
        albumTitle = in.readString();
        albumArtist = in.readString();

        albumTrackCount = in.readInt();
        amazonRequestErrorNumber = in.readInt();
        spotifyRequestErrorNumber = in.readInt();
        lastFmRequestErrorNumber = in.readInt();
        albumId=in.readInt();
        in.readTypedList(songObjectList, SongObject.CREATOR);

    }
    public int describeContents() {

        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumArtURI);
        dest.writeString(albumArtURI_Center_Cropped_To_Sreen);
        dest.writeString(albumTitle);
        dest.writeString(albumArtist);

        dest.writeInt(albumTrackCount);
        dest.writeInt(amazonRequestErrorNumber);
        dest.writeInt(spotifyRequestErrorNumber);
        dest.writeInt(lastFmRequestErrorNumber);
        dest.writeInt(albumId);
        dest.writeTypedList(songObjectList);

    }
}

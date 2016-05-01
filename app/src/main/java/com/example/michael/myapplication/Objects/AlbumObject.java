package com.example.michael.myapplication.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * I have a class in my Android app that I've made Parcelable so that it can be
 * passed between Activities.I would like to be able to save this object to the filesystem.
 * It seems that since I've already implemented Parcelable, it would make sense to pipe the output
 * of this to the filesystem and read it back later. Is there a correct way to do this?
 * Or must I implement both Parcelable and Serialiazble if I want to both pass the object between
 * Activities and also save it to the filesystem?
 *
 * Parcel is not a general-purpose serialization mechanism.
 * This class (and the corresponding Parcelable API for placing arbitrary objects into a Parcel)
 * is designed as a high-performance IPC transport. As such, it is not appropriate to place any
 * Parcel data in to persistent storage: changes in the underlying implementation of any of the
 * data in the Parcel can render older data unreadable.
 *
 * http://stackoverflow.com/questions/7356035/persisting-a-parcelable-object-in-android
 *
 * ArrayList is serializable by default. This means you need not to implement Serializable
 * interface explicitly in order to serialize an ArrayList.
 * In this tutorial we will learn how to serialize and de-serialize an ArrayList.
 *
 * note: I only need to serailize the object in the list?
 *
 * http://beginnersbook.com/2013/12/how-to-serialize-arraylist-in-java/
 *
 *
 */

public class AlbumObject implements Parcelable {

    public String albumTitle;
    public String albumArtist;
    public int albumTrackCount = 0;
    public int albumId;
    public String albumArtURI;
    public String albumArtURICenterCroppedToScreen;
    public String albumArtURIScaledToScreenWidth;
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
        albumArtURICenterCroppedToScreen = in.readString();
        albumArtURIScaledToScreenWidth = in.readString();
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
        albumArtURICenterCroppedToScreen = in.readString();
        albumArtURIScaledToScreenWidth = in.readString();
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
        dest.writeString(albumArtURICenterCroppedToScreen);
        dest.writeString(albumArtURIScaledToScreenWidth);
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

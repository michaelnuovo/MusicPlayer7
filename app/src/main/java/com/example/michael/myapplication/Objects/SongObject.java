package com.example.michael.myapplication.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

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


public class SongObject implements Parcelable, java.io.Serializable {

    // My Code

    public String albumArtURI;
    public String albumArtURICenterCroppedToScreen;
    public String albumArtURIScaledToScreenWidth;

    public String albumArtURI_InfoPanel;
    public String albumTitle;
    public String artist;
    public String songTitle;
    public String songPath;
    public String songDuration;
    public String albumURL;
    public String albumID;
    //public String[] albumID;

    public SongObject copy(SongObject so){

        so.albumArtURI = albumArtURI;
        so.albumArtURI_InfoPanel = albumArtURI_InfoPanel;
        so.albumArtURICenterCroppedToScreen = albumArtURICenterCroppedToScreen;
        so.albumArtURIScaledToScreenWidth = albumArtURIScaledToScreenWidth;
        so.albumTitle=albumTitle;
        so.artist=artist;
        so.songTitle=songTitle;
        so.songPath=songPath;
        so.songDuration=songDuration;
        so.albumURL=albumURL;
        so.albumID=albumID;

        return so;
    }

    public SongObject(){
        super();
    }

    // Parcelable Code


    public SongObject(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<SongObject> CREATOR = new Parcelable.Creator<SongObject>() {
        public SongObject createFromParcel(Parcel in) {
            return new SongObject(in);
        }

        public SongObject[] newArray(int size) {

            return new SongObject[size];
        }

    };

    public void readFromParcel(Parcel in) {
        albumArtURI = in.readString();
        albumArtURI_InfoPanel = in.readString();
        albumArtURICenterCroppedToScreen = in.readString();
        albumArtURIScaledToScreenWidth = in.readString();
        albumTitle = in.readString();
        artist = in.readString();
        songTitle = in.readString();
        songPath = in.readString();
        songDuration = in.readString();
        albumURL = in.readString();
        albumID = in.readString();
        //albumID = in.createTypedArray(); // A newly created array containing objects with the same data as those that were previously written.
        // http://developer.android.com/reference/android/os/Parcel.html#createTypedArray(android.os.Parcelable.Creator<T>)

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumArtURI);
        dest.writeString(albumArtURI_InfoPanel);
        dest.writeString(albumArtURICenterCroppedToScreen);
        dest.writeString(albumArtURIScaledToScreenWidth);
        dest.writeString(albumTitle);
        dest.writeString(artist);
        dest.writeString(songTitle);
        dest.writeString(songPath);
        dest.writeString(songDuration);
        dest.writeString(albumURL);
        dest.writeString(albumID);
        //dest.writeTypedArray(albumID);
    }
}

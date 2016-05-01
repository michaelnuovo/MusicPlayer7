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

public class ArtistObject implements Parcelable {

    public String artistName;

    public int albumCount;
    public int artistTrackCount = 0;

    public ArrayList<SongObject> songObjectList = new ArrayList<>();

    public ArtistObject(SongObject songObject){

        this.artistName = songObject.artist;
        this.songObjectList.add(songObject);

    }

    // Parcelable Code

    public ArtistObject(Parcel in) {

        artistName = in.readString();
        albumCount = in.readInt();
        artistTrackCount = in.readInt();
        in.readTypedList(songObjectList, SongObject.CREATOR);
    }

    public static final Parcelable.Creator<ArtistObject> CREATOR = new Parcelable.Creator<ArtistObject>() {

        public ArtistObject createFromParcel(Parcel in) {

            return new ArtistObject(in);
        }

        public ArtistObject[] newArray(int size) {

            return new ArtistObject[size];
        }

    };

    public void readFromParcel(Parcel in) {

        artistName = in.readString();
        albumCount = in.readInt();
        artistTrackCount = in.readInt();
        in.readTypedList(songObjectList, SongObject.CREATOR);

    }
    public int describeContents() {

        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(artistName);
        dest.writeInt(albumCount);
        dest.writeInt(artistTrackCount);
        dest.writeTypedList(songObjectList);

    }

}

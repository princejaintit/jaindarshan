package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by admin on 9/8/2017.
 */
@IgnoreExtraProperties
public class AudioMainModel implements Parcelable{

    public String title;
    public ArrayList<AudioModel> audioModelArrayList;
    public boolean hasMore;

    protected AudioMainModel(Parcel in) {
        title = in.readString();
        audioModelArrayList = in.createTypedArrayList(AudioModel.CREATOR);
        hasMore = in.readByte() != 0;
    }

    public static final Creator<AudioMainModel> CREATOR = new Creator<AudioMainModel>() {
        @Override
        public AudioMainModel createFromParcel(Parcel in) {
            return new AudioMainModel(in);
        }

        @Override
        public AudioMainModel[] newArray(int size) {
            return new AudioMainModel[size];
        }
    };

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }



    public AudioMainModel() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<AudioModel> getAudioModelArrayList() {
        return audioModelArrayList;
    }

    public void setAudioModelArrayList(ArrayList<AudioModel> audioModelArrayList) {
        this.audioModelArrayList = audioModelArrayList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeTypedList(audioModelArrayList);
        parcel.writeByte((byte) (hasMore ? 1 : 0));
    }
}

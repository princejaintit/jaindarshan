package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 9/7/2017.
 */

public class PoemModel implements Parcelable {
    private String title;
    private String content;

    public PoemModel() {
    }

    protected PoemModel(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<PoemModel> CREATOR = new Creator<PoemModel>() {
        @Override
        public PoemModel createFromParcel(Parcel in) {
            return new PoemModel(in);
        }

        @Override
        public PoemModel[] newArray(int size) {
            return new PoemModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
    }
}

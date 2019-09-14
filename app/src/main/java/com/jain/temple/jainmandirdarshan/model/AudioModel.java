package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 9/6/2017.
 */

public class AudioModel implements Parcelable{
    private String title_h;
    private String title_e;
    private String detail;
    private String url_refrence;
    private String category;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public AudioModel(Parcel in) {
        title_h = in.readString();
        title_e = in.readString();
        detail = in.readString();
        url_refrence = in.readString();
        category = in.readString();
    }

    public static final Creator<AudioModel> CREATOR = new Creator<AudioModel>() {
        @Override
        public AudioModel createFromParcel(Parcel in) {
            return new AudioModel(in);
        }

        @Override
        public AudioModel[] newArray(int size) {
            return new AudioModel[size];
        }
    };

    public AudioModel() {

    }

    public String getTitle_h() {
        return title_h;
    }

    public void setTitle_h(String title_h) {
        this.title_h = title_h;
    }

    public String getTitle_e() {
        return title_e;
    }

    public void setTitle_e(String title_e) {
        this.title_e = title_e;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUrl_refrence() {
        return url_refrence;
    }

    public void setUrl_refrence(String url_refrence) {
        this.url_refrence = url_refrence;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title_h);
        parcel.writeString(title_e);
        parcel.writeString(detail);
        parcel.writeString(url_refrence);
        parcel.writeString(category);
    }
}

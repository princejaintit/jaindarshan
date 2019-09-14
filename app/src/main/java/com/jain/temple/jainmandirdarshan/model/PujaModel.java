package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by admin on 8/14/2017.
 */

public class PujaModel implements Parcelable,Comparable {
    private String name;
    private String name_hindi;
    private String detail;
    private int id;

    public PujaModel() {
    }

    protected PujaModel(Parcel in) {
        name = in.readString();
        name_hindi = in.readString();
        detail = in.readString();
        id = in.readInt();
    }

    public static final Creator<PujaModel> CREATOR = new Creator<PujaModel>() {
        @Override
        public PujaModel createFromParcel(Parcel in) {
            return new PujaModel(in);
        }

        @Override
        public PujaModel[] newArray(int size) {
            return new PujaModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName_hindi() {
        return name_hindi;
    }

    public void setName_hindi(String name_hindi) {
        this.name_hindi = name_hindi;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(name_hindi);
        dest.writeString(detail);
        dest.writeInt(id);
    }

    @Override
    public int compareTo(@NonNull Object comparestu) {
        int compareage=((StrotraModel)comparestu).getId();
        /* For Ascending order*/
        return this.id-compareage;
    }


}

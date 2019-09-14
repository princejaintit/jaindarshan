package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by admin on 7/28/2017.
 */

public class StrotraModel implements Parcelable,Comparable,Comparator {
    private String name;
    private String name_hindi;
    private String detail;
    private int id;
    private String category;
    private boolean isFavourite;

    public StrotraModel() {
    }

    protected StrotraModel(Parcel in) {
        name = in.readString();
        name_hindi = in.readString();
        detail = in.readString();
        id = in.readInt();
        category = in.readString();
        isFavourite = in.readByte() != 0;
    }

    public static final Creator<StrotraModel> CREATOR = new Creator<StrotraModel>() {
        @Override
        public StrotraModel createFromParcel(Parcel in) {
            return new StrotraModel(in);
        }

        @Override
        public StrotraModel[] newArray(int size) {
            return new StrotraModel[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }


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
    public int compareTo(@NonNull Object comparestu) {
        int compareage=((StrotraModel)comparestu).getId();
        /* For Ascending order*/
        return this.id-compareage;
    }

    @Override
    public int compare(Object o1, Object o2) {
        StrotraModel p1 = (StrotraModel) o1;
        StrotraModel p2 = (StrotraModel) o2;
        return p1.getName().compareToIgnoreCase(p2.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(name_hindi);
        parcel.writeString(detail);
        parcel.writeInt(id);
        parcel.writeString(category);
        parcel.writeByte((byte) (isFavourite ? 1 : 0));
    }
}

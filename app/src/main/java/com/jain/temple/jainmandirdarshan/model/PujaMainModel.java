package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by admin on 8/3/2017.
 */

public class PujaMainModel implements Parcelable, Comparable,Comparator {
    private String name;
    private String name_hindi;
    private int id;
    private ArrayList<StrotraModel> modelArrayList;

    public PujaMainModel() {
    }

    protected PujaMainModel(Parcel in) {
        name = in.readString();
        name_hindi = in.readString();
        id = in.readInt();
        modelArrayList = in.createTypedArrayList(StrotraModel.CREATOR);
    }

    public static final Creator<PujaMainModel> CREATOR = new Creator<PujaMainModel>() {
        @Override
        public PujaMainModel createFromParcel(Parcel in) {
            return new PujaMainModel(in);
        }

        @Override
        public PujaMainModel[] newArray(int size) {
            return new PujaMainModel[size];
        }
    };

    public ArrayList<StrotraModel> getModelArrayList() {
        return modelArrayList;
    }

    public void setModelArrayList(ArrayList<StrotraModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(name_hindi);
        dest.writeInt(id);
        dest.writeTypedList(modelArrayList);
    }
}

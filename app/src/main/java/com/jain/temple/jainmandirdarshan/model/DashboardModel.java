package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.PriorityQueue;

/**
 * Created by admin on 7/21/2017.
 */

public class DashboardModel implements Parcelable{
    private String name;
    private int icon;
    private String color;

    public DashboardModel() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    protected DashboardModel(Parcel in) {
        name = in.readString();
        icon = in.readInt();
        color = in.readString();
    }

    public static final Creator<DashboardModel> CREATOR = new Creator<DashboardModel>() {
        @Override
        public DashboardModel createFromParcel(Parcel in) {
            return new DashboardModel(in);
        }

        @Override
        public DashboardModel[] newArray(int size) {
            return new DashboardModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(icon);
        dest.writeString(color);
    }
}

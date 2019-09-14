package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NavDrawerItem implements Parcelable{
    private String newOnVersion;
    private String title;
    int icon;

    protected NavDrawerItem(Parcel in) {
        newOnVersion = in.readString();
        title = in.readString();
        icon = in.readInt();
    }

    public static final Creator<NavDrawerItem> CREATOR = new Creator<NavDrawerItem>() {
        @Override
        public NavDrawerItem createFromParcel(Parcel in) {
            return new NavDrawerItem(in);
        }

        @Override
        public NavDrawerItem[] newArray(int size) {
            return new NavDrawerItem[size];
        }
    };

    public String getNewOnVersion() {
        return newOnVersion;
    }

    public void setNewOnVersion(String newOnVersion) {
        this.newOnVersion = newOnVersion;
    }


    public NavDrawerItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(newOnVersion);
        parcel.writeString(title);
        parcel.writeInt(icon);
    }
}

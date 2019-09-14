package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 8/4/2017.
 */

public class DayDetail implements Parcelable {
    private String dayName;
    private String dayDetail;

    public DayDetail() {
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayDetail() {
        return dayDetail;
    }

    public void setDayDetail(String dayDetail) {
        this.dayDetail = dayDetail;
    }



    protected DayDetail(Parcel in) {
        dayName = in.readString();
        dayDetail = in.readString();
    }

    public static final Creator<DayDetail> CREATOR = new Creator<DayDetail>() {
        @Override
        public DayDetail createFromParcel(Parcel in) {
            return new DayDetail(in);
        }

        @Override
        public DayDetail[] newArray(int size) {
            return new DayDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dayName);
        dest.writeString(dayDetail);
    }
}

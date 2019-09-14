package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

public class CalendarMonthModel implements Parcelable{
    private String monthNameEnglish;
    private String monthHindi;
    private String monthNameHildiStart;
    private String monthNameHildiEnd;
    private String veer_samvat;
    private String vikram_samvat;
    private ArrayList<CalendarDayModel> dayModelArrayList;

    public CalendarMonthModel() {
    }

    public String getMonthHindi() {
        return monthHindi;
    }

    public void setMonthHindi(String monthHindi) {
        this.monthHindi = monthHindi;
    }



    protected CalendarMonthModel(Parcel in) {
        monthNameEnglish = in.readString();
        monthNameHildiStart = in.readString();
        monthNameHildiEnd = in.readString();
        veer_samvat = in.readString();
        vikram_samvat = in.readString();
        monthHindi = in.readString();
        dayModelArrayList = in.createTypedArrayList(CalendarDayModel.CREATOR);
    }

    public static final Creator<CalendarMonthModel> CREATOR = new Creator<CalendarMonthModel>() {
        @Override
        public CalendarMonthModel createFromParcel(Parcel in) {
            return new CalendarMonthModel(in);
        }

        @Override
        public CalendarMonthModel[] newArray(int size) {
            return new CalendarMonthModel[size];
        }
    };

    public ArrayList<CalendarDayModel> getDayModelArrayList() {
        return dayModelArrayList;
    }

    public void setDayModelArrayList(ArrayList<CalendarDayModel> dayModelArrayList) {
        this.dayModelArrayList = dayModelArrayList;
    }


    public String getMonthNameEnglish() {
        return monthNameEnglish;
    }

    public void setMonthNameEnglish(String monthNameEnglish) {
        this.monthNameEnglish = monthNameEnglish;
    }

    public String getMonthNameHildiStart() {
        return monthNameHildiStart;
    }

    public void setMonthNameHildiStart(String monthNameHildiStart) {
        this.monthNameHildiStart = monthNameHildiStart;
    }

    public String getMonthNameHildiEnd() {
        return monthNameHildiEnd;
    }

    public void setMonthNameHildiEnd(String monthNameHildiEnd) {
        this.monthNameHildiEnd = monthNameHildiEnd;
    }

    public String getVeer_samvat() {
        return veer_samvat;
    }

    public void setVeer_samvat(String veer_samvat) {
        this.veer_samvat = veer_samvat;
    }

    public String getVikram_samvat() {
        return vikram_samvat;
    }

    public void setVikram_samvat(String vikram_samvat) {
        this.vikram_samvat = vikram_samvat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(monthNameEnglish);
        dest.writeString(monthNameHildiStart);
        dest.writeString(monthNameHildiEnd);
        dest.writeString(veer_samvat);
        dest.writeString(vikram_samvat);
        dest.writeString(monthHindi);
        dest.writeTypedList(dayModelArrayList);
    }
}

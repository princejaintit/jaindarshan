package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 7/31/2017.
 */

public class CalendarDayModel implements Parcelable{
    private String day_d;
    private String Detail;
    private String mahina;
    private String mahina_h;
    private String month_d;
    private String paksh;
    private String paksh_h;
    private String sangyak;
    private String special;
    private String tithi_e;
    private String tithi_eh;
    private String veer_samvat;
    private String vikram_samvat;
    private boolean isTodayDate;

    public CalendarDayModel() {
    }

    protected CalendarDayModel(Parcel in) {
        day_d = in.readString();
        Detail = in.readString();
        mahina = in.readString();
        mahina_h = in.readString();
        month_d = in.readString();
        paksh = in.readString();
        paksh_h = in.readString();
        sangyak = in.readString();
        special = in.readString();
        tithi_e = in.readString();
        tithi_eh = in.readString();
        veer_samvat = in.readString();
        vikram_samvat = in.readString();
        isTodayDate = in.readByte() != 0;
    }

    public static final Creator<CalendarDayModel> CREATOR = new Creator<CalendarDayModel>() {
        @Override
        public CalendarDayModel createFromParcel(Parcel in) {
            return new CalendarDayModel(in);
        }

        @Override
        public CalendarDayModel[] newArray(int size) {
            return new CalendarDayModel[size];
        }
    };

    public boolean isTodayDate() {
        return isTodayDate;
    }

    public void setTodayDate(boolean todayDate) {
        isTodayDate = todayDate;
    }

    public String getDay_d() {
        return day_d;
    }

    public void setDay_d(String day_d) {
        this.day_d = day_d;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getMahina() {
        return mahina;
    }

    public void setMahina(String mahina) {
        this.mahina = mahina;
    }

    public String getMahina_h() {
        return mahina_h;
    }

    public void setMahina_h(String mahina_h) {
        this.mahina_h = mahina_h;
    }

    public String getMonth_d() {
        return month_d;
    }

    public void setMonth_d(String month_d) {
        this.month_d = month_d;
    }

    public String getPaksh() {
        return paksh;
    }

    public void setPaksh(String paksh) {
        this.paksh = paksh;
    }

    public String getPaksh_h() {
        return paksh_h;
    }

    public void setPaksh_h(String paksh_h) {
        this.paksh_h = paksh_h;
    }

    public String getSangyak() {
        return sangyak;
    }

    public void setSangyak(String sangyak) {
        this.sangyak = sangyak;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getTithi_e() {
        return tithi_e;
    }

    public void setTithi_e(String tithi_e) {
        this.tithi_e = tithi_e;
    }

    public String getTithi_eh() {
        return tithi_eh;
    }

    public void setTithi_eh(String tithi_eh) {
        this.tithi_eh = tithi_eh;
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
        dest.writeString(day_d);
        dest.writeString(Detail);
        dest.writeString(mahina);
        dest.writeString(mahina_h);
        dest.writeString(month_d);
        dest.writeString(paksh);
        dest.writeString(paksh_h);
        dest.writeString(sangyak);
        dest.writeString(special);
        dest.writeString(tithi_e);
        dest.writeString(tithi_eh);
        dest.writeString(veer_samvat);
        dest.writeString(vikram_samvat);
        dest.writeByte((byte) (isTodayDate ? 1 : 0));
    }
}

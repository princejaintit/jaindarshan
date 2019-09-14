package com.jain.temple.jainmandirdarshan.roomORM.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "myFavourateTemple")
public class MyFavourateTempleEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id ;

    @ColumnInfo(name = "category")
    private String CATEGORY ;

    @ColumnInfo(name = "content_category")
    private String CONTENT_CATEGORY;

    @ColumnInfo(name = "title")
    private String TITLE ;

    @ColumnInfo(name = "descp")
    private String DESCRIPTION;

    @ColumnInfo(name = "address")
    private String ADDRESS ;

    @ColumnInfo(name = "url_refrence")
    private String URL_REFRENCE;

    @ColumnInfo(name = "contact_no")
    private String CONTACT_NO ;

    @ColumnInfo(name = "rating")
    private String RATING;

    @ColumnInfo(name = "latitude")
    private String LATITUDE ;

    @ColumnInfo(name = "longitude")
    private String LONGITUDE;

    @ColumnInfo(name = "placeId")
    private String PLACE_ID;

    @ColumnInfo(name = "name")
    private String NAME ;

    @ColumnInfo(name = "pic_url")
    private String T_PIC;

    public MyFavourateTempleEntity(Parcel in) {
        id = in.readInt();
        CATEGORY = in.readString();
        CONTENT_CATEGORY = in.readString();
        TITLE = in.readString();
        DESCRIPTION = in.readString();
        ADDRESS = in.readString();
        URL_REFRENCE = in.readString();
        CONTACT_NO = in.readString();
        RATING = in.readString();
        LATITUDE = in.readString();
        LONGITUDE = in.readString();
        PLACE_ID = in.readString();
        NAME = in.readString();
        T_PIC = in.readString();
    }

    public static final Creator<MyFavourateTempleEntity> CREATOR = new Creator<MyFavourateTempleEntity>() {
        @Override
        public MyFavourateTempleEntity createFromParcel(Parcel in) {
            return new MyFavourateTempleEntity(in);
        }

        @Override
        public MyFavourateTempleEntity[] newArray(int size) {
            return new MyFavourateTempleEntity[size];
        }
    };

    public MyFavourateTempleEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getCONTENT_CATEGORY() {
        return CONTENT_CATEGORY;
    }

    public void setCONTENT_CATEGORY(String CONTENT_CATEGORY) {
        this.CONTENT_CATEGORY = CONTENT_CATEGORY;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getURL_REFRENCE() {
        return URL_REFRENCE;
    }

    public void setURL_REFRENCE(String URL_REFRENCE) {
        this.URL_REFRENCE = URL_REFRENCE;
    }

    public String getCONTACT_NO() {
        return CONTACT_NO;
    }

    public void setCONTACT_NO(String CONTACT_NO) {
        this.CONTACT_NO = CONTACT_NO;
    }

    public String getRATING() {
        return RATING;
    }

    public void setRATING(String RATING) {
        this.RATING = RATING;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getPLACE_ID() {
        return PLACE_ID;
    }

    public void setPLACE_ID(String PLACE_ID) {
        this.PLACE_ID = PLACE_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getT_PIC() {
        return T_PIC;
    }

    public void setT_PIC(String t_PIC) {
        T_PIC = t_PIC;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(CATEGORY);
        parcel.writeString(CONTENT_CATEGORY);
        parcel.writeString(TITLE);
        parcel.writeString(DESCRIPTION);
        parcel.writeString(ADDRESS);
        parcel.writeString(URL_REFRENCE);
        parcel.writeString(CONTACT_NO);
        parcel.writeString(RATING);
        parcel.writeString(LATITUDE);
        parcel.writeString(LONGITUDE);
        parcel.writeString(PLACE_ID);
        parcel.writeString(NAME);
        parcel.writeString(T_PIC);
    }
}

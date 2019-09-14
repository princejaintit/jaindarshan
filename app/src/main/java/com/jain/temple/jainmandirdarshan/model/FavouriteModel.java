package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by admin on 7/24/2017.
 */

public class FavouriteModel implements Parcelable{
    private String placeId;
    private String templeName;
    private String templeAddress;
    private String templeContactNo;
    private String templeRating;
    private LatLng latLng;
    private String picRefrenceUrl;
    private String category;
    private String title;
    private String content_category;

    protected FavouriteModel(Parcel in) {
        placeId = in.readString();
        templeName = in.readString();
        templeAddress = in.readString();
        templeContactNo = in.readString();
        templeRating = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        picRefrenceUrl = in.readString();
        category = in.readString();
        title = in.readString();
        content_category = in.readString();
        description = in.readString();
    }

    public static final Creator<FavouriteModel> CREATOR = new Creator<FavouriteModel>() {
        @Override
        public FavouriteModel createFromParcel(Parcel in) {
            return new FavouriteModel(in);
        }

        @Override
        public FavouriteModel[] newArray(int size) {
            return new FavouriteModel[size];
        }
    };

    public String getContent_category() {
        return content_category;
    }

    public void setContent_category(String content_category) {
        this.content_category = content_category;
    }


    public void setPicRefrenceUrl(String picRefrenceUrl) {
        this.picRefrenceUrl = picRefrenceUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
  //  private ArrayList<String> picUrlList;

    public FavouriteModel() {
    }


    public String getPicRefrenceUrl() {
        return picRefrenceUrl;
    }

    public void setPicRefrence(String picRefrenceUrl) {
        this.picRefrenceUrl = picRefrenceUrl;
    }


    /* public ArrayList<String> getPicUrlList() {
        return picUrlList;
    }

    public void setPicUrlList(ArrayList<String> picUrlList) {
        this.picUrlList = picUrlList;
    }*/

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getTempleName() {
        return templeName;
    }

    public void setTempleName(String templeName) {
        this.templeName = templeName;
    }

    public String getTempleAddress() {
        return templeAddress;
    }

    public void setTempleAddress(String templeAddress) {
        this.templeAddress = templeAddress;
    }

    public String getTempleContactNo() {
        return templeContactNo;
    }

    public void setTempleContactNo(String templeContactNo) {
        this.templeContactNo = templeContactNo;
    }

    public String getTempleRating() {
        return templeRating;
    }

    public void setTempleRating(String templeRating) {
        this.templeRating = templeRating;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeId);
        parcel.writeString(templeName);
        parcel.writeString(templeAddress);
        parcel.writeString(templeContactNo);
        parcel.writeString(templeRating);
        parcel.writeParcelable(latLng, i);
        parcel.writeString(picRefrenceUrl);
        parcel.writeString(category);
        parcel.writeString(title);
        parcel.writeString(content_category);
        parcel.writeString(description);
    }
}

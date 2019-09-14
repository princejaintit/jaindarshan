package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by admin on 7/4/2017.
 */

public class NearPlaceLatLngModel implements Parcelable{
    private String title;
    private LatLng latLng;
    private String templeName;
    private String templeAddress;
    private String photoRefrence;
    private String placeId;


    public NearPlaceLatLngModel() {
    }




    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }



    protected NearPlaceLatLngModel(Parcel in) {
        title = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        templeName = in.readString();
        templeAddress = in.readString();
        photoRefrence = in.readString();
        placeId = in.readString();

    }

    public static final Creator<NearPlaceLatLngModel> CREATOR = new Creator<NearPlaceLatLngModel>() {
        @Override
        public NearPlaceLatLngModel createFromParcel(Parcel in) {
            return new NearPlaceLatLngModel(in);
        }

        @Override
        public NearPlaceLatLngModel[] newArray(int size) {
            return new NearPlaceLatLngModel[size];
        }
    };

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

    public String getPhotoRefrence() {
        return photoRefrence;
    }

    public void setPhotoRefrence(String photoRefrence) {
        this.photoRefrence = photoRefrence;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(latLng, flags);
        dest.writeString(templeName);
        dest.writeString(templeAddress);
        dest.writeString(photoRefrence);
        dest.writeString(placeId);

    }
}

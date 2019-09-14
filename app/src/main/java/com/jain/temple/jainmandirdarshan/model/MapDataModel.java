package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 7/4/2017.
 */

public class MapDataModel implements Parcelable{
    private HashMap<Integer,NearPlaceLatLngModel> destinationNearPlaceLatLngModelList;
    private HashMap<Integer,NearPlaceLatLngModel> sourceNearPlaceLatLngModelList;
    private LatLng sourcelatLng;
    private LatLng destlatLng;
    private String title;
    private Place place;
    private String next_page_token;
    List<List<HashMap<String, String>>> polyLineResult;

    public MapDataModel() {
    }

    protected MapDataModel(Parcel in) {
        sourcelatLng = in.readParcelable(LatLng.class.getClassLoader());
        destlatLng = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        next_page_token = in.readString();

        destinationNearPlaceLatLngModelList= in.readHashMap(HashMap.class.getClassLoader());
        sourceNearPlaceLatLngModelList= in.readHashMap(HashMap.class.getClassLoader());
    }

    public static final Creator<MapDataModel> CREATOR = new Creator<MapDataModel>() {
        @Override
        public MapDataModel createFromParcel(Parcel in) {
            return new MapDataModel(in);
        }

        @Override
        public MapDataModel[] newArray(int size) {
            return new MapDataModel[size];
        }
    };

    public HashMap<Integer,NearPlaceLatLngModel> getSourceNearPlaceLatLngModelList() {
        return sourceNearPlaceLatLngModelList;
    }

    public void setSourceNearPlaceLatLngModelList(HashMap<Integer,NearPlaceLatLngModel> sourceNearPlaceLatLngModelList) {
        this.sourceNearPlaceLatLngModelList = sourceNearPlaceLatLngModelList;
    }


    public List<List<HashMap<String, String>>> getPolyLineResult() {
        return polyLineResult;
    }

    public void setPolyLineResult(List<List<HashMap<String, String>>> polyLineResult) {
        this.polyLineResult = polyLineResult;
    }
    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }






    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public LatLng getSourcelatLng() {
        return sourcelatLng;
    }

    public void setSourcelatLng(LatLng sourcelatLng) {
        this.sourcelatLng = sourcelatLng;
    }

    public LatLng getDestlatLng() {
        return destlatLng;
    }

    public void setDestlatLng(LatLng destlatLng) {
        this.destlatLng = destlatLng;
    }

    public HashMap<Integer,NearPlaceLatLngModel> getDestinationNearPlaceLatLngModelList() {
        return destinationNearPlaceLatLngModelList;
    }

    public void setDestinationNearPlaceLatLngModelList(HashMap<Integer,NearPlaceLatLngModel> destinationNearPlaceLatLngModelList) {
        this.destinationNearPlaceLatLngModelList = destinationNearPlaceLatLngModelList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(sourcelatLng, flags);
        dest.writeParcelable(destlatLng, flags);
        dest.writeString(title);
        dest.writeString(next_page_token);
        dest.writeMap(destinationNearPlaceLatLngModelList);
        dest.writeMap(sourceNearPlaceLatLngModelList);



    }
}




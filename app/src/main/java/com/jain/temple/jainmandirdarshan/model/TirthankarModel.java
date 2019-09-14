package com.jain.temple.jainmandirdarshan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 7/27/2017.
 */

public class TirthankarModel implements Parcelable{
    private String tirthankarName;
    private String birthPlace;
    private String birthtithi;
    private String fatherName;
    private String motherName;
    private String dikshaTithi;
    private String kevalgyanTithi;
    private String nakshatra;
    private String dikshaSathi;
    private String shadak_veevan;
    private String age_lived;
    private String lakshanSign;
    private String neervan_place;
    private String neervanSathi;
    private String neervanTithi;
    private String colour;
    private String father_name_hindi;
    private String mother_name_hindi;
    private String birthplace_hindi;
    private String neervan_place_hindi;
    private String name_hindi;
    private String sign_hindi;
    private int position;


    public TirthankarModel() {
    }

    protected TirthankarModel(Parcel in) {
        tirthankarName = in.readString();
        birthPlace = in.readString();
        birthtithi = in.readString();
        fatherName = in.readString();
        motherName = in.readString();
        dikshaTithi = in.readString();
        kevalgyanTithi = in.readString();
        nakshatra = in.readString();
        dikshaSathi = in.readString();
        shadak_veevan = in.readString();
        age_lived = in.readString();
        lakshanSign = in.readString();
        neervan_place = in.readString();
        neervanSathi = in.readString();
        neervanTithi = in.readString();
        colour = in.readString();
        father_name_hindi = in.readString();
        mother_name_hindi = in.readString();
        birthplace_hindi = in.readString();
        neervan_place_hindi = in.readString();
        name_hindi = in.readString();
        sign_hindi = in.readString();
        position = in.readInt();
    }

    public static final Creator<TirthankarModel> CREATOR = new Creator<TirthankarModel>() {
        @Override
        public TirthankarModel createFromParcel(Parcel in) {
            return new TirthankarModel(in);
        }

        @Override
        public TirthankarModel[] newArray(int size) {
            return new TirthankarModel[size];
        }
    };

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



    public String getSign_hindi() {
        return sign_hindi;
    }

    public void setSign_hindi(String sign_hindi) {
        this.sign_hindi = sign_hindi;
    }



    public String getName_hindi() {
        return name_hindi;
    }

    public void setName_hindi(String name_hindi) {
        this.name_hindi = name_hindi;
    }


    public String getFather_name_hindi() {
        return father_name_hindi;
    }

    public void setFather_name_hindi(String father_name_hindi) {
        this.father_name_hindi = father_name_hindi;
    }

    public String getMother_name_hindi() {
        return mother_name_hindi;
    }

    public void setMother_name_hindi(String mother_name_hindi) {
        this.mother_name_hindi = mother_name_hindi;
    }

    public String getBirthplace_hindi() {
        return birthplace_hindi;
    }

    public void setBirthplace_hindi(String birthplace_hindi) {
        this.birthplace_hindi = birthplace_hindi;
    }

    public String getNeervan_place_hindi() {
        return neervan_place_hindi;
    }

    public void setNeervan_place_hindi(String neervan_place_hindi) {
        this.neervan_place_hindi = neervan_place_hindi;
    }


    public String getTirthankarName() {
        return tirthankarName;
    }

    public void setTirthankarName(String tirthankarName) {
        this.tirthankarName = tirthankarName;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthtithi() {
        return birthtithi;
    }

    public void setBirthtithi(String birthtithi) {
        this.birthtithi = birthtithi;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getDikshaTithi() {
        return dikshaTithi;
    }

    public void setDikshaTithi(String dikshaTithi) {
        this.dikshaTithi = dikshaTithi;
    }

    public String getKevalgyanTithi() {
        return kevalgyanTithi;
    }

    public void setKevalgyanTithi(String kevalgyanTithi) {
        this.kevalgyanTithi = kevalgyanTithi;
    }

    public String getNakshatra() {
        return nakshatra;
    }

    public void setNakshatra(String nakshatra) {
        this.nakshatra = nakshatra;
    }

    public String getDikshaSathi() {
        return dikshaSathi;
    }

    public void setDikshaSathi(String dikshaSathi) {
        this.dikshaSathi = dikshaSathi;
    }

    public String getShadak_veevan() {
        return shadak_veevan;
    }

    public void setShadak_veevan(String shadak_veevan) {
        this.shadak_veevan = shadak_veevan;
    }

    public String getAge_lived() {
        return age_lived;
    }

    public void setAge_lived(String age_lived) {
        this.age_lived = age_lived;
    }

    public String getLakshanSign() {
        return lakshanSign;
    }

    public void setLakshanSign(String lakshanSign) {
        this.lakshanSign = lakshanSign;
    }

    public String getNeervan_place() {
        return neervan_place;
    }

    public void setNeervan_place(String neervan_place) {
        this.neervan_place = neervan_place;
    }

    public String getNeervanSathi() {
        return neervanSathi;
    }

    public void setNeervanSathi(String neervanSathi) {
        this.neervanSathi = neervanSathi;
    }

    public String getNeervanTithi() {
        return neervanTithi;
    }

    public void setNeervanTithi(String neervanTithi) {
        this.neervanTithi = neervanTithi;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tirthankarName);
        parcel.writeString(birthPlace);
        parcel.writeString(birthtithi);
        parcel.writeString(fatherName);
        parcel.writeString(motherName);
        parcel.writeString(dikshaTithi);
        parcel.writeString(kevalgyanTithi);
        parcel.writeString(nakshatra);
        parcel.writeString(dikshaSathi);
        parcel.writeString(shadak_veevan);
        parcel.writeString(age_lived);
        parcel.writeString(lakshanSign);
        parcel.writeString(neervan_place);
        parcel.writeString(neervanSathi);
        parcel.writeString(neervanTithi);
        parcel.writeString(colour);
        parcel.writeString(father_name_hindi);
        parcel.writeString(mother_name_hindi);
        parcel.writeString(birthplace_hindi);
        parcel.writeString(neervan_place_hindi);
        parcel.writeString(name_hindi);
        parcel.writeString(sign_hindi);
        parcel.writeInt(position);
    }
}

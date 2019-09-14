package com.jain.temple.jainmandirdarshan.roomORM.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id ;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "isSendNotification")
    private String isSendNotification;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsSendNotification() {
        return isSendNotification;
    }

    public void setIsSendNotification(String isSendNotification) {
        this.isSendNotification = isSendNotification;
    }






}

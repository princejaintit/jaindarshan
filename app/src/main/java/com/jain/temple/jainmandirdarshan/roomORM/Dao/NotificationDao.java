package com.jain.temple.jainmandirdarshan.roomORM.Dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jain.temple.jainmandirdarshan.roomORM.entity.NotificationEntity;



@Dao
public interface NotificationDao {

    @Query("SELECT * FROM NOTIFICATION WHERE date LIKE :dateCheck"+" LIMIT 1")
    NotificationEntity isNotificationSend(String dateCheck);

    @Insert
    void setSuccessNotificationresultDataBase(NotificationEntity notificationEntity);
}

package com.jain.temple.jainmandirdarshan.roomORM.Dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jain.temple.jainmandirdarshan.model.FavouriteModel;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavouriteDao {

    @Insert
    void addFavouriteTemple(MyFavourateTempleEntity favouriteModel);

    @Insert
    void AddFavouriteContent(MyFavourateTempleEntity favouriteModel);

    @Insert
    void AddFavouriteAudio(MyFavourateTempleEntity favouriteModel);

    @Query("SELECT * FROM myFavourateTemple WHERE placeId LIKE :placeIdCheck"+" LIMIT 1")
    MyFavourateTempleEntity isFavouriteTemple(String placeIdCheck);

    @Query("SELECT * FROM myFavourateTemple WHERE title LIKE :titleCheck"+" LIMIT 1")
    MyFavourateTempleEntity isFavouriteContent(String titleCheck);

    @Query("SELECT * FROM myFavourateTemple WHERE title LIKE :titleCheck"+" LIMIT 1")
    MyFavourateTempleEntity isFavouriteAudio(String titleCheck);

    @Query("SELECT * FROM myFavourateTemple WHERE category LIKE :catCheck")
    List<MyFavourateTempleEntity> getAllFavoriteTempleList(String catCheck);

    @Query("SELECT * FROM myFavourateTemple WHERE category LIKE :catCheck")
    List<MyFavourateTempleEntity> getAllFavoriteContentList(String catCheck);

    @Query("SELECT * FROM myFavourateTemple WHERE category LIKE :catCheck")
    List<MyFavourateTempleEntity> getAllFavoriteAudioList(String catCheck);

  /*  @Delete
    void deleteFavoriteTemple(MyFavourateTempleEntity myFavourateTempleEntity);*/

    @Query("DELETE FROM myFavourateTemple WHERE placeId = :placeIdCheck")
    void deleteFavoriteTemple(String placeIdCheck);


    @Query("DELETE FROM myFavourateTemple WHERE title = :titleCheck")
    void deleteFavoriteContent(String titleCheck);

    @Query("DELETE FROM myFavourateTemple WHERE title = :nameCheck")
    void deleteFavoriteAudio(String nameCheck);
}

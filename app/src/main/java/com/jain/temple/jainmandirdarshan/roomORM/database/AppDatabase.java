package com.jain.temple.jainmandirdarshan.roomORM.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jain.temple.jainmandirdarshan.roomORM.Dao.FavouriteDao;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;
import com.jain.temple.jainmandirdarshan.roomORM.entity.NotificationEntity;
import com.jain.temple.jainmandirdarshan.roomORM.Dao.NotificationDao;


@Database(entities = {NotificationEntity.class,MyFavourateTempleEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract NotificationDao notificationDao();
    public abstract FavouriteDao favouriteDao();





    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
              String DATABASE_NAME = "findTemple";
             String ID = "id";
              final int DATABASE_VERSION = 3;
             String TABLE_NAME_INFO = "myFavourateTemple";
             String TABLE_NAME_PIC = "tablePic";
             String TABLE_NAME_NOTIFICATION = "notification";

             String PLACE_ID = "placeId";
             String NAME = "name";
             String ADDRESS = "address";
             String CONTACT_NO = "contact_no";
             String RATING = "rating";
             String LATITUDE = "latitude";
             String LONGITUDE = "longitude";

             String CATEGORY = "category";
             String TITLE = "title";
             String DESCRIPTION = "descp";
             String CONTENT_CATEGORY = "content_category";
             String URL_REFRENCE = "url_refrence";

             String DATE = "date";
             String isSendNotification = "isSendNotification";

            /// private String T_ID = "id";
            //private String T_I_ID = "t_i_id";
             String T_PIC = "pic_url";


         /*  database.execSQL("ALTER TABLE users "
                    +"ADD COLUMN address TEXT");*/

            database.execSQL("DROP TABLE IF EXISTS " + "myFavourateTemple");
            database.execSQL("DROP TABLE IF EXISTS " + "notification");


        /*    database.execSQL(
                    "CREATE TABLE users_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(userid))");

            // Copy the data
            database.execSQL(
                    "INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
// Remove the old table
            database.execSQL("DROP TABLE users");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE users_new RENAME TO users");*/

            String CREATE_TABLE_INFO = "CREATE TABLE " + TABLE_NAME_INFO + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + CATEGORY + " TEXT," + CONTENT_CATEGORY + " TEXT," + TITLE + " TEXT," + DESCRIPTION + " TEXT," + PLACE_ID + " TEXT," + NAME + " TEXT,"
                    + ADDRESS + " TEXT," + URL_REFRENCE + " TEXT," + CONTACT_NO + " TEXT," + RATING + " TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT,"
                    + T_PIC + " TEXT" + ")";

            String CREATE_TABLE_NOTIFICATION = "CREATE TABLE " + TABLE_NAME_NOTIFICATION + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + DATE + " TEXT ," + isSendNotification + " TEXT" + ")";


            database.execSQL(CREATE_TABLE_INFO);
            database.execSQL(CREATE_TABLE_NOTIFICATION);

        }
    };


    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "findTemple")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .addMigrations(MIGRATION_3_4)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

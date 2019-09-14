package com.jain.temple.jainmandirdarshan.roomORM.utils;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.NotificationEntity;

public class DatabaseInitializer {


    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db, NotificationEntity user) {
        PopulateDbAsync task = new PopulateDbAsync(db,user);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db,NotificationEntity user) {
        populateWithTestData(db,user);
    }

  /*  private static User addUser(final AppDatabase db, User user) {
        db.userDao().insertAll(user);
        return user;
    }*/
  private static NotificationEntity addUser(final AppDatabase db, NotificationEntity user) {
      db.notificationDao().isNotificationSend(user.getDate());
      return user;
  }


    private static void populateWithTestData(AppDatabase db, NotificationEntity user) {
        /*User user = new User();
        user.setFirstName("Ajay");
        user.setLastName("Saini");
        user.setAge(25);*/
        addUser(db, user);

      /*  List<User> userList = db.userDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + userList.size());*/
    }
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private NotificationEntity user;

        PopulateDbAsync(AppDatabase db, NotificationEntity user) {
           this. mDb = db;
            this.user=user;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb,user);
            return null;
        }

    }
}

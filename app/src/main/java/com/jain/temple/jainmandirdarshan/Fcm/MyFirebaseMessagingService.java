/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jain.temple.jainmandirdarshan.Fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.Service.SendPushNotificationServiceOreo;
import com.jain.temple.jainmandirdarshan.activity.SplashScreenActivity;
import com.jain.temple.jainmandirdarshan.util.SharedPreferenceManager;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and NotificationEntity messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. NotificationEntity messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated NotificationEntity is displayed.
        // When the user taps on the NotificationEntity they are returned to the app. Messages containing both NotificationEntity
        // and data payloads are treated as NotificationEntity messages. The Firebase console always sends NotificationEntity
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());



            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }
        Map<String, String> notification11= remoteMessage.getData();
        // Check if message contains a NotificationEntity payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message NotificationEntity Body: " + remoteMessage.getNotification());

          RemoteMessage.Notification notification= remoteMessage.getNotification();

            sendNotification(notification);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        /*// [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]*/
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple NotificationEntity containing the received FCM message.
     *
     * @param notification FCM message body received.
     */
   /* private void sendNotification(RemoteMessage.NotificationEntity NotificationEntity) {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.launcher_icon)
                .setContentTitle(NotificationEntity.getTitle())
                .setContentText(NotificationEntity.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of NotificationEntity *//*, notificationBuilder.build());
    }
*/
    private void  sendNotification(RemoteMessage.Notification notification)  {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        String CHANNEL_ID = "my_channel_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
            // Register the channel with the system; you can't change the importance
            // or other NotificationEntity behaviors after this


           /*// NotificationManager notificationManager = getSystemService(NotificationManager.class);


            NotificationCompat.Builder mBuilder;
            mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("Title")
                    .setContentText("Text")
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.launcher_icon)
                    .setChannelId(CHANNEL_ID);

// Issue the NotificationEntity.
            notificationManager.notify(0, mBuilder.build());*/
        }


            callNotification(notification,CHANNEL_ID);

    }

    public void callNotification(RemoteMessage.Notification notification, String CHANNEL_ID){
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
     //   String CHANNEL_ID = "my_channel_01";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.launcher_icon)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
              //  .setChannelId(CHANNEL_ID)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of NotificationEntity */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());

        sharedPreferenceManager.setToken(token);

        Intent i = new Intent( getApplicationContext(), SendPushNotificationServiceOreo.class);
        SendPushNotificationServiceOreo.enqueueWork( this,i);

    }


}

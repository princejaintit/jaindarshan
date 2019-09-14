package com.jain.temple.jainmandirdarshan.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.jain.temple.jainmandirdarshan.Service.SendPushNotificationService;
import com.jain.temple.jainmandirdarshan.Service.SendPushNotificationServiceOreo;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG ="AlarmReceiver";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");


     //   context.startForegroundService(new Intent(context,SendPushNotificationService.class));

        Intent i = new Intent(context, SendPushNotificationServiceOreo.class);
        SendPushNotificationServiceOreo.enqueueWork(context,i);

    }
}

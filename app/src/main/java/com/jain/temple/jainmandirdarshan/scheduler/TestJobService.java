package com.jain.temple.jainmandirdarshan.scheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.jain.temple.jainmandirdarshan.Service.SendPushNotificationServiceOreo;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TestJobService extends JobService {
    private static final String TAG = "SyncService";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onStartJob(JobParameters params) {
            /*Intent service = new Intent(getApplicationContext(), SendPushNotificationService.class);
            getApplicationContext().startForegroundService(service);*/

        Intent i = new Intent( getApplicationContext(), SendPushNotificationServiceOreo.class);
        SendPushNotificationServiceOreo.enqueueWork( getApplicationContext(),i);
        //Toast.makeText(getApplicationContext(),"hhhhhhhhhhhhhhhhhh",Toast.LENGTH_LONG).show();
        Log.d("aaaaaaaaaaaaaaaaaaa","jjjjjjjjjjj");
        Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}

package com.jain.temple.jainmandirdarshan.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class MyStartServiceReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("cellecteddddddd","true");
      //  Toast.makeText(context,"hjhejgjggjjgre",Toast.LENGTH_LONG).show();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Util.scheduleJob(context);
        }
    }

}

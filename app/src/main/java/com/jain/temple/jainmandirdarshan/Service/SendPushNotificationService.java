package com.jain.temple.jainmandirdarshan.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.CustomRequestJsonRaw;
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel;
import com.jain.temple.jainmandirdarshan.model.CalendarMonthModel;
import com.jain.temple.jainmandirdarshan.util.AppController;
import com.jain.temple.jainmandirdarshan.util.SharedPreferenceManager;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.FCM_URL;

/**
 * Created by admin on 8/28/2017.
 */

public class SendPushNotificationService extends IntentService {
   // private DataBaseHelper db;
    private String todayDate;

    public SendPushNotificationService() {
        super("HelloIntentService");

    }

    public SendPushNotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        Log.d("ssssssssssssssssss", "true");

        Calendar calendar = Calendar.getInstance();
        UiUtils uiUtils = new UiUtils();
        todayDate = uiUtils.getCurrentDayData(calendar);
      /*  db = new DataBaseHelper(this);
        boolean isTodayNotificationSend = db.isTodayNotificationSend(todayDate);


        Log.d("isTodayNotificationSend", isTodayNotificationSend + "");


        if (!isTodayNotificationSend) {
            ArrayList<CalendarMonthModel> modelArrayList = new ArrayList<>();

            getCurentDateTithi(uiUtils, calendar, getApplicationContext(), modelArrayList);

        }
*/
    }

    public void getCurentDateTithi(UiUtils uiUtils, Calendar calendar, Context activity, ArrayList<CalendarMonthModel> monthModelArrayList) {

        String detail = "आज ";
        boolean sendNotification=false;
        // int week = calendar.get(Calendar.DAY_OF_WEEK);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        JSONObject jsonObject = uiUtils.getCalendarlistForTithi(activity);

        monthModelArrayList = uiUtils.parseJsonforCalendar(jsonObject, monthModelArrayList, 0, 0);

        if (monthModelArrayList.size() > 0) {
            CalendarMonthModel calendarMonthModel = monthModelArrayList.get(month);
            ArrayList<CalendarDayModel> calendarDayModelArrayList = calendarMonthModel.getDayModelArrayList();

            CalendarDayModel calendarDayModel = calendarDayModelArrayList.get(day - 1);
            String tithi = calendarDayModel.getTithi_e();


            if (tithi.equalsIgnoreCase("Ashtami") || tithi.equalsIgnoreCase("Chaturdashi")) {
                detail = detail+" "+calendarDayModel.getTithi_eh() + ",";
                sendNotification=true;

            }
            if (calendarDayModel.getSpecial().equalsIgnoreCase("Y")) {
                detail = detail + calendarDayModel.getDetail();
                sendNotification=true;
            }
            if (sendNotification) {
                sendNotification(detail + " है |");
            }
        }


    }

    private void sendNotification(String detail) {
        try {
            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());
            String deviceToken = sharedPreferenceManager.getToken();


            JSONObject jsonObject = new JSONObject();
            JSONObject notificationJson = new JSONObject();

            jsonObject.put("to", deviceToken);

            notificationJson.put("body", detail);
            notificationJson.put("title", getApplicationContext().getString(R.string.app_name));

            jsonObject.put("NotificationEntity", notificationJson);

            Log.d("jsonObject",jsonObject.toString());

            AppController.getInstance().addToRequestQueue(new CustomRequestJsonRaw(getApplicationContext(), Request.Method.POST,
                    FCM_URL, jsonObject, dataResponse,
                    errorListener), "tag_send_notification");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Response.Listener<JSONObject> dataResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            Log.d("responseDistanceeeeeee", response.toString());

        //    db.setSuccessNotificationresultDataBase(getApplicationContext(), todayDate);


        }

    };


    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());


            try {
                if (error.networkResponse != null) {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);


                }
            } catch (JSONException e) {
                //Handle a malformed json response
            } catch (UnsupportedEncodingException e) {

            }

        }
    };


}

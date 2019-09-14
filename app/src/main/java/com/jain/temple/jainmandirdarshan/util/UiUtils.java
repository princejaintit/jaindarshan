package com.jain.temple.jainmandirdarshan.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.CustomRequestJson;
import com.jain.temple.jainmandirdarshan.helper.DialogBoxHelper;
import com.jain.temple.jainmandirdarshan.model.AudioModel;
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel;
import com.jain.temple.jainmandirdarshan.model.CalendarMonthModel;
import com.jain.temple.jainmandirdarshan.model.DayDetail;
import com.jain.temple.jainmandirdarshan.model.FavouriteModel;
import com.jain.temple.jainmandirdarshan.model.PoemModel;
import com.jain.temple.jainmandirdarshan.model.PujaMainModel;
import com.jain.temple.jainmandirdarshan.model.StrotraModel;
import com.jain.temple.jainmandirdarshan.model.TirthankarModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by admin on 6/29/2017.
 */

public class UiUtils {
    private AdaptorClickInterface adaptorClickInterface;
    private Activity activity;
    private  ListenerData listenerData;
private DialogBoxHelper dialogBoxHelper;

    public UiUtils() {
    }


    public  boolean isStoragePermissionGrantedActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                }, 101);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    public  boolean isCallPermissionGrantedActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED ) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_COARSE_LOCATION
                }, 101);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    public  boolean isCallPermissionGrantedFragment(Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED ) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                fragment.requestPermissions(new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_COARSE_LOCATION
                }, 101);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    public static boolean isLocationPermissionGrantedFragment(Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public void makeCalculateDistanceRequest(Activity activity, AdaptorClickInterface adaptorClickInterface, LatLng sourcelatLng, LatLng destlatLng) {
       this.activity=activity;
        this.adaptorClickInterface=adaptorClickInterface;

        dialogBoxHelper=new DialogBoxHelper(activity);
        dialogBoxHelper.showProgressDialog();

        StringBuilder googlePlacesUrl =
                new StringBuilder("http://maps.googleapis.com/maps/api/distancematrix/json?");
        googlePlacesUrl.append("origins=").append(sourcelatLng.latitude).append(",").append(sourcelatLng.longitude);
        googlePlacesUrl.append("&destinations=").append(destlatLng.latitude).append(",").append(destlatLng.longitude);
        googlePlacesUrl.append("&mode=driving").append("&language=en-EN");
        googlePlacesUrl.append("&sensor=false");


        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<String, String>();

        AppController.getInstance().addToRequestQueue(new CustomRequestJson(activity, Request.Method.GET,
                googlePlacesUrl.toString(), params,mHeaders, dataResponse,
                errorListener), "tag_calculate distance_req");
    }

    private Response.Listener<JSONObject>  dataResponse = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("responsedddddddddd", response.toString());

                dialogBoxHelper.hideProgressDialog();
                try {
                    JSONArray rowsArr = response.getJSONArray("rows");

                    JSONObject rowJson = (JSONObject) rowsArr.get(0);
                    JSONObject elementsJson = (JSONObject) rowJson.getJSONArray("elements").get(0);

                    String distance = elementsJson.getJSONObject("distance").getString("text");

                    String duration = elementsJson.getJSONObject("duration").getString("text");

                  //  ((FindTempleActivity)activity).showsnackBarMessage(distance + "  :  " + duration);

                    adaptorClickInterface.getitemClickPosition(distance + "  :  " + duration+" By Driving");

                    // Toast.makeText(getActivity(), distance + "  :  " + duration, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        };


    public void makeCalculateDistanceRequest2(Activity activity, ListenerData listenerData, LatLng sourcelatLng, LatLng destlatLng) {
        this.activity=activity;
        this.listenerData=listenerData;


        dialogBoxHelper  = new DialogBoxHelper(activity);
        dialogBoxHelper.showProgressDialog();

        StringBuilder googlePlacesUrl =
                new StringBuilder("http://maps.googleapis.com/maps/api/distancematrix/json?");
        googlePlacesUrl.append("origins=").append(sourcelatLng.latitude).append(",").append(sourcelatLng.longitude);
        googlePlacesUrl.append("&destinations=").append(destlatLng.latitude).append(",").append(destlatLng.longitude);
        googlePlacesUrl.append("&mode=driving").append("&language=en-EN");
        googlePlacesUrl.append("&sensor=false");


        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<String, String>();

        AppController.getInstance().addToRequestQueue(new CustomRequestJson(activity, Request.Method.GET,
                googlePlacesUrl.toString(), params,mHeaders, responseData,
                errorListener), "tag_calculate distance_req");
    }

    private Response.Listener<JSONObject>  responseData;

    {
        responseData = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("responsedddddddddd", response.toString());

                dialogBoxHelper.hideProgressDialog();


                try {
                    JSONArray rowsArr = response.getJSONArray("rows");

                    JSONObject rowJson = (JSONObject) rowsArr.get(0);
                    JSONObject elementsJson = (JSONObject) rowJson.getJSONArray("elements").get(0);

                    String distance = elementsJson.getJSONObject("distance").getString("text");

                    String duration = elementsJson.getJSONObject("duration").getString("text");

                    //  ((FindTempleActivity)activity).showsnackBarMessage(distance + "  :  " + duration);

                    listenerData.onItemSelected(null,response);

                    // Toast.makeText(getActivity(), distance + "  :  " + duration, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        };
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());
            dialogBoxHelper.hideProgressDialog();

            if (error != null){

                error.getMessage();
                //  dialogBoxHelper.hideProgressDialog();
                Log.d("response", error.toString());
                if (error.networkResponse != null) {
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);

                        Toast.makeText(activity, jsonObject.getString("error"), Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        //Handle a malformed json response
                    } catch (UnsupportedEncodingException e) {

                    }
                }

                else{
                    Toast.makeText(activity,     error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }
    };

    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

      /*  boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;*/
    }

    public void showAlertDialog(Activity activity, String message, String title) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

/*builder1.setNegativeButton(
        "No",
        new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    });*/

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void showSnackBarMessage(CoordinatorLayout coordinatorLayout, String msg) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        /*snackbar.setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });*/

        snackbar.show();
    }


    public void showSnackBarMessageInfinite(CoordinatorLayout coordinatorLayout,String msg) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }



    public JSONObject getStrotralist(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("strotra/strorta.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

   public ArrayList<StrotraModel> parseJsonforStrotra(JSONObject jsonObject,ArrayList<StrotraModel> modelArrayList){

       try {
           JSONArray stotraArr= jsonObject.getJSONArray("stotra");

           for (int i = 0; i <stotraArr.length() ; i++) {
               StrotraModel strotraModel=new StrotraModel();
              JSONObject strotraJson= (JSONObject) stotraArr.get(i);

               strotraModel.setName(strotraJson.getString("name").toUpperCase());
               strotraModel.setName_hindi(strotraJson.getString("name_h"));
               strotraModel.setDetail(strotraJson.getString("content"));
               strotraModel.setCategory("स्तोत्र");

               modelArrayList.add(strotraModel);

           }

       } catch (JSONException e) {
           e.printStackTrace();
       }
       return  modelArrayList;
    }



    public JSONObject getChalisalist(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("chalisa/chalisa.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<StrotraModel> parseJsonforChalisa(JSONObject jsonObject,ArrayList<StrotraModel> modelArrayList){

        try {
            JSONArray stotraArr= jsonObject.getJSONArray("chalisa");

            for (int i = 0; i <stotraArr.length() ; i++) {
                StrotraModel strotraModel=new StrotraModel();
                JSONObject strotraJson= (JSONObject) stotraArr.get(i);

                strotraModel.setId(Integer.parseInt(strotraJson.getString("id")));
                strotraModel.setName(strotraJson.getString("name").toUpperCase());
                strotraModel.setName_hindi(strotraJson.getString("name_h"));
                strotraModel.setDetail(strotraJson.getString("content"));
                strotraModel.setCategory("चालीसा");

                modelArrayList.add(strotraModel);

            }

            Collections.sort(modelArrayList);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }

    public JSONObject getPujalist(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("puja/puja.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<StrotraModel> parseJsonforPuja(JSONObject jsonObject,ArrayList<StrotraModel> modelArrayList){

        try {
            JSONArray stotraArr= jsonObject.getJSONArray("puja");

            for (int i = 0; i <stotraArr.length() ; i++) {
                StrotraModel strotraModel=new StrotraModel();
                JSONObject strotraJson= (JSONObject) stotraArr.get(i);

                strotraModel.setName(strotraJson.getString("name").toUpperCase());
                strotraModel.setName_hindi(strotraJson.getString("name_h"));
                strotraModel.setDetail(strotraJson.getString("content"));
                strotraModel.setCategory("पूजा");

                modelArrayList.add(strotraModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }


    public JSONObject getCalendarlist(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("calendar/calendar.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<CalendarMonthModel> parseJsonforCalendar(JSONObject jsonObject, ArrayList<CalendarMonthModel> modelArrayList, int monthIndex, int DAY_OF_MONTH){

        try {
           Calendar calendar= Calendar.getInstance();
           int year= calendar.get(Calendar.YEAR);

            if (jsonObject.has(year+"")){



            ArrayList<CalendarDayModel> dayModelArrayList = null;
            JSONArray calendarArr= jsonObject.getJSONArray(year+"");

            for (int i = 0; i <calendarArr.length() ; i++) {
                String month = "";
                dayModelArrayList = new ArrayList<>();
                JSONObject calendarJson = (JSONObject) calendarArr.get(i);
                CalendarMonthModel calendarMonthModel = new CalendarMonthModel();

                calendarMonthModel.setMonthNameEnglish(calendarJson.getString("month_m"));
                calendarMonthModel.setMonthNameHildiStart(calendarJson.getString("start_m"));
                calendarMonthModel.setMonthNameHildiEnd(calendarJson.getString("end_m"));
                calendarMonthModel.setVeer_samvat(calendarJson.getString("veer_samvat_m"));
                calendarMonthModel.setVikram_samvat(calendarJson.getString("vikram_samvat_m"));


                switch (calendarJson.getString("month_m")) {
                    case "012019":
                        month = "जनवरी";
                        break;
                    case "022019":
                        month = "फरवरी";
                        break;
                    case "032019":
                        month = "मार्च";
                        break;
                    case "042019":
                        month = "अप्रैल";
                        break;
                    case "052019":
                        month = "मई";
                        break;
                    case "062019":
                        month = "जून";
                        break;
                    case "072019":
                        month = "जुलाई";
                        break;
                    case "082019":
                        month = "अगस्त";
                        break;
                    case "092019":
                        month = "सितंबर";
                        break;
                    case "102019":
                        month = "अक्टूबर";
                        break;
                    case "112019":
                        month = "नवंबर";
                        break;
                    case "122019":
                        month = "दिसंबर";
                        break;


                }
                calendarMonthModel.setMonthHindi(month);

                JSONArray DaysArr = calendarJson.getJSONArray("Days");

                for (int j = 0; j < DaysArr.length(); j++) {

                    JSONObject daysJson = (JSONObject) DaysArr.get(j);

                    CalendarDayModel calendarDayModel = new CalendarDayModel();

                    calendarDayModel.setVikram_samvat(daysJson.getString("vikram_samvat"));
                    calendarDayModel.setVeer_samvat(daysJson.getString("veer_samvat"));
                    calendarDayModel.setDay_d(daysJson.getString("day_d"));
                    calendarDayModel.setTithi_e(daysJson.getString("tithi_e"));
                    calendarDayModel.setTithi_eh(daysJson.getString("tithi_sh"));
                    calendarDayModel.setPaksh(daysJson.getString("paksh"));
                    calendarDayModel.setPaksh_h(daysJson.getString("paksh_h"));
                    calendarDayModel.setMahina(daysJson.getString("mahina"));
                    calendarDayModel.setMahina_h(daysJson.getString("mahina_h"));
                    calendarDayModel.setMonth_d(i + 1 + "");
                    calendarDayModel.setSpecial(daysJson.getString("special"));
                    calendarDayModel.setDetail(daysJson.getString("Detail"));

                    if (i == monthIndex && daysJson.getString("day_d").equalsIgnoreCase(DAY_OF_MONTH + "")) {
                        calendarDayModel.setTodayDate(true);
                    }

                    dayModelArrayList.add(calendarDayModel);

                }
                calendarMonthModel.setDayModelArrayList(dayModelArrayList);

                modelArrayList.add(calendarMonthModel);
            }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }

   public String getCurrentDayData(Calendar calendar){

       System.out.println("Current time => " + calendar.getTime());

       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
       String formattedDate = df.format(calendar.getTime());

       return formattedDate;
    }

    public String getMonth(Calendar calendar) {
        String str = "";
        int month = calendar.get(Calendar.MONTH);

        switch (month) {
            case 0:
                str = "JAN";
                break;
            case 1:
                str = "FEB";
                break;
            case 2:
                str = "MAR";
                break;
            case 3:
                str = "APR";
                break;
            case 4:
                str = "MAY";
                break;
            case 5:
                str = "JUN";
                break;
            case 6:
                str = "JUL";
                break;
            case 7:
                str = "AUG";
                break;
            case 8:
                str = "SEP";
                break;
            case 9:
                str = "OCT";
                break;
            case 10:
                str = "NOV";
                break;
            case 11:
                str = "DEC";
                break;

        }
        return str;
    }

    public int getDayOfMonth(Calendar calendar) {

        int month = calendar.get(Calendar.DAY_OF_MONTH);

        return month;

        }


    public String getDayOfWeek(Calendar calendar) {
        String str="";
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        switch (week) {
            case 1:
                str = "Sunday (रविवार)";
                break;
            case 2:
                str = "Monday (सोमवार)";
                break;
            case 3:
                str = "Tuesday (मंगलवार)";
                break;
            case 4:
                str = "Wenesday (बुधवार)";
                break;
            case 5:
                str = "Thursday (गुरूवार)";
                break;
            case 6:
                str = "Friday (शुक्रवार)";
                break;
            case 7:
                str = "Saturday (शनिवार)";
                break;


        }
        return str;

    }


    public JSONObject getCalendarlistForTithi(Context activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("calendar/calendar_30days.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public JSONObject getBhavnalist(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("bhavna/bhavna.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<StrotraModel> parseJsonforBhavna(JSONObject jsonObject,ArrayList<StrotraModel> modelArrayList){

        try {
            JSONArray stotraArr= jsonObject.getJSONArray("data");

            for (int i = 0; i <stotraArr.length() ; i++) {
                StrotraModel strotraModel=new StrotraModel();
                JSONObject strotraJson= (JSONObject) stotraArr.get(i);

                strotraModel.setName(strotraJson.getString("name").toUpperCase());
                strotraModel.setName_hindi(strotraJson.getString("name_h"));
                strotraModel.setDetail(strotraJson.getString("content"));
                strotraModel.setCategory("भक्ति");

                modelArrayList.add(strotraModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }

    public JSONObject getStutiList(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("stuti/stuti.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<StrotraModel> parseJsonforStuti(JSONObject jsonObject,ArrayList<StrotraModel> modelArrayList){

        try {
            JSONArray stotraArr= jsonObject.getJSONArray("data");

            for (int i = 0; i <stotraArr.length() ; i++) {
                StrotraModel strotraModel=new StrotraModel();
                JSONObject strotraJson= (JSONObject) stotraArr.get(i);

                strotraModel.setName(strotraJson.getString("name").toUpperCase());
                strotraModel.setName_hindi(strotraJson.getString("name_h"));
                strotraModel.setDetail(strotraJson.getString("content"));
                strotraModel.setCategory("स्तुति");

                modelArrayList.add(strotraModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }







    public JSONObject getAartiList(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("aarti/aarti.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<StrotraModel> parseJsonforAarti(JSONObject jsonObject,ArrayList<StrotraModel> modelArrayList){

        try {
            JSONArray stotraArr= jsonObject.getJSONArray("aarti");

            for (int i = 0; i <stotraArr.length() ; i++) {
                StrotraModel strotraModel=new StrotraModel();
                JSONObject strotraJson= (JSONObject) stotraArr.get(i);

                strotraModel.setName(strotraJson.getString("name").toUpperCase());
                strotraModel.setName_hindi(strotraJson.getString("name_h"));
                strotraModel.setDetail(strotraJson.getString("details"));
                strotraModel.setCategory("आरती");

                modelArrayList.add(strotraModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }








    public JSONObject getPujalistCategoryWise(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("puja/puja_category.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<PujaMainModel> parseJsonforPujaCategoryWise(JSONObject jsonObject,ArrayList<PujaMainModel> modelArrayList){

        try {
            JSONArray stotraArr= jsonObject.getJSONArray("puja");

            for (int i = 0; i <stotraArr.length() ; i++) {
                ArrayList<StrotraModel> subPujaList=new ArrayList<>();
                PujaMainModel pujaMainModel=new PujaMainModel();
                JSONObject pujaJson= (JSONObject) stotraArr.get(i);

                pujaMainModel.setId(Integer.parseInt(pujaJson.getString("id")));
                pujaMainModel.setName(pujaJson.getString("name"));
                pujaMainModel.setName_hindi(pujaJson.getString("name_h"));

                JSONArray dataArr=pujaJson.getJSONArray("data");

                for (int j = 0; j < dataArr.length(); j++) {
                    StrotraModel strotraModel=new StrotraModel();
                    JSONObject strotraJson= (JSONObject) dataArr.get(j);

                    strotraModel.setId(Integer.parseInt(strotraJson.getString("id")));
                    strotraModel.setName(strotraJson.getString("name"));
                    strotraModel.setName_hindi(strotraJson.getString("name_h"));
                    strotraModel.setDetail(strotraJson.getString("content"));
                    strotraModel.setCategory("पूजा");

                    subPujaList.add(strotraModel);
                }

                Collections.sort(subPujaList);

                pujaMainModel.setModelArrayList(subPujaList);

                modelArrayList.add(pujaMainModel);




            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }


    public ArrayList<DayDetail> getDataForDayDetail(CalendarDayModel calendarDayModel, ArrayList<DayDetail> detailArrayList) {

        DayDetail dayDetail=new DayDetail();
        dayDetail.setDayName("तिथि:");
        dayDetail.setDayDetail(calendarDayModel.getTithi_eh());
        detailArrayList.add(dayDetail);

        DayDetail dayDetai2=new DayDetail();
        dayDetai2.setDayName("पक्ष:");
        dayDetai2.setDayDetail(calendarDayModel.getPaksh_h());
        detailArrayList.add(dayDetai2);

        DayDetail dayDetail3=new DayDetail();
        dayDetail3.setDayName("मास:");
        dayDetail3.setDayDetail(calendarDayModel.getMahina_h());
        detailArrayList.add(dayDetail3);

        DayDetail dayDetail4=new DayDetail();
        dayDetail4.setDayName("विक्रम सम्वत:");
        dayDetail4.setDayDetail(calendarDayModel.getVikram_samvat());
        detailArrayList.add(dayDetail4);

        DayDetail dayDetail5=new DayDetail();
        dayDetail5.setDayName("वीर सम्वत:");
        dayDetail5.setDayDetail(calendarDayModel.getVeer_samvat());
        detailArrayList.add(dayDetail5);

        return detailArrayList;


    }



    public JSONObject getTirthankarlist(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("tirthankar/tirthankar.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }


    public ArrayList<TirthankarModel> parseJsonforTirthankar(JSONObject jsonObject, ArrayList<TirthankarModel> modelArrayList){

        try {
            JSONArray tirthankarArr= jsonObject.getJSONArray("tirthankar");

            for (int i = 0; i <tirthankarArr.length() ; i++) {
                TirthankarModel tirthankarModel = new TirthankarModel();
                JSONObject tirthankarJson = (JSONObject) tirthankarArr.get(i);

                tirthankarModel.setPosition(i+1);
                tirthankarModel.setTirthankarName(tirthankarJson.getString("name"));
                tirthankarModel.setName_hindi(i+1+": "+tirthankarJson.getString("name_hindi"));
                tirthankarModel.setLakshanSign(tirthankarJson.getString("lakshan_sign"));
                tirthankarModel.setSign_hindi(tirthankarJson.getString("sign_hindi"));
                tirthankarModel.setAge_lived(tirthankarJson.getString("age_lived"));
                tirthankarModel.setBirthPlace(tirthankarJson.getString("birth_place"));
                tirthankarModel.setBirthplace_hindi(tirthankarJson.getString("birthplace_hindi"));
                tirthankarModel.setBirthtithi(tirthankarJson.getString("birth_thithi"));

                tirthankarModel.setColour(tirthankarJson.getString("colour"));
                tirthankarModel.setDikshaSathi(tirthankarJson.getString("diksha_sathi"));
                tirthankarModel.setDikshaTithi(tirthankarJson.getString("diksha_thithi"));
                tirthankarModel.setFatherName(tirthankarJson.getString("father_name"));
                tirthankarModel.setFather_name_hindi(tirthankarJson.getString("father_name_hindi"));
                tirthankarModel.setMotherName(tirthankarJson.getString("mother_name"));
                tirthankarModel.setMother_name_hindi(tirthankarJson.getString("mother_name_hindi"));
                tirthankarModel.setKevalgyanTithi(tirthankarJson.getString("Kevalgyan_thithi"));
                tirthankarModel.setNakshatra(tirthankarJson.getString("nakshatra"));
                tirthankarModel.setShadak_veevan(tirthankarJson.getString("shadhak_Jeevan"));


                tirthankarModel.setNeervan_place(tirthankarJson.getString("neervan_place"));
                tirthankarModel.setNeervan_place_hindi(tirthankarJson.getString("neervan_place_hindi"));
                tirthankarModel.setNeervanSathi(tirthankarJson.getString("neervan_sathi"));
                tirthankarModel.setNeervanTithi(tirthankarJson.getString("neervan_thithi"));

                modelArrayList.add(tirthankarModel);

             //   modelArrayList.add(pujaMainModel);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }








    public JSONObject getBhajanList(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("bhajan/bhajan.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<StrotraModel> parseJsonforBhajan(JSONObject jsonObject,ArrayList<StrotraModel> modelArrayList){
        try {
            JSONArray stotraArr= jsonObject.getJSONArray("data");

            for (int i = 0; i <stotraArr.length() ; i++) {
                StrotraModel strotraModel=new StrotraModel();
                JSONObject strotraJson= (JSONObject) stotraArr.get(i);

                strotraModel.setName(strotraJson.getString("name").toUpperCase());
                strotraModel.setName_hindi(strotraJson.getString("name_h"));
                strotraModel.setDetail(strotraJson.getString("content"));
                strotraModel.setCategory("भजन");

                modelArrayList.add(strotraModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }






    public JSONObject getPoemList(Activity activity) {
        StringBuilder buf=new StringBuilder();
        JSONObject jsonObject = null;
        try {

            InputStream json=activity.getAssets().open("poem/poem.dat");
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            jsonObject=new JSONObject(buf.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    public ArrayList<PoemModel> parseJsonforPoem(JSONObject jsonObject, ArrayList<PoemModel> modelArrayList){

        try {
            JSONArray poemArr= jsonObject.getJSONArray("data");

            for (int i = 0; i <poemArr.length() ; i++) {
                PoemModel poemModel=new PoemModel();
                JSONObject poemJson= (JSONObject) poemArr.get(i);

                poemModel.setTitle(poemJson.getString("title"));
                poemModel.setContent(poemJson.getString("content"));

                modelArrayList.add(poemModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  modelArrayList;
    }



    public String getPoemDetail(Activity activity,String content) {
        StringBuilder buf=new StringBuilder();

        try {

            InputStream json=activity.getAssets().open("poem/"+content);
            BufferedReader in= null;

            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

            String str;

            while ((str=in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return buf.toString();
    }



    public void appShareContent(Activity activity){
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "Share This App");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+activity.getPackageName());
        activity. startActivity(Intent.createChooser(share, "Share App"));
    }

    public void callShareTempleInfo(Activity activity,FavouriteModel favouriteModel) {
        String shareBody =favouriteModel.getTempleName()+"\n"+favouriteModel.getTempleAddress()+"\nContanct no: "+favouriteModel.getTempleContactNo()
                +"\n\n" +"Location: "+ "http://maps.google.com/maps?q=loc:"+favouriteModel.getLatLng().latitude+","+favouriteModel.getLatLng().longitude+"\n\n"+"Download The app from here: \n"+"http://market.android.com/details?id="+activity.getPackageName();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Temple Info");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);

        activity. startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.templedetail)));
    }

    public void setFcmToken(Context applicationContext, String token) {

        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(applicationContext);
        String fcm_token= sharedPreferenceManager.getToken();
        if (fcm_token.equalsIgnoreCase("")){
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            sharedPreferenceManager.setToken(refreshedToken);
        }
    }

    public ArrayList<AudioModel> getDownloadListData(Activity activity, ArrayList<AudioModel> audioModelArrayList) {

        File mydir = activity.getDir("audio", Context.MODE_PRIVATE);
      //  File file= activity.getFilesDir();
       File [] files= mydir.listFiles();

        for (File file1:files) {
            AudioModel audioModel=new AudioModel();

           String name= file1.getName();
            audioModel.setTitle_h(name.substring(0,name.indexOf(".")));
            audioModel.setUrl_refrence(file1.getAbsolutePath());
            audioModel.setCategory("");
            audioModel.setTitle_e("");
            audioModel.setDetail("");

            audioModelArrayList.add(audioModel);

        }

        return audioModelArrayList;
    }


    public boolean checkFileInDirectory(Activity activity,String filename){
        boolean isExit = false;
        File mydir = activity.getDir("audio", Context.MODE_PRIVATE);
        //  File file= activity.getFilesDir();
        File [] files= mydir.listFiles();

        for (File file1:files) {
            if (filename.equalsIgnoreCase(file1.getName())){
                isExit=true;
            }

        }
      return  isExit;
    }

    public String convertDateUTCtoLocal(String dateSrc){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateSrc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
      String  timeZone = Calendar.getInstance().getTimeZone().getID();
        df1.setTimeZone(TimeZone.getTimeZone(timeZone));
        String dfre= df1.format(date);

        return dfre;




    }
}

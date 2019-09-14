package com.jain.temple.jainmandirdarshan.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by prince on 3/31/2016.
 */
public class SharedPreferenceManager {


    private static final String PREFERENCE_FILE_NAME = "jainDarshan_Pref";
    private static final String FCM_TOKEN ="token";;

    boolean result = false;
    private Context context;
    private String res = "";

    SharedPreferences sharedPreferences;

    public SharedPreferenceManager(Context context) {

        this.context=context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME,context.MODE_PRIVATE);


    }

    public void setToken(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FCM_TOKEN, token);
        result = editor.commit();
    }

    public String getToken(){
        String token;
        token = sharedPreferences.getString(FCM_TOKEN, "");

        return  token;
    }



}

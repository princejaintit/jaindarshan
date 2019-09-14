package com.jain.temple.jainmandirdarshan.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Ashish on 12/24/2015.
 */
public class DialogBoxHelper {

    private Context mContext;
    private ProgressDialog pDialog;
    private String title="Loading...";

    public DialogBoxHelper(Activity activity) {


        pDialog = new ProgressDialog(activity);
        pDialog.setMessage(title);
        pDialog.setCancelable(false);
    }

    public void setTitle(String title){
        pDialog.setMessage(title);

    }

    public void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
        pDialog.cancel();
    }



}

package com.jain.temple.jainmandirdarshan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.Service.SendPushNotificationServiceOreo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by admin on 8/23/2017.
 */

public class SplashScreenActivity extends BaseActivity {
    // Splash screen timer
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        //    checkVersion();
      //  String deviceToken = FirebaseInstanceId.getInstance().getToken();

        Fabric.with(this, new Crashlytics());


       /* FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d(TAG, token);
                        Toast.makeText(SplashScreenActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
*/

        skipActivity();
    }




  /*  private void checkVersion() {

        String url="https://androidquery.appspot.com/api/market?app="+getPackageName();

        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<>();


        AppController.getInstance().addToRequestQueue(new CustomRequestJson(SplashScreenActivity.this, Request.Method.GET,
                url, params, mHeaders, versionResponse,
                errorListener), "tag_version_req");
    }


    private Response.Listener<JSONObject> versionResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("responseRadiussssss", response.toString());
            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                if (response.getString("status").equalsIgnoreCase("1")) {

                    String onLineVersion=  response.getString("version");

                    if (!onLineVersion.equalsIgnoreCase(version)){
                        AlertDialog alertDialog=   createDeleteDialog(getString(R.string.app_name),getString(R.string.new_version_available));
                        alertDialog.show();
                    }

                    else{
                        skipActivity();
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    };*/

    private void skipActivity() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                //  intent.putExtra("fromSplash",true);
                startActivity(intent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());
            skipActivity();

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


    private AlertDialog createDeleteDialog(String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                openAppInPlayStore(SplashScreenActivity.this);
                paramAnonymousDialogInterface.dismiss();
              //  skipActivity();
            }
        }).setNegativeButton(getString(R.string.no_thanks), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                skipActivity();
            }
        })/*.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                skipActivity();

            }
        })*/
        .setCancelable(false)
        .setMessage(message).setTitle(title).create();
        return dialog;
    }
}

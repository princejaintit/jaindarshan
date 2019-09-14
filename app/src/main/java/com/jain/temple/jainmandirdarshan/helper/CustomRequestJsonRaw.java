package com.jain.temple.jainmandirdarshan.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.jain.temple.jainmandirdarshan.BuildConfig;
import com.jain.temple.jainmandirdarshan.util.SharedPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by prince on 6/2/2016.
 */
public class CustomRequestJsonRaw extends Request<JSONObject> {
    private int mMethod;
    private String mUrl;
    private JSONObject mParams;
    private Response.Listener<JSONObject> mListener;
    private String postString = null;
    private Context activity;



    public CustomRequestJsonRaw(Context activity, int method, String url, JSONObject params,
                                Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mMethod = method;
        this.mUrl = url;
        this.mParams = params;
        this.mListener = reponseListener;
        this.activity=activity;
        postString=params.toString();

        /*if (method == Method.POST && params != null && params.length() > 0) {
            setRetryPolicy(new DefaultRetryPolicy(12000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            postString = new GsonBuilder().create().toJson(params);
        }*/

        //   getUrl();
        //   parseNetworkResponse(reponseListener);
    }


    @Override
    public byte[] getBody() throws AuthFailureError {

        return postString != null ? postString.getBytes(Charset
                .forName("UTF-8")) : super.getBody();
    }


    @Override
    public String getUrl() {
        if(mMethod == Method.GET) {

        }

        Log.d("url", mUrl);
        return mUrl;
    }

  /* @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return mParams;
    };*/

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  mHeaders = new HashMap<String, String>();
        mHeaders.put("Content-Type", "application/json");
        mHeaders.put("Authorization", "key="+ BuildConfig.FcmServerKey);

        return mHeaders;
    }


    @Override
    public String getBodyContentType() {
        return postString !=null?"application/json; charset=utf-8":super.getBodyContentType();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {

            // response.put("Content-Type", "application/json");
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub

        mListener.onResponse(response);


    }
}


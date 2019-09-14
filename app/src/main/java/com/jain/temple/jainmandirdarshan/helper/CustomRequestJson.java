package com.jain.temple.jainmandirdarshan.helper;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by prince on 3/15/2016.
 */
public class CustomRequestJson extends Request<JSONObject> {
    private int mMethod;
    private String mUrl;
    private Map<String, String> mParams;
    private Response.Listener<JSONObject> mListener;
    private String postString = null;
    private Activity activity;
    private Map<String, String> mHeaders;


    public CustomRequestJson(Activity activity, int method, String url, Map<String, String> params, Map<String, String> mHeaders,
                             Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mMethod = method;
        this.mUrl = url;
        this.mParams = params;
        this.mHeaders=mHeaders;
        this.mListener = reponseListener;
        this.activity=activity;
        if (method == Method.POST && params != null && params.size() > 0) {
            setRetryPolicy(new DefaultRetryPolicy(12000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            postString = new GsonBuilder().create().toJson(params);
        }

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
            StringBuilder stringBuilder = new StringBuilder(mUrl);
            Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if(i == 1) {
                    stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
                } else {
                    stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                }
                iterator.remove(); // avoids a ConcurrentModificationException
                i++;
            }
            mUrl = stringBuilder.toString();
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
       /* Map<String, String>  params = new HashMap<String, String>();
        params.put("X-Token", "KLM@90op");*/
        // params.put("Content-Type", "application/json");

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

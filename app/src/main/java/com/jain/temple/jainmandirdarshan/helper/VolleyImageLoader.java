package com.jain.temple.jainmandirdarshan.helper;

import android.content.Context;
import android.graphics.Bitmap;


import androidx.collection.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Ashish on 1/6/2016.
 */
public class VolleyImageLoader {

    private static VolleyImageLoader mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    private VolleyImageLoader(final Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {

//                    Bitmap scaledBitmap = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo_catchiimg);
//                    int nh = (int) (scaledBitmap.getHeight() * (1024.0 / scaledBitmap.getWidth()));
//                    Bitmap scaled = Bitmap.createScaledBitmap(scaledBitmap, 1024, nh, true);

                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {

                        Bitmap bitmap = cache.get(url);

                        if(bitmap!=null) {
                            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                            bitmap = scaled;
                        }


                        return bitmap;
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        Bitmap thumbnail = bitmap;
                        int nh = (int) ( thumbnail.getHeight() * (512.0 / thumbnail.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(thumbnail,512, nh, true);

                        cache.put(url, scaled);
                    }
                });
    }

    public static synchronized VolleyImageLoader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyImageLoader(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}

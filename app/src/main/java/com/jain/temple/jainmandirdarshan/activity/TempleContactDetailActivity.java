package com.jain.temple.jainmandirdarshan.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.jain.temple.jainmandirdarshan.BuildConfig;
import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.SliderPagerAdapter;
import com.jain.temple.jainmandirdarshan.helper.CustomRequestJson;
import com.jain.temple.jainmandirdarshan.model.FavouriteModel;
import com.jain.temple.jainmandirdarshan.model.NearPlaceLatLngModel;
import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;
import com.jain.temple.jainmandirdarshan.util.AppController;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.OK;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.STATUS;

/**
 * Created by admin on 7/7/2017.
 */

public class TempleContactDetailActivity extends BaseActivity implements AdaptorClickInterface, View.OnClickListener, ListenerData {
    private TextView temple_name_tv, temple_address_tv, temple_contact_no_tv, temple_rating_tv;
    private TextView source_from, source_distance, source_time;
    private TextView destination_from, destination_distance, destination_time;
    private CoordinatorLayout mainCoordinatorLayout;
    private ImageView ic_call, ic_share;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    int page_position_full_screen = 0;
    private String callNo = "";
    private NearPlaceLatLngModel nearPlaceLatLngModel;
    private FavouriteModel favouriteModel;
    private boolean isFavorite;
    private Menu menu;
    private String LOG_TAG = "activity";
    private AppDatabase appDatabase  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact_temple);

        init();

        addBottomDots(0, ll_dots);

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);


    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);

        appDatabase= AppDatabase.getAppDatabase(getApplicationContext());
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        destination_from = (TextView) findViewById(R.id.destination_from);
        destination_distance = (TextView) findViewById(R.id.destination_distance);
        destination_time = (TextView) findViewById(R.id.destination_time);

        source_from = (TextView) findViewById(R.id.source_from);
        source_distance = (TextView) findViewById(R.id.source_distance);
        source_time = (TextView) findViewById(R.id.source_time);

        temple_name_tv = (TextView) findViewById(R.id.temple_name_tv);
        temple_address_tv = (TextView) findViewById(R.id.temple_address_tv);
        temple_contact_no_tv = (TextView) findViewById(R.id.temple_contact_no_tv);
        temple_rating_tv = (TextView) findViewById(R.id.temple_rating_tv);

        ic_call = (ImageView) findViewById(R.id.ic_call);
        ic_share = (ImageView) findViewById(R.id.ic_share);


        ic_call.setOnClickListener(this);
        ic_share.setOnClickListener(this);

        Intent intent = getIntent();

        nearPlaceLatLngModel = intent.getParcelableExtra("nearPlaceLatLngModel");
        LatLng destinationLatLng = intent.getParcelableExtra("destinationLatLng");
        LatLng sourcelatLng = intent.getParcelableExtra("sourceLatLng");


        if (destinationLatLng != null) {
            new UiUtils().makeCalculateDistanceRequest2(this, this, destinationLatLng, nearPlaceLatLngModel.getLatLng());
        }

        if (sourcelatLng != null) {
            makeCalculateDistanceRequest(sourcelatLng, nearPlaceLatLngModel.getLatLng());
        }

        getPlaceDetail();

        slider_image_list = new ArrayList<>();


        sliderPagerAdapter = new SliderPagerAdapter(TempleContactDetailActivity.this, slider_image_list, this, "root");
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, ll_dots);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    public void makeCalculateDistanceRequest(LatLng sourcelatLng, LatLng destlatLng) {

        StringBuilder googlePlacesUrl =
                new StringBuilder("http://maps.googleapis.com/maps/api/distancematrix/json?");
        googlePlacesUrl.append("origins=").append(sourcelatLng.latitude).append(",").append(sourcelatLng.longitude);
        googlePlacesUrl.append("&destinations=").append(destlatLng.latitude).append(",").append(destlatLng.longitude);
        googlePlacesUrl.append("&mode=driving").append("&language=en-EN");
        googlePlacesUrl.append("&sensor=false");


        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<String, String>();

        AppController.getInstance().addToRequestQueue(new CustomRequestJson(this, Request.Method.GET,
                googlePlacesUrl.toString(), params, mHeaders, dataResponse1,
                errorListener), "tag_calculate distance_req");
    }

    private Response.Listener<JSONObject> dataResponse1 = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            Log.d("responsedddddddddd", response.toString());


            try {
                JSONArray rowsArr = response.getJSONArray("rows");

                JSONObject rowJson = (JSONObject) rowsArr.get(0);
                JSONObject elementsJson = (JSONObject) rowJson.getJSONArray("elements").get(0);

                String distance = elementsJson.getJSONObject("distance").getString("text");

                String duration = elementsJson.getJSONObject("duration").getString("text");

                JSONArray origin_addresses = response.getJSONArray("origin_addresses");
                String address = origin_addresses.get(0).toString();

                source_from.setText(/*address +*/" Source Station");
                source_distance.setText(distance);
                source_time.setText(duration);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    };


    private void getPlaceDetail() {
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?placeid=");
        googlePlacesUrl.append(nearPlaceLatLngModel.getPlaceId()).append("&key=" + BuildConfig.BrowserApiKey);

        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<String, String>();

        AppController.getInstance().addToRequestQueue(new CustomRequestJson(TempleContactDetailActivity.this, Request.Method.GET,
                googlePlacesUrl.toString(), params, mHeaders, dataResponse,
                errorListener), "tag_logout_req");
    }

    private Response.Listener<JSONObject> dataResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            Log.d("responsedddddddddd", response.toString());

            parseLocationResult(response);


        }

    };

    private void parseLocationResult(JSONObject response) {

        String templeName, templeAddress, templeRating;
        try {


            if (response.getString(STATUS).equalsIgnoreCase(OK)) {

                JSONObject resultsJson = response.getJSONObject("result");
                if (resultsJson.has("photos")) {


                    JSONArray photosArr = resultsJson.getJSONArray("photos");

                    for (int i = 0; i < photosArr.length(); i++) {

                        JSONObject photosJson = (JSONObject) photosArr.get(i);
                        slider_image_list.add("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=" + photosJson.getString("photo_reference") + "&key=" + BuildConfig.BrowserApiKey);

                    }
                } else {
                    slider_image_list.add("");
                }

                sliderPagerAdapter.notifyDataSetChanged();
                if (resultsJson.has("formatted_phone_number")) {
                    callNo = resultsJson.getString("formatted_phone_number");
                }

                templeName = resultsJson.getString("name");
                if (resultsJson.has("rating")) {
                    templeRating = resultsJson.getString("rating");
                } else {
                    templeRating = "0";
                }

                temple_name_tv.setText(templeName);
                temple_address_tv.setText(resultsJson.getString("formatted_address"));
                temple_contact_no_tv.setText(callNo);
                temple_rating_tv.setText(templeRating);

                String pic_refrenceUrl = resultsJson.getString("reference");


                favouriteModel = new FavouriteModel();
                favouriteModel.setPlaceId(nearPlaceLatLngModel.getPlaceId());
                favouriteModel.setTempleName(templeName);
                favouriteModel.setTempleAddress(nearPlaceLatLngModel.getTempleAddress());
                favouriteModel.setTempleContactNo(callNo);
                favouriteModel.setTempleRating(templeRating);
                favouriteModel.setLatLng(nearPlaceLatLngModel.getLatLng());
                favouriteModel.setPicRefrence(nearPlaceLatLngModel.getPhotoRefrence());
                favouriteModel.setCategory("1");

                // favouriteModel.setPicRefrence("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=" + pic_refrenceUrl + "&key=" + GOOGLE_BROWSER_API_KEY);
                /*if (slider_image_list.size() > 0) {
                    favouriteModel.setPicRefrence();
                }

                else{
                    favouriteModel.setPicRefrence("");
                }
*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());

            if (error.networkResponse != null) {

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);

                    Toast.makeText(TempleContactDetailActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    //Handle a malformed json response
                } catch (UnsupportedEncodingException e) {

                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.detail_option_menu, menu);
        getFavourite();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wishList:
                if (isFavorite) {
                    deleteFavorite();
                } else {
                    saveFavourite();
                }

                break;
            case R.id.action_location:
                startActivity(new Intent(this, MapsActivityCurrentPlace.class).putExtra("data", nearPlaceLatLngModel));
                break;

            case R.id.action_navigation:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+nearPlaceLatLngModel.getLatLng().latitude+","+nearPlaceLatLngModel.getLatLng().longitude));
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private void deleteFavorite() {
        /*MyFavourateTempleEntity myFavourateTempleEntity=new MyFavourateTempleEntity();
        myFavourateTempleEntity.setTITLE(favouriteModel.getTitle());
        myFavourateTempleEntity.setDESCRIPTION(favouriteModel.getDescription());
        myFavourateTempleEntity.setCATEGORY(favouriteModel.getCategory());*/

        appDatabase.favouriteDao().deleteFavoriteTemple(favouriteModel.getTitle());

      //  db.deleteFavoriteTemple(favouriteModel);

        isFavorite = false;
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
    }

    private void saveFavourite() {

        MyFavourateTempleEntity myFavourateTempleEntity=new MyFavourateTempleEntity();
        myFavourateTempleEntity.setTITLE(favouriteModel.getTitle());
        myFavourateTempleEntity.setDESCRIPTION(favouriteModel.getDescription());
        myFavourateTempleEntity.setCATEGORY(favouriteModel.getCategory());
        myFavourateTempleEntity.setCONTACT_NO(favouriteModel.getTempleContactNo());
        myFavourateTempleEntity.setNAME(favouriteModel.getTempleName());
        myFavourateTempleEntity.setPLACE_ID(favouriteModel.getPlaceId());
        myFavourateTempleEntity.setADDRESS(favouriteModel.getTempleAddress());
        myFavourateTempleEntity.setRATING(favouriteModel.getTempleRating());
        myFavourateTempleEntity.setT_PIC(favouriteModel.getPicRefrenceUrl());
        myFavourateTempleEntity.setLATITUDE(favouriteModel.getLatLng().latitude+"");
        myFavourateTempleEntity.setLONGITUDE(favouriteModel.getLatLng().longitude+"");

        appDatabase.favouriteDao().addFavouriteTemple(myFavourateTempleEntity);

       // db.AddFavouriteTemple(favouriteModel);

        isFavorite = true;
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
    }


    private void addBottomDots(int currentPage, LinearLayout dots_layout) {
        dots = new TextView[slider_image_list.size()];

        dots_layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            dots_layout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void getitemClickPosition(Object o) {
        final int position = (int) o;

        page_position_full_screen = position;
        final Dialog dialog;
        dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.full_screen_inage_slider);
        final ViewPager bannerFlipperDetails = (ViewPager) dialog.findViewById(R.id.bannerFlipperdialog);
        final LinearLayout ll_dots_full_screen = (LinearLayout) dialog.findViewById(R.id.ll_dots_full_screen);
       /* CirclePageIndicator indicatorFlipper= (CirclePageIndicator) dialog.findViewById(R.id.indicatorFlipperdialog);

        indicatorFlipper.setFillColor(getActivity().getResources().getColor(R.color.text_deatils_color));
        indicatorFlipper.setPageColor(getActivity().getResources().getColor(R.color.yellow));
        indicatorFlipper.setRadius(14);*/

        ImageView dialog_cut_iv1 = (ImageView) dialog.findViewById(R.id.dialog_cut_iv);

        dialog_cut_iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        SliderPagerAdapter sliderPagerAdapter = new SliderPagerAdapter(this,
                slider_image_list, TempleContactDetailActivity.this, "");
        bannerFlipperDetails.setAdapter(sliderPagerAdapter);

        sliderPagerAdapter.notifyDataSetChanged();

        addBottomDots(position, ll_dots_full_screen);

        dialog.show();

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position_full_screen == slider_image_list.size()) {
                    page_position_full_screen = 0;
                } else {
                    page_position_full_screen = page_position_full_screen + 1;
                }
                bannerFlipperDetails.setCurrentItem(page_position_full_screen, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);


        bannerFlipperDetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, ll_dots_full_screen);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void launchCallActiivty() {

        if (!callNo.equalsIgnoreCase("")) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + callNo));
            if (ActivityCompat.checkSelfPermission(TempleContactDetailActivity.this,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        } else {
            final Snackbar snackbar = Snackbar.make(mainCoordinatorLayout, "No does not exit", Snackbar.LENGTH_SHORT);

            snackbar.show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_call:
                if (new UiUtils().isCallPermissionGrantedActivity(this)) {
                    launchCallActiivty();
                } else {

                }
                break;
            case R.id.ic_share:
                new UiUtils().callShareTempleInfo(this, favouriteModel);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            launchCallActiivty();
        }
    }

    @Override
    public void onItemSelected(View view, Object o) {
        try {
            JSONObject jsonObject = (JSONObject) o;

            JSONArray origin_addresses = jsonObject.getJSONArray("origin_addresses");
            String address = origin_addresses.get(0).toString();

            JSONArray rowsArr = jsonObject.getJSONArray("rows");

            JSONObject rowJson = (JSONObject) rowsArr.get(0);
            JSONObject elementsJson = (JSONObject) rowJson.getJSONArray("elements").get(0);

            String distance = elementsJson.getJSONObject("distance").getString("text");

            String duration = elementsJson.getJSONObject("duration").getString("text");

            destination_from.setText(/*address+*/" Destination Station");
            destination_distance.setText(distance);
            destination_time.setText(duration);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getFavourite() {
        MyFavourateTempleEntity myFavourateTempleEntity=appDatabase.favouriteDao().isFavouriteTemple(nearPlaceLatLngModel.getPlaceId());
       // DataBaseHelper db = new DataBaseHelper(this);
        // FavouriteModel favouriteModel = db.getFavourite(nearPlaceLatLngModel.getPlaceId());
     //   boolean isFavourite = db.isFavouriteTemple(nearPlaceLatLngModel.getPlaceId());

      if ( myFavourateTempleEntity!=null){
          isFavorite = true;
          menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
      }
      else{
          isFavorite = false;
          menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
      }

       /* if (isFavourite) {
            isFavorite = true;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
        } else {
            isFavorite = false;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }*/
    }

    @Override
    public void onPause() {
     /*   if (mAdView != null) {
            mAdView.pause();
        }*/
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
}

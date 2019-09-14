package com.jain.temple.jainmandirdarshan.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.jain.temple.jainmandirdarshan.BuildConfig;
import com.jain.temple.jainmandirdarshan.Fragment.TempleInListFragment;
import com.jain.temple.jainmandirdarshan.Fragment.TempleInMapFragment;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.CustomRequestJson;
import com.jain.temple.jainmandirdarshan.helper.DialogBoxHelper;
import com.jain.temple.jainmandirdarshan.helper.DirectionsJSONParser;
import com.jain.temple.jainmandirdarshan.model.MapDataModel;
import com.jain.temple.jainmandirdarshan.model.NearPlaceLatLngModel;
import com.jain.temple.jainmandirdarshan.util.AppController;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.FASTEST_INTERVAL_BW_UPDATES;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.GEOMETRY;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.ICON;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.LATITUDE;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.LOCATION;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.LONGITUDE;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.MIN_TIME_BW_UPDATES;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.NAME;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.OK;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.PLACE_ID;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.PROXIMITY_RADIUS;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.REFERENCE;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.STATUS;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.SUPERMARKET_ID;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.VICINITY;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.ZERO_RESULTS;

/**
 * Created by admin on 6/29/2017.
 */

public class FindTempleActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String TAG = "FindTempleActivity";
    private final int SOURCE_PLACE_PICKER_REQUEST = 10;
    private final int DEST_PLACE_PICKER_REQUEST = 2;
    private LocationManager locationManager;
    private CoordinatorLayout mainCoordinatorLayout;
    private LatLng sourselatlng, destinationLatLng;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Pager adapter;
    private MapDataModel mapDataModel;
    private String[] title;
    private HashMap<Integer, NearPlaceLatLngModel> sourcePlaceLatLngModelArrayList;
    private HashMap<Integer, NearPlaceLatLngModel> destPlaceLatLngModelArrayList;
    private boolean sourceClick;
    private int sourcePosition = 0;
    private int destPosition = 0;
    private boolean result1, result2;
    private DialogBoxHelper dialogBoxHelper;
    private GoogleApiClient mGoogleApiClient;
    final static int REQUEST_LOCATION = 199;
    private LinearLayout source_layout;
    private EditText destTv;
    public Marker nowMarker;
    private Location addressDestination;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_temple_find);

        inItView();
    }


    private void inItView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapDataModel = new MapDataModel();

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        title = new String[]{"Map", "List"};


        destPlaceLatLngModelArrayList = new HashMap<>();
        sourcePlaceLatLngModelArrayList = new HashMap<>();

        adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount(), title);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);

        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);
        source_layout = (LinearLayout) findViewById(R.id.source_layout);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // showLocationSettings();
        }
        PlaceAutocompleteFragment place_autocomplete_fragment_source = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_source);

        ImageView searchViewSource = (ImageView) place_autocomplete_fragment_source.getView().findViewById(R.id.place_autocomplete_search_button);
        searchViewSource.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dest));
        //searchView.set

        EditText sourceTv = (EditText) place_autocomplete_fragment_source.getView().findViewById(R.id.place_autocomplete_search_input);
        sourceTv.setHint("Destination Station");
        sourceTv.setTextSize(15.0f);
        // sourceTv.setPadding(10,-10,0,0);
        sourceTv.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_drawable));

        sourceTv.setTextColor(ContextCompat.getColor(this, R.color.black));
        sourceTv.setHintTextColor(ContextCompat.getColor(this, R.color.hintColor_text));

        PlaceAutocompleteFragment place_autocomplete_fragment_dest = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_dest);
        ImageView searchViewDest = (ImageView) place_autocomplete_fragment_dest.getView().findViewById(R.id.place_autocomplete_search_button);
        searchViewDest.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.start));


        destTv = (EditText) place_autocomplete_fragment_dest.getView().findViewById(R.id.place_autocomplete_search_input);
        destTv.setHint("Source Station");
        destTv.setTextSize(15.0f);
        destTv.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_drawable));
        destTv.setTextColor(ContextCompat.getColor(this, R.color.black));
        destTv.setHintTextColor(ContextCompat.getColor(this, R.color.hintColor_text));


        place_autocomplete_fragment_source.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                String toastMsg = String.format("Place: %s", place.getName());
                //  Toast.makeText(FindTempleActivity.this, toastMsg, Toast.LENGTH_LONG).show();

                result1 = false;
                result2 = false;

                sourceClick = true;
                sourcePosition = 0;
                sourcePlaceLatLngModelArrayList.clear();

                sourselatlng = place.getLatLng();

                if (new UiUtils().isNetworkAvailable(FindTempleActivity.this)) {
                    dialogBoxHelper = new DialogBoxHelper(FindTempleActivity.this);
                    dialogBoxHelper.showProgressDialog();

                    loadNearByPlaces("");
                    searchWithRankByDistance("");

                    if (sourselatlng != null && destinationLatLng != null) {

                        String url = getDirectionsUrl(sourselatlng, destinationLatLng);

                        getRouteDirection(url);
                    }
                } else {
                    new UiUtils().showSnackBarMessage(mainCoordinatorLayout, getString(R.string.address));
                }


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        place_autocomplete_fragment_dest.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                String toastMsg = String.format("Place: %s", place.getName());
                // Toast.makeText(FindTempleActivity.this, toastMsg, Toast.LENGTH_LONG).show();
                searchDestinationTemple(place.getLatLng());


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        ImageButton place_autocomplete_clear_button = place_autocomplete_fragment_dest.getView().findViewById(R.id.place_autocomplete_clear_button);
        place_autocomplete_clear_button.setVisibility(View.GONE);
        place_autocomplete_clear_button.setEnabled(false);

/*
        UiUtils  uiUtils=new UiUtils();

        uiUtils.showAlertDialog(this,getString(R.string.search_help),getString(R.string.app_name));*/

        showsnackBarMessage(getString(R.string.search_help));

    }


    private void loadNearByPlaces(String nextPageToken) {


        String type = "place_of_worship";
        String keyword = "Jain Mandir";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=");
        if (sourceClick) {
            googlePlacesUrl.append(sourselatlng.latitude).append(",").append(sourselatlng.longitude);
        } else {
            googlePlacesUrl.append(destinationLatLng.latitude).append(",").append(destinationLatLng.longitude);
        }
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=").append(type);
        if (!nextPageToken.equalsIgnoreCase("")) {
            googlePlacesUrl.append("&pagetoken=").append(nextPageToken);
        }
        try {
            googlePlacesUrl.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8").replace("+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        googlePlacesUrl.append("&key=" + BuildConfig.BrowserApiKey);

        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<>();


        AppController.getInstance().addToRequestQueue(new CustomRequestJson(FindTempleActivity.this, Request.Method.GET,
                googlePlacesUrl.toString(), params, mHeaders, dataResponseByRadius,
                errorListener), "tag_nearByLocation_req");

    }


    private void searchWithRankByDistance(String next_page_token) {
        String type = "place_of_worship";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=");
        if (sourceClick) {
            googlePlacesUrl.append(sourselatlng.latitude).append(",").append(sourselatlng.longitude);
        } else {
            googlePlacesUrl.append(destinationLatLng.latitude).append(",").append(destinationLatLng.longitude);
        }
        googlePlacesUrl.append("&rankby=distance");
        googlePlacesUrl.append("&type=").append(type);
        googlePlacesUrl.append("&key=" + BuildConfig.BrowserApiKey);
        if (!next_page_token.equalsIgnoreCase("")) {
            googlePlacesUrl.append("&pagetoken=").append(next_page_token);
        }

        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<String, String>();

        AppController.getInstance().addToRequestQueue(new CustomRequestJson(FindTempleActivity.this, Request.Method.GET,
                googlePlacesUrl.toString(), params, mHeaders, dataResponseRankByDistance,
                errorListener), "tag_search_by_distance_req");
    }

    private Response.Listener<JSONObject> dataResponseRankByDistance = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            Log.d("responseDistanceeeeeee", response.toString());

            parseLocationResultRankByDistance(response);


        }

    };


    private Response.Listener<JSONObject> dataResponseByRadius = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            Log.d("responseRadiussssss", response.toString());

            parseLocationResultByRadius(response);


        }

    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("response", error.toString());

            dialogBoxHelper.hideProgressDialog();

            try {
                if (error.networkResponse != null) {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);

                    Toast.makeText(FindTempleActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                //Handle a malformed json response
            } catch (UnsupportedEncodingException e) {

            }

        }
    };


    private void parseLocationResultByRadius(JSONObject result) {

        String next_page_token;
        try {
            JSONArray jsonArray = result.getJSONArray("results");
            String status = result.getString(STATUS);

            if (status.equalsIgnoreCase(OK)) {

                addDataonList(jsonArray);


                if (!result.isNull("next_page_token")) {
                    next_page_token = result.getString("next_page_token");
                    mapDataModel.setNext_page_token(next_page_token);

                    loadNearByPlaces(next_page_token);
                } else {

                    result2 = true;
                    mapDataModel.setDestinationNearPlaceLatLngModelList(destPlaceLatLngModelArrayList);
                    mapDataModel.setSourceNearPlaceLatLngModelList(sourcePlaceLatLngModelArrayList);

                    commitFragment();


                    //   dialogBoxHelper.hideProgressDialog();

                }
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                mapDataModel.setDestinationNearPlaceLatLngModelList(destPlaceLatLngModelArrayList);
                mapDataModel.setSourceNearPlaceLatLngModelList(sourcePlaceLatLngModelArrayList);
                result2 = true;
                commitFragment();


            } else if (status.equalsIgnoreCase("INVALID_REQUEST")) {
                mapDataModel.setDestinationNearPlaceLatLngModelList(destPlaceLatLngModelArrayList);
                mapDataModel.setSourceNearPlaceLatLngModelList(sourcePlaceLatLngModelArrayList);
                result2 = true;


                commitFragment();


            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }


    }

    private void parseLocationResultRankByDistance(JSONObject result) {

        String next_page_token;
        try {
            JSONArray jsonArray = result.getJSONArray("results");
            String status = result.getString(STATUS);

            if (status.equalsIgnoreCase(OK)) {

                addDataonList(jsonArray);


                if (!result.isNull("next_page_token")) {
                    next_page_token = result.getString("next_page_token");
                    mapDataModel.setNext_page_token(next_page_token);

                    searchWithRankByDistance(next_page_token);
                } else {


                    mapDataModel.setDestinationNearPlaceLatLngModelList(destPlaceLatLngModelArrayList);
                    mapDataModel.setSourceNearPlaceLatLngModelList(sourcePlaceLatLngModelArrayList);
                    result1 = true;
                    commitFragment();

                    //  dialogBoxHelper.hideProgressDialog();

                }
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                mapDataModel.setDestinationNearPlaceLatLngModelList(destPlaceLatLngModelArrayList);
                mapDataModel.setSourceNearPlaceLatLngModelList(sourcePlaceLatLngModelArrayList);
                result1 = true;
                commitFragment();


            } else if (status.equalsIgnoreCase("INVALID_REQUEST")) {
                mapDataModel.setDestinationNearPlaceLatLngModelList(destPlaceLatLngModelArrayList);
                mapDataModel.setSourceNearPlaceLatLngModelList(sourcePlaceLatLngModelArrayList);

                result1 = true;
                commitFragment();


            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }


    }


    public void addDataonList(JSONArray jsonArray) {
        String id, place_id, placeName = null, reference, icon, vicinity = null, next_page_token = "";
        double latitude, longitude;

        try {

            LatLng latLng;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject place = null;
                boolean alreadyExitPlace = false;
                place = jsonArray.getJSONObject(i);

                NearPlaceLatLngModel nearPlaceLatLngModel = new NearPlaceLatLngModel();

                String name = place.getString(NAME).toLowerCase();
                if (name.contains("jain temple") || name.contains("jain mandir") || name.contains("jain derasar") || name.contains("jain chaityalaya")) {
                    placeName = place.getString(NAME);
                    nearPlaceLatLngModel.setTempleName(placeName);

                    if (place.has("photos")) {

                        JSONArray photosArr = place.getJSONArray("photos");

                        JSONObject photosJson = (JSONObject) photosArr.get(0);
                        String photo_reference = photosJson.getString("photo_reference");
                        nearPlaceLatLngModel.setPhotoRefrence("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=" + photo_reference + "&key=" + BuildConfig.BrowserApiKey);

                    } else {

                        nearPlaceLatLngModel.setPhotoRefrence(place.getString(ICON));
                    }

                    id = place.getString(SUPERMARKET_ID);
                    place_id = place.getString(PLACE_ID);

                    nearPlaceLatLngModel.setPlaceId(place_id);


                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);

                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);


                    latLng = new LatLng(latitude, longitude);

                    nearPlaceLatLngModel.setTempleAddress(vicinity);

                    nearPlaceLatLngModel.setLatLng(latLng);

                    nearPlaceLatLngModel.setTitle(placeName /*+ " : " + vicinity*/);
                    if (sourceClick) {

                        for (Map.Entry entry : destPlaceLatLngModelArrayList.entrySet()) {
                            NearPlaceLatLngModel nearPlaceLatLngModel1 = (NearPlaceLatLngModel) entry.getValue();
                            if (nearPlaceLatLngModel1.getPlaceId().equalsIgnoreCase(place_id)) {
                                alreadyExitPlace = true;
                            }
                        }


                        for (Map.Entry entry : sourcePlaceLatLngModelArrayList.entrySet()) {
                            NearPlaceLatLngModel nearPlaceLatLngModel1 = (NearPlaceLatLngModel) entry.getValue();
                            if (nearPlaceLatLngModel1.getPlaceId().equalsIgnoreCase(place_id)) {
                                alreadyExitPlace = true;
                            }
                        }
                        if (!alreadyExitPlace) {
                            sourcePlaceLatLngModelArrayList.put(destPlaceLatLngModelArrayList.size() + sourcePosition, nearPlaceLatLngModel);
                            sourcePosition++;
                        }

                    } else {
                        for (Map.Entry entry : destPlaceLatLngModelArrayList.entrySet()) {
                            NearPlaceLatLngModel nearPlaceLatLngModel1 = (NearPlaceLatLngModel) entry.getValue();
                            if (nearPlaceLatLngModel1.getPlaceId().equalsIgnoreCase(place_id)) {
                                alreadyExitPlace = true;
                            }
                        }


                        for (Map.Entry entry : sourcePlaceLatLngModelArrayList.entrySet()) {
                            NearPlaceLatLngModel nearPlaceLatLngModel1 = (NearPlaceLatLngModel) entry.getValue();
                            if (nearPlaceLatLngModel1.getPlaceId().equalsIgnoreCase(place_id)) {
                                alreadyExitPlace = true;
                            }
                        }

                        if (!alreadyExitPlace) {

                            destPlaceLatLngModelArrayList.put(destPosition, nearPlaceLatLngModel);
                            destPosition++;
                        }
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void commitFragment() {

        if (result2 && result1) {

            viewPager.setCurrentItem(0);

            adapter.notifyDataSetChanged();

            dialogBoxHelper.hideProgressDialog();
        }

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
      /*  dialogBoxHelper = new DialogBoxHelper(FindTempleActivity.this);
        dialogBoxHelper.showProgressDialog();*/

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service


       // return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key="+getString(R.string.map_direction_key);

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+"&key="+ BuildConfig.ApiKey;
    }

    public void getRouteDirection(String url) {
        //YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works

        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<String, String>();

        AppController.getInstance().addToRequestQueue(new CustomRequestJson(FindTempleActivity.this, Request.Method.GET,
                url, params, mHeaders, routeResponse,
                errorListener), "tag_logout_req");

    }

    private Response.Listener<JSONObject> routeResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            Log.d("responsedddddddddd", response.toString());

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(response.toString());


        }

    };

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                TempleInMapFragment.newInstance().addMarkerOnMap(mapDataModel);
                break;
            case 1:
                TempleInListFragment.newInstance().showOnList(mapDataModel);
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            //  dialogBoxHelper.hideProgressDialog();

            mapDataModel.setPolyLineResult(result);
            mapDataModel.setSourcelatLng(sourselatlng);
            mapDataModel.setTitle("Destination Address");


            TempleInMapFragment.newInstance().drawPath(mapDataModel);

            commitFragment();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        GetUserLocation();//FINALLY YOUR OWN METHOD TO GET YOUR USER LOCATION HERE
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "Enable your location (GPS) to use this Feature", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                    default:
                        break;
                }
                break;

            case DEST_PLACE_PICKER_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Place place = PlacePicker.getPlace(data, this);
                        String toastMsg = String.format("Place: %s", place.getName());
                        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();


                        sourselatlng = place.getLatLng();
                        loadNearByPlaces("");


                        mapDataModel.setPlace(place);

                        //addMarkerOnMap(place);

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        break;
                    default:
                        break;
                }
                break;

            case SOURCE_PLACE_PICKER_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Place place = PlacePicker.getPlace(data, this);
                        String toastMsg = String.format("Place: %s", place.getName());
                        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                        if (sourselatlng != null && destinationLatLng != null) {

                            String url = getDirectionsUrl(new LatLng(sourselatlng.latitude, sourselatlng.longitude), place.getLatLng());

                            getRouteDirection(url);
                        }

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to

                        break;
                    default:
                        break;
                }
                break;

        }


    }

    public void checkGps() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();

       // LocationRequest locationRequest = LocationRequest.create();


        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL_BW_UPDATES);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .setNeedBle(true)
               /* .addLocationRequest(mLocationRequestHighAccuracy)
                .addLocationRequest(mLocationRequestBalancedPowerAccuracy)*/;

      /*  PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());*/

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        FindTempleActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });

    }


    private void GetUserLocation() {

        // startActivity(new Intent(MainActivity.this,FindPumpNearMeActivity.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    class Pager extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;
        String[] title;

        //Constructor to the class
        public Pager(FragmentManager fm, int tabCount, String[] title) {
            super(fm);
            //Initializing tab count
            this.tabCount = tabCount;
            this.title = title;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    TempleInMapFragment templeInMapFragment = new TempleInMapFragment().newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", mapDataModel);
                    templeInMapFragment.setArguments(bundle);
                    return templeInMapFragment;
                case 1:
                    TempleInListFragment templeInListFragment = new TempleInListFragment().newInstance();
                    templeInListFragment.showOnList(mapDataModel);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("data", mapDataModel);
                    templeInListFragment.setArguments(bundle1);
                    return templeInListFragment;

                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return title[position];
                case 1:
                    return title[position];

                default:
                    return null;
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void showsnackBarMessage(String msg) {
        final Snackbar snackbar = Snackbar.make(mainCoordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    public void setAddressDestination(Location addressDestination) {
        /*this.addressDestination = addressDestination;
        dialogBoxHelper = new DialogBoxHelper(this);
        dialogBoxHelper.showProgressDialog();

        String url = "http://maps.google.com/maps/api/geocode/json?latlng=" + addressDestination.getLatitude() + "," + addressDestination.getLongitude() + "&sensor=true";

        Map<String, String> params = new LinkedHashMap<>();

        Map<String, String> mHeaders = new HashMap<>();


        AppController.getInstance().addToRequestQueue(new CustomRequestJson(FindTempleActivity.this, Request.Method.GET,
                url, params, mHeaders, currentLocationResponse,
                errorListener), "tag_geocoder_req");*/

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(addressDestination.getLatitude(), addressDestination.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();


            destTv.setText(address);
            searchDestinationTemple(new LatLng(addressDestination.getLatitude(), addressDestination.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void searchDestinationTemple(LatLng latLng) {
        result1 = false;
        result2 = false;

        sourceClick = false;
        destPosition = 0;

        destPlaceLatLngModelArrayList.clear();

        destinationLatLng = latLng;
        mapDataModel.setDestlatLng(destinationLatLng);

        source_layout.setVisibility(View.VISIBLE);

        dialogBoxHelper = new DialogBoxHelper(FindTempleActivity.this);
        dialogBoxHelper.showProgressDialog();

        loadNearByPlaces("");
        searchWithRankByDistance("");

        if (sourselatlng != null && destinationLatLng != null) {
            String url = getDirectionsUrl(sourselatlng, destinationLatLng);

            getRouteDirection(url);
        }

    }

    private Response.Listener<JSONObject> currentLocationResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            dialogBoxHelper.hideProgressDialog();

            Log.d("responseRadiussssss", response.toString());

            try {
                JSONArray resultsArr = response.getJSONArray("results");
                JSONObject jsonObject = (JSONObject) resultsArr.get(0);
                String formatted_address = jsonObject.getString("formatted_address");
                destTv.setText(formatted_address);
                searchDestinationTemple(new LatLng(addressDestination.getLatitude(), addressDestination.getLongitude()));


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    };

    @Override
    public void onResume() {
        super.onResume();

        checkGps();
    }
}

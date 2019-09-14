package com.jain.temple.jainmandirdarshan.activity

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener
import com.jain.temple.jainmandirdarshan.R
import com.jain.temple.jainmandirdarshan.helper.CustomRequestJson
import com.jain.temple.jainmandirdarshan.helper.DialogBoxHelper
import com.jain.temple.jainmandirdarshan.util.AppConfig.FASTEST_INTERVAL_BW_UPDATES
import com.jain.temple.jainmandirdarshan.util.AppConfig.MIN_TIME_BW_UPDATES
import com.jain.temple.jainmandirdarshan.util.AppController
import com.jain.temple.jainmandirdarshan.util.PermissionUtils
import com.jain.temple.jainmandirdarshan.util.UiUtils
import kotlinx.android.synthetic.main.acticity_find_sunset.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

class SunsetSunriseActvity : BaseActivity(),GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener{
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private val TAG = "FindPlaceActivity"
    private var dialogBoxHelper: DialogBoxHelper? = null
    var destTv: EditText? = null
    //  var placeName: CharSequence? = null
    var sourselatlng: LatLng? = null
    protected val REQUEST_CHECK_SETTINGS = 0x1
   private var mGoogleApiClient: GoogleApiClient? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    /**
     * Provide callbacks for location events.
     */
    private var mLocationCallback: LocationCallback? = null
    private var provider: String? = ""
    /**
     * An object representing the current location
     */
    private var mCurrentLocation: Location? = null
    /**
     * FusedLocationProviderApi Save request parameters
     */
    private var mLocationRequest: LocationRequest? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticity_find_sunset)

        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar as Toolbar?)
        val actionBar = supportActionBar/*.setDisplayHomeAsUpEnabled(true)*/
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        provider = intent.getStringExtra("provider")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkMyPermissionLocation();
        } else {
            initGoogleMapLocation();
        }

        var place_autocomplete_fragment: PlaceAutocompleteFragment? = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        destTv = place_autocomplete_fragment!!.getView()!!.findViewById(R.id.place_autocomplete_search_input) as EditText
        destTv!!.setHint("Enter your Place, City")
        destTv!!.setTextSize(15.0f)
        //  destTv!!.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_drawable))
        destTv!!.setTextColor(ContextCompat.getColor(this, R.color.black))
        destTv!!.setHintTextColor(ContextCompat.getColor(this, R.color.hintColor_text))



        place_autocomplete_fragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                var placeName: CharSequence? = place.name
                var address = place.address
                Log.i(TAG, "Place: " + place.name)
                val toastMsg = String.format("Place: %s", place.name)
                sourselatlng = place.latLng

                if (UiUtils().isNetworkAvailable(this@SunsetSunriseActvity)) {
                    dialogBoxHelper = DialogBoxHelper(this@SunsetSunriseActvity)
                    dialogBoxHelper!!.showProgressDialog()

                    if (sourselatlng != null) {
                        callTodayService(sourselatlng!!)

                    }
                } else {
                    // UiUtils().showSnackBarMessage(mainCoordinatorLayout, getString(R.string.address))
                }


            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status)
            }
        })

    }


    private fun checkMyPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission Check
            PermissionUtils.requestPermission(this);
        } else {
            //If you're authorized, start setting your location
            initGoogleMapLocation();
        }
    }

    private fun callTodayService(sourselatlng: LatLng): Unit {
        val basePath = "https://api.sunrise-sunset.org/json"

        val headers = LinkedHashMap<String, String>()

        //    val service = ServiceInterfaceMap()

        val params = java.util.LinkedHashMap<String, String>()
        params.put("lat", sourselatlng.latitude.toString())
        params.put("lng", sourselatlng.longitude.toString())
        params.put("date", "today")

        val mHeaders = HashMap<String, String>()

        AppController.getInstance().addToRequestQueue(CustomRequestJson(this@SunsetSunriseActvity, Request.Method.GET,
                basePath, params, mHeaders, dataResponseToday,
                errorListener), "tag_nearByLocation_req")

        //    var req= CustomRequestString(this, Request.Method.GET,basePath,params,headers,loginResponse,errorListener)

    }

    private val dataResponseToday = Response.Listener<JSONObject> { response ->
        dialogBoxHelper!!.hideProgressDialog()
        Log.d("responseRadiussssss", response.toString())

        if (response.getString("status").equals("OK")) {
            var resultsJson: JSONObject = response.getJSONObject("results");

            val c: Calendar = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            val df: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
            var formattedDate: String = df.format(c.getTime()) + " ";

            var obj = UiUtils()
            var sunrise: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("sunrise"))
            var sunset: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("sunset"))
            var solar_noon: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("solar_noon"))
            var day_length: String = resultsJson.getString("day_length")
            var civil_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("civil_twilight_begin"))
            var civil_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("civil_twilight_end"))

            var nautical_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("nautical_twilight_begin"))
            var nautical_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("nautical_twilight_end"))
            var astronomical_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("astronomical_twilight_begin"))
            var astronomical_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("astronomical_twilight_end"))


            today_tv.setText("Today: " + formattedDate)
            today_day_length_tv.setText("Day length :" + day_length + " hrs")

            sunrise_time_tv.setText(sunrise)
            sunset_time_tv.setText(sunset)

            c.setTime(df.parse(formattedDate))
            c.add(Calendar.DATE, -1);
            var yesterday: Date = c.getTime();


            val yesterdayDate = df.format(yesterday)


            Log.e("dayyyyyy", yesterday.toString())

            callYesterdayService(sourselatlng!!, yesterdayDate)

        }

    }

    internal var errorListener: Response.ErrorListener = Response.ErrorListener { error ->
        Log.d("response", error.toString())

        dialogBoxHelper!!.hideProgressDialog()

        try {
            if (error.networkResponse != null) {
                val responseBody = String(error.networkResponse.data, Charsets.UTF_8)
                val jsonObject = JSONObject(responseBody)

                Toast.makeText(this@SunsetSunriseActvity, jsonObject.getString("error"), Toast.LENGTH_LONG).show()

            }
        } catch (e: JSONException) {
            //Handle a malformed json response
        } catch (e: UnsupportedEncodingException) {

        }
    }

    private fun callYesterdayService(sourselatlng: LatLng, yesterdayDate: String): Unit {
        val basePath = "https://api.sunrise-sunset.org/json"

        val headers = LinkedHashMap<String, String>()

        //    val service = ServiceInterfaceMap()

        val params = java.util.LinkedHashMap<String, String>()
        params.put("lat", sourselatlng.latitude.toString())
        params.put("lng", sourselatlng.longitude.toString())
        params.put("date", yesterdayDate)

        val mHeaders = HashMap<String, String>()

        AppController.getInstance().addToRequestQueue(CustomRequestJson(this@SunsetSunriseActvity, Request.Method.GET,
                basePath, params, mHeaders, dataResponseYesterday,
                errorListener), "tag_nearByLocation_req")

        //    var req= CustomRequestString(this, Request.Method.GET,basePath,params,headers,loginResponse,errorListener)

    }

    private val dataResponseYesterday = Response.Listener<JSONObject> { response ->
        dialogBoxHelper!!.hideProgressDialog()
        Log.d("responseRadiussssss", response.toString())

        if (response.getString("status").equals("OK")) {
            var resultsJson: JSONObject = response.getJSONObject("results");

            val c: Calendar = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            System.out.println("Current time => " + c.getTime());

            val df: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
            var formattedDate: String = df.format(c.getTime()) + " ";

            var obj = UiUtils()
            var sunrise: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("sunrise"))
            var sunset: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("sunset"))
            var solar_noon: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("solar_noon"))
            var day_length: String = resultsJson.getString("day_length")
            var civil_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("civil_twilight_begin"))
            var civil_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("civil_twilight_end"))

            var nautical_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("nautical_twilight_begin"))
            var nautical_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("nautical_twilight_end"))
            var astronomical_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("astronomical_twilight_begin"))
            var astronomical_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("astronomical_twilight_end"))


            yesterday_tv.setText("Yesterday: " + formattedDate)
            yesterday_day_length.setText("Day length :" + day_length + " hrs")

            yesterday_sunrise_tv.setText(sunrise)
            yesterday_sunset_tv.setText(sunset)

            c.setTime(df.parse(formattedDate))
            c.add(Calendar.DATE, 2);
            var tomorrow: Date = c.getTime();


            val tomorrowDate = df.format(tomorrow)


            Log.e("dayyyyyy", tomorrow.toString())

            callTomorrowService(sourselatlng!!, tomorrowDate)

        }

    }


    private fun callTomorrowService(sourselatlng: LatLng, tomorrowDate: String): Unit {
        val basePath = "https://api.sunrise-sunset.org/json"

        val headers = LinkedHashMap<String, String>()

        //    val service = ServiceInterfaceMap()

        val params = java.util.LinkedHashMap<String, String>()
        params.put("lat", sourselatlng.latitude.toString())
        params.put("lng", sourselatlng.longitude.toString())
        params.put("date", tomorrowDate)

        val mHeaders = HashMap<String, String>()

        AppController.getInstance().addToRequestQueue(CustomRequestJson(this@SunsetSunriseActvity, Request.Method.GET,
                basePath, params, mHeaders, dataResponseTomorrow,
                errorListener), "tag_nearByLocation_req")

        //    var req= CustomRequestString(this, Request.Method.GET,basePath,params,headers,loginResponse,errorListener)

    }

    private val dataResponseTomorrow = Response.Listener<JSONObject> { response ->
        dialogBoxHelper!!.hideProgressDialog()
        Log.d("responseRadiussssss", response.toString())

        if (response.getString("status").equals("OK")) {
            var resultsJson: JSONObject = response.getJSONObject("results");

            val c: Calendar = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            System.out.println("Current time => " + c.getTime());

            val df: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
            var formattedDate: String = df.format(c.getTime()) + " ";

            var obj = UiUtils()
            var sunrise: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("sunrise"))
            var sunset: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("sunset"))
            var solar_noon: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("solar_noon"))
            var day_length: String = resultsJson.getString("day_length")
            var civil_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("civil_twilight_begin"))
            var civil_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("civil_twilight_end"))

            var nautical_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("nautical_twilight_begin"))
            var nautical_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("nautical_twilight_end"))
            var astronomical_twilight_begin: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("astronomical_twilight_begin"))
            var astronomical_twilight_end: String = obj.convertDateUTCtoLocal(formattedDate + resultsJson.getString("astronomical_twilight_end"))


            tomorrow_tv.setText("Tomorrow: " + formattedDate)
            tomorrow_day_length.setText("Day length :" + day_length + " hrs")

            tomorrow_sunrise_tv.setText(sunrise)
            tomorrow_sunset_tv.setText(sunset)

            layout_whole.visibility = View.VISIBLE


        }

    }

    fun checkGps() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }

        mGoogleApiClient?.connect()

        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = MIN_TIME_BW_UPDATES
        mLocationRequest.fastestInterval = FASTEST_INTERVAL_BW_UPDATES
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)

        val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())

        result.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            val LS_state = locationSettingsResult.locationSettingsStates
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS ->
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                    GetUserLocation()
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(this@SunsetSunriseActvity, REQUEST_CHECK_SETTINGS)

                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }// Location settings are not satisfied. However, we have no way to fix the
            // settings so we won't show the dialog.
        }
    }

    private fun GetUserLocation() {

    }

    override fun onConnected(bundle: Bundle?) {


    }

    override fun onConnectionSuspended(i: Int) {

    }

    public override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop fired ..............")
       // mGoogleApiClient?.disconnect()
        if (mFusedLocationClient != null) {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback);
        }
    }

    public override fun onPause() {
        super.onPause()

    }

    /*public override fun onResume() {
        super.onResume()

        checkGps()
    }*/

    private fun initGoogleMapLocation() {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            /**
             * Location Setting API to
             */
            val mSettingsClient = LocationServices.getSettingsClient(this)
            /*
         * Callback returning location result
         */
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    super.onLocationResult(result)
                    //mCurrentLocation = locationResult.getLastLocation();
                    mCurrentLocation = result!!.locations[0]


                    if (mCurrentLocation != null) {
                        Log.e("Location(Lat)==", "" + mCurrentLocation!!.latitude)
                        Log.e("Location(Long)==", "" + mCurrentLocation!!.longitude)

                        sourselatlng = LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude)


                        var geocoder: Geocoder = Geocoder(this@SunsetSunriseActvity, Locale.getDefault())
                        var addresses: List<Address>;
                        addresses = geocoder.getFromLocation(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        if (addresses.isNotEmpty()) {
                        var address: String = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        var city: String = addresses.get(0).locality;
                        var state: String = addresses.get(0).adminArea;
                        var country: String = addresses.get(0).countryName;
                        var postalCode: String = addresses.get(0).postalCode;
                        var knownName: String = addresses.get(0).featureName;

                        destTv?.setText(city + ", " + state + ", " + country + ", " + postalCode)

                        if (UiUtils().isNetworkAvailable(this@SunsetSunriseActvity)) {
                            dialogBoxHelper = DialogBoxHelper(this@SunsetSunriseActvity)
                            dialogBoxHelper!!.showProgressDialog()

                            if (sourselatlng != null) {
                                callTodayService(sourselatlng!!)

                            }
                        } else {
                            // UiUtils().showSnackBarMessage(mainCoordinatorLayout, getString(R.string.address))
                        }
                    }
                    }

                    /**
                     * To get location information consistently
                     * mLocationRequest.setNumUpdates(1) Commented out
                     * Uncomment the code below
                     */
                    mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!)
                }

                //Locatio nMeaning that all relevant information is available
                override fun onLocationAvailability(availability: LocationAvailability?) {
                    //boolean isLocation = availability.isLocationAvailable();
                }
            }
            mLocationRequest = LocationRequest()
            mLocationRequest!!.interval = 5000
            mLocationRequest!!.fastestInterval = 5000
            //To get location information only once here
            mLocationRequest!!.numUpdates = 3
            /* if (provider!!.equals(LocationManager.GPS_PROVIDER)) {
            //Accuracy is a top priority regardless of battery consumption
            mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        } else {*/
            //Acquired location information based on balance of battery and accuracy (somewhat higher accuracy)
            mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            //  }

            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest!!)
            /**
             * Stores the type of location service the client wants to use. Also used for positioning.
             */
            val mLocationSettingsRequest = builder.build()

            val locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            locationResponse.addOnSuccessListener(this, OnSuccessListener {
                Log.e("Response", "Successful acquisition of location information!!")
                //
                if (ActivityCompat.checkSelfPermission(this@SunsetSunriseActvity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return@OnSuccessListener
                }
                mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback!!, Looper.myLooper())
            })
            //When the location information is not set and acquired, callback
            locationResponse.addOnFailureListener(this) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> Log.e("onFailure", "Location environment check")
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Check location setting"
                        Log.e("onFailure", errorMessage)
                    }
                }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //If the request code does not match
        if (requestCode != PermissionUtils.REQUEST_CODE) {
            return;
        }
        if (PermissionUtils.isPermissionGranted(arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), grantResults)) {
            //If you have permission, go to the code to get the location value
            initGoogleMapLocation();
        } else {
            Toast.makeText(this, "Stop apps without permission to use location information", Toast.LENGTH_SHORT).show();
            //finish();
        }

    }

}
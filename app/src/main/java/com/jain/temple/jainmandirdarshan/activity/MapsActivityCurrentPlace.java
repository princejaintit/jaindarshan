package com.jain.temple.jainmandirdarshan.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jain.temple.jainmandirdarshan.Fragment.TempleInMapFragment;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.NearPlaceLatLngModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.MIN_DISTANCE_CHANGE_FOR_UPDATES;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.MIN_TIME_BW_UPDATES;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends BaseActivity
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener {

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private static TempleInMapFragment f;
    public GoogleMap mMap;
    private LocationManager locationManager;
    private Bundle bundle;
    private GoogleApiClient mGoogleApiClient;
    private CoordinatorLayout mainCoordinatorLayout;
    private  NearPlaceLatLngModel nearPlaceLatLngModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);


        inItView();

    }

    private void inItView() {



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);
        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


    }

    public void addMarkerOnMap(NearPlaceLatLngModel nearPlaceLatLngModel) {


        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(nearPlaceLatLngModel.getLatLng());
        markerOptions.title(nearPlaceLatLngModel.getTitle());
        markerOptions.snippet(nearPlaceLatLngModel.getTempleAddress());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_dt));

       // mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivityCurrentPlace.this));
        mMap.addMarker(markerOptions);

        CameraUpdate center =
                CameraUpdateFactory.newLatLng(nearPlaceLatLngModel.getLatLng());
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);

        mMap.animateCamera(zoom);

        // mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);



            Intent intent = getIntent();

            nearPlaceLatLngModel = intent.getParcelableExtra("data");

            addMarkerOnMap(nearPlaceLatLngModel);

            new UiUtils().showSnackBarMessageInfinite(mainCoordinatorLayout, nearPlaceLatLngModel.getTempleName() + "\n" + nearPlaceLatLngModel.getTempleAddress());

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    return false;
                }
            });
        } else {
            ActivityCompat.requestPermissions(MapsActivityCurrentPlace.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
        }

    }


    private void showCurrentLocation() {
        Criteria criteria = new Criteria();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

           Location  location = locationManager.getLastKnownLocation(bestProvider);

            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(bestProvider, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, MapsActivityCurrentPlace.this);
        } else {

            ActivityCompat.requestPermissions(MapsActivityCurrentPlace.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
        }


    }


    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //  loadNearByPlaces(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);

                showCurrentLocation();
            }
        } else if (requestCode == 102) {
            showCurrentLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_option_menu, menu);
        menu.findItem(R.id.action_wishList).setVisible(false);
        menu.findItem(R.id.action_location).setVisible(false);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_navigation:
                if (nearPlaceLatLngModel!=null) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + nearPlaceLatLngModel.getLatLng().latitude + "," + nearPlaceLatLngModel.getLatLng().longitude));
                    startActivity(intent);
                }
                    break;


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause() {

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

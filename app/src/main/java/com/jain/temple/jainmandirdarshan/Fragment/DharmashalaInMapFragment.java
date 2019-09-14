package com.jain.temple.jainmandirdarshan.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.FindDharamshalaActivity;
import com.jain.temple.jainmandirdarshan.activity.FindTempleActivity;
import com.jain.temple.jainmandirdarshan.activity.TempleContactDetailActivity;
import com.jain.temple.jainmandirdarshan.adaptor.CustomInfoWindowAdapter;
import com.jain.temple.jainmandirdarshan.model.MapDataModel;
import com.jain.temple.jainmandirdarshan.model.NearPlaceLatLngModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.FASTEST_INTERVAL_BW_UPDATES;
import static com.jain.temple.jainmandirdarshan.util.AppConfig.MIN_TIME_BW_UPDATES;

/**
 * Created by admin on 7/4/2017.
 */

public class DharmashalaInMapFragment extends Fragment implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdaptorClickInterface {
    private static final String TAG = "Map";
    private CoordinatorLayout coordinatorLayout;
    private static DharmashalaInMapFragment f;
    public GoogleMap mMap;
    private Bundle bundle;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private Marker destMarker, sourceMarker;
    private LatLng sourceLatLng, destinationLatLng;
    private Polyline polylineFinal;
    private HashMap<Integer, NearPlaceLatLngModel> destLatLngModelArrayList;
    private MapDataModel mapDataModel;

    public DharmashalaInMapFragment() {
    }

    public static DharmashalaInMapFragment newInstance() {

        if (f == null) {

            f = new DharmashalaInMapFragment();
        }

        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        inItView(rootView);
        return rootView;
    }

    private void inItView(View rootView) {

        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        if (!isGooglePlayServicesAvailable()) {
            getActivity().finish();
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            createLocationRequest();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        bundle = getArguments();
        if (bundle != null) {
            mapDataModel = bundle.getParcelable("data");

            addMarkerOnMap(mapDataModel);
        }
    }

    public void addMarkerOnMap(MapDataModel mapDataModel) {

        destLatLngModelArrayList = mapDataModel.getDestinationNearPlaceLatLngModelList();

        if (destLatLngModelArrayList != null) {
            HashMap<Integer, NearPlaceLatLngModel> sourceLatLngModelArrayList = mapDataModel.getSourceNearPlaceLatLngModelList();
            if (sourceLatLngModelArrayList != null) {
                destLatLngModelArrayList.putAll(sourceLatLngModelArrayList);
            }


            for (Map.Entry m : destLatLngModelArrayList.entrySet()) {
                NearPlaceLatLngModel nearPlaceLatLngModel = (NearPlaceLatLngModel) m.getValue();

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(nearPlaceLatLngModel.getLatLng());
                markerOptions.title(nearPlaceLatLngModel.getTitle());
                markerOptions.snippet(nearPlaceLatLngModel.getTempleAddress());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_dt));

                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
                mMap.addMarker(markerOptions);
            }
        }

        destinationLatLng = mapDataModel.getDestlatLng();
        if (destinationLatLng != null) {

            if (destMarker != null) {
                destMarker.remove();
            }

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(destinationLatLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(destinationLatLng);
            markerOptions.title("Source address");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));

            destMarker = mMap.addMarker(markerOptions);

        }

        else if (mapDataModel.getSourcelatLng()!=null){
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng( mapDataModel.getSourcelatLng());
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(4);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }

        if (destLatLngModelArrayList != null) {

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng( destLatLngModelArrayList.get(0).getLatLng());
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            Log.e("ssssssssssssss", destLatLngModelArrayList.size() + "                   " + "");


            Toast.makeText(getActivity(), destLatLngModelArrayList.size() + " Jain Dharmashala found!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void drawPath(MapDataModel mapDataModel) {

        if (polylineFinal != null) {
            polylineFinal.remove();
        }
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        List<List<HashMap<String, String>>> result = mapDataModel.getPolyLineResult();
        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            if (sourceMarker != null) {
                sourceMarker.remove();
            }

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(mapDataModel.getSourcelatLng());
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(mapDataModel.getSourcelatLng()));

            MarkerOptions markerOptions = new MarkerOptions();
            sourceLatLng = mapDataModel.getSourcelatLng();
            markerOptions.position(sourceLatLng);
            markerOptions.title("Destination Address");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dest));

            sourceMarker = mMap.addMarker(markerOptions);

            sourceMarker.setPosition(mapDataModel.getSourcelatLng());


            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            lineOptions.addAll(points);
            lineOptions.width(5);
            lineOptions.color(Color.RED);

            polylineFinal = mMap.addPolyline(lineOptions);

            // makeCalculateDistanceRequest(mapDataModel.getSourcelatLng(),mapDataModel.getDestlatLng());

            new UiUtils().makeCalculateDistanceRequest(getActivity(), this, mapDataModel.getSourcelatLng(), mapDataModel.getDestlatLng());
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL_BW_UPDATES);
        //  mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (sourceLatLng != null && destinationLatLng != null) {

                }
                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);


            createLocationRequest();
        } else {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);

                createLocationRequest();

            }
        } else if (requestCode == 102) {
            // showCurrentLocation();
            createLocationRequest();

        }
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            if (((FindDharamshalaActivity) getActivity()).nowMarker != null) {
                ((FindDharamshalaActivity) getActivity()).nowMarker.remove();
            } else {
                ((FindDharamshalaActivity) getActivity()).setAddressDestination(mCurrentLocation);
            }

            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            markerOptions.position(latLng);
            markerOptions.title("Current location");
            //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_accessibility_black_24dp));

            ((FindDharamshalaActivity) getActivity()).nowMarker = mMap.addMarker(markerOptions);

           /* CameraUpdate center =
                    CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);*/


            // marker.setPosition(latLng);
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "Location update started ..............: ");
        } else {
            //  this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();


    }


    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            Log.d(TAG, "Location update stopped .......................");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }
    }


    @Override
    public void getitemClickPosition(Object o) {
        ((FindDharamshalaActivity) getActivity()).showsnackBarMessage((String) o);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
      //  Toast.makeText(getActivity(), "Click Info Window", Toast.LENGTH_SHORT).show();

        LatLng latLng = marker.getPosition();
       String address= marker.getSnippet();

        for (Map.Entry m : destLatLngModelArrayList.entrySet()) {
            NearPlaceLatLngModel nearPlaceLatLngModel = (NearPlaceLatLngModel) m.getValue();

            String addre = nearPlaceLatLngModel.getTempleAddress();
            if (address!=null) {
                if (address.equalsIgnoreCase(addre)) {
                    Intent intent = new Intent(getActivity(), TempleContactDetailActivity.class);
                    intent.putExtra("nearPlaceLatLngModel", nearPlaceLatLngModel);
                    intent.putExtra("destinationLatLng", mapDataModel.getDestlatLng());
                    intent.putExtra("sourceLatLng", mapDataModel.getSourcelatLng());

                    startActivity(intent);
                    break;
                }
            }

        }

    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
      //  Toast.makeText(getActivity(), "Info Window long click", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }


}

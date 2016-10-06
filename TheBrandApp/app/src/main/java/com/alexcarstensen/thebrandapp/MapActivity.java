package com.alexcarstensen.thebrandapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.alexcarstensen.thebrandapp.R.id.map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean _userLocationKnown = false;
    private boolean _tracing = true;
    private Location userLocation;
    private LocationManager locationManager;
    public static final String EXTRA_USER_LATITUDE = "location_latitude";
    public static final String EXTRA_USER_LONGITUDE = "location_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        ////THISSSS"!
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            userLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

        }

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(map);
            mapFragment.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Show rationale and request permission.
                }

                updateMap();
            }
        }
    }

    private void updateMap() {
        if (_userLocationKnown) {

            if (!_tracing) {
                mMap.clear();   //if we are not tracking, remove the marker first
            }


            mMap.addMarker(new MarkerOptions().position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())).title("You are here!"));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (userLocation != null) {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.noinfo)));
            Bitmap test = resize(ContextCompat.getDrawable(this, R.drawable.noinfo));

            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Your location")
                    .icon(BitmapDescriptorFactory.fromBitmap(test)));
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(sydney, 15);
            mMap.animateCamera(yourLocation);
        }
        Toast.makeText(this, "ss", Toast.LENGTH_SHORT).show();

    }

    private Bitmap resize(Drawable image) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        return Bitmap.createScaledBitmap(b, 50, 50, false);
    }


    private void broadcastLocationUpdate(Location location){

        Intent update = new Intent("LOCATION_UPDATE");
        update.putExtra(EXTRA_USER_LATITUDE, location.getLatitude());
        update.putExtra(EXTRA_USER_LONGITUDE, location.getLongitude());
        LocalBroadcastManager.getInstance(this).sendBroadcast(update);

    }
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            userLocation = location;
            broadcastLocationUpdate(location);
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
    };
}

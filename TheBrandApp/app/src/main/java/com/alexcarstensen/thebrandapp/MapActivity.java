package com.alexcarstensen.thebrandapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.alexcarstensen.thebrandapp.Helpers.EmailNameHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.alexcarstensen.thebrandapp.Helpers.PermissionHelper.askPermission;
import static com.alexcarstensen.thebrandapp.R.id.map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker marker;
    private Boolean isResume = true;
    private Boolean zoomed;
    private long UPDATE_INTERVAL = 10000;
    private long FASTEST_INTERVAL = 2000;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_SET_LOCATION = 2;

    private String _mainUserEmail;
    public static final long ONE_MEGABYTE = 4096 * 4096;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent getIntent = getIntent();

        _mainUserEmail = getIntent.getStringExtra(MainActivity.SEND_MAINUSER_EMAIL_INFO);

        SetupFirebase();

        checkForPermissionIfGrantedInitializeMap();

        setPictureMarkersOnMap();

    }

    private void SetupFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();

    }

    private void checkForPermissionIfGrantedInitializeMap()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (askPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION))
            {
                getMapFragmentAndInitializeMap();

                buildGoogleApiClient();
                mGoogleApiClient.connect();
            }
        }
        else
        {
            getMapFragmentAndInitializeMap();

            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
    }

    private void getMapFragmentAndInitializeMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    private void buildGoogleApiClient()
    {
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void setPictureMarkersOnMap()
    {

        ValueEventListener picturesListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                for (DataSnapshot urlSnapShot : dataSnapshot.getChildren()
                        )
                {

                    final Pication pic = urlSnapShot.getValue(Pication.class);

                    StorageReference storageRefImage = storage.getReferenceFromUrl(pic.getUrl());

                    storageRefImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>()
                    {
                        @Override
                        public void onSuccess(byte[] bytes)
                        {
                            Bitmap bitmap = resize(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

                            LatLng example = new LatLng(pic.getLat(), pic.getLon());
                            mMap.addMarker(new MarkerOptions()
                                    .position(example).title("picture")
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("Maps", "Couldn't fetch pictures");
            }
        };

        String usersConvertedEmail = EmailNameHelper.ConvertEmail(_mainUserEmail);

        mDatabase.child("Pictures").child(usersConvertedEmail).addListenerForSingleValueEvent(picturesListener);


    }

    private Bitmap resize(Bitmap image)
    {
        return Bitmap.createScaledBitmap(image, 50, 50, false);
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        startLocationUpdates();
        CheckPermissionIfGrantedGetLastKnowLocation();

        if (mLastLocation != null)
            zoomToLastKnowLocation(mLastLocation);
    }

    private void CheckPermissionIfGrantedGetLastKnowLocation()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (askPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_SET_LOCATION) &&
                    askPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION))
            {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
        }
        else
        {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

    }

    // Ref: https://developer.android.com/training/location/receive-location-updates.html#save-state
    protected void startLocationUpdates()
    {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location)
    {

        // You can now create a LatLng Object for use with maps
        zoomToLastKnowLocation(location);
    }

    private void zoomToLastKnowLocation(Location location)
    {
        LatLng lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (isResume)
        {
            if (marker != null)
                marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(lastKnownLocation).title("Your Location"));
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15);
            mMap.animateCamera(yourLocation);
            zoomed = true;
            isResume = false;
        }
        else
        {

            if (marker != null)
                marker.remove();
            if (zoomed)
            {
                marker = mMap.addMarker(new MarkerOptions().position(lastKnownLocation).title("Your Location"));
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLng(lastKnownLocation);
                mMap.animateCamera(yourLocation);
            }
            else
            {
                marker = mMap.addMarker(new MarkerOptions().position(lastKnownLocation).title("Your Location"));
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15);
                mMap.animateCamera(yourLocation);
                zoomed = true;
            }


        }
    }


    @Override
    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isResume = true;

        if (mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                }
                else
                    Toast.makeText(this, "Permission denied to read your Location", Toast.LENGTH_SHORT).show();
                break;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_SET_LOCATION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                }
                else
                    Toast.makeText(this, "Permission denied to read your Location", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }


}

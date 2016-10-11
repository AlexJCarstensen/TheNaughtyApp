package com.alexcarstensen.thebrandapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.alexcarstensen.thebrandapp.Helpers.EmailNameHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_SET_LOCATION = 2;

    private String _mainUserEmail;
    private ArrayList<String> downloadUrls = new ArrayList<>();
    public static final long ONE_MEGABYTE = 4096 * 4096; // Der skal diskuteres om hvordan billeder gemmes

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;

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
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))
                Toast.makeText(this, "This app needs your permission to access your location", Toast.LENGTH_SHORT).show();
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                getMapFragmentAndInitializeMap();
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
                .findFragmentById(R.id.map);
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
        // TODO DEBUGGING ATM.. Retrive pictures from downloadURl


        ValueEventListener picturesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot urlSnapShot: dataSnapshot.getChildren()
                     ) {

                    final Pication pic = urlSnapShot.getValue(Pication.class);

                    StorageReference storageRefImage = storage.getReferenceFromUrl(pic.getUrl());

                    storageRefImage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>()
                    {
                        @Override
                        public void onSuccess(byte[] bytes)
                        {
                            Bitmap bitmap = resize(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            LatLng example = new LatLng(pic.getLat(), pic.getLon());
                            mMap.addMarker(new MarkerOptions()
                                    .position(example).title("picture")
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                        }
                    });

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
        CheckPermissionIfGrantedGetLastKnowLocation();

        if (mLastLocation != null)
            zoomToLastKnowLocation();
    }

    private void CheckPermissionIfGrantedGetLastKnowLocation()
    {
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_SET_LOCATION);
        }
        else
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    private void zoomToLastKnowLocation()
    {
        LatLng lastKnownLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(lastKnownLocation).title("Your Location"));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 15);
        mMap.animateCamera(yourLocation);
    }


    @Override
    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
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

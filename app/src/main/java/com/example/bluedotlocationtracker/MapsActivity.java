package com.example.bluedotlocationtracker;

import android.Manifest.permission;
import android.annotation.SuppressLint;

import com.example.bluedotlocationtracker.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.widget.Toast;

public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private boolean permissionDenied = false;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int locationPermissionCode = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private static final int intervalTime = 5000;
    private static final int minDistance = 10;
    private static final int handlerDelay = 1000 * 60 * 5;
    private static final int startHandlerDelay = 0;
    double newLat = 0;
    double newLon = 0;
    double oldLat = 0;
    double oldLon = 0;
    float distance;
    Location newLocation = new Location("newlocation");
    Location oldLocation = new Location("oldLocation");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        newLocation.setLatitude(newLat);
        newLocation.setLongitude(newLon);
        oldLocation.setLatitude(oldLat);
        oldLocation.setLongitude(oldLon);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                requestLocation();
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                handler.postDelayed(this, handlerDelay);
            }
        }, startHandlerDelay);

    }

    private void requestLocation() {
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        intervalTime, minDistance, (LocationListener) this);
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (oldLocation.getLatitude() == 0 && oldLocation.getLongitude() == 0){
            oldLocation.setLatitude(location.getLatitude());
            oldLocation.setLongitude(location.getLongitude());
            Toast.makeText(this, "Got Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        }else {
            newLocation.setLatitude(location.getLatitude());
            newLocation.setLongitude(location.getLongitude());
            distance = newLocation.distanceTo(oldLocation);
            if (distance >= 10){
                Toast.makeText(this, "You have moved: " + distance + " metres from your previous position.", Toast.LENGTH_SHORT).show();
                oldLocation.setLatitude(location.getLatitude());
                oldLocation.setLongitude(location.getLongitude());
                newLocation.setLatitude(0);
                newLocation.setLongitude(0);
            }
        }

        LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 50.0f));
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, locationPermissionCode,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != locationPermissionCode) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.mMap = googleMap;
    }
}
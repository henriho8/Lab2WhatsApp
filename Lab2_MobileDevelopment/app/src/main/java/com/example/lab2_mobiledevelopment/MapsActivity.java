package com.example.lab2_mobiledevelopment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap nMap;
    private static final String Tag = "MainActivity";
    private boolean nLocationPermission = false;
    private FusedLocationProviderClient nFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkLocationPermission();
    }


     // This method will call the current location after the map is ready

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        if (nLocationPermission) {
            getCurrentLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            nMap.setMyLocationEnabled(true);
        }
    }

    // Initializing map once permission is given

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        nLocationPermission = false;
        switch (requestCode) {
            case 2345:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    nLocationPermission = true;
                    initMap();
                }
        }
    }

    // Initializing the map settings

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // When user clicks on info button it will take them to location and get details

    public void getLocationDetails(View view){
        EditText place = (EditText) findViewById(R.id.txtLocation);
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<Address>();
        try{
            addressList = geocoder.getFromLocationName(place.getText().toString(),1);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }
        if(addressList.size() > 0){
            Address address = addressList.get(0);
            updateLocation(new LatLng(address.getLatitude(),address.getLongitude()),15f);
        }
    }

    // Checking permission for location

    private void checkLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                nLocationPermission = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 2345);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 2345);
        }
    }


    // Getting location details

    private void getCurrentLocation() {
        nFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(nLocationPermission){
                final Task location = nFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Log.i(TAG,"Location is found");
                            Location currentLocation = (Location) task.getResult();
                            updateLocation(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);
                        } else{
                            Log.e(TAG,"Location is not found");
                        }
                    }
                });
            }
        }catch (SecurityException e)
        {
            Log.e(TAG,e.getMessage());
        }
    }


    // Updating longitude, latitude, and markers

    private void updateLocation(LatLng latlng, float zoom){
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));

        nMap.addMarker(new MarkerOptions().position(latlng).title("Latitude : "+latlng.latitude+" "+"Longitude : "+latlng.longitude));

        TextView lat = (TextView) findViewById(R.id.lblLatText);
        TextView lng = (TextView) findViewById(R.id.lblLngText);
        lat.setText(latlng.latitude+"");
        lng.setText(latlng.longitude+"");
    }

    public void goToMain(View view) {
        Intent redirect = new Intent(MapsActivity.this,MainActivity.class);
        startActivity(redirect);
    }
}

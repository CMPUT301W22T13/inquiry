package com.cmput301w22t13.inquiry.geo;

import com.google.android.gms.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class GeolocationSetup extends AppCompatActivity {

    public LocationRequest locationRequest;

    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationRequest =  LocationRequest.create()
                .setInterval(30000 )
            .setFastestInterval(5000)
            .setMaxWaitTime(100)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    }

    private void update(){
        client = LocationServices.getFusedLocationProviderClient(GeolocationSetup.this);
    }

}

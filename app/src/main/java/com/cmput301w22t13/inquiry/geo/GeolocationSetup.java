package com.cmput301w22t13.inquiry.geo;

import com.google.android.gms.location.LocationRequest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class GeolocationSetup extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    public LocationRequest locationRequest;

    public FusedLocationProviderClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationRequest =  LocationRequest.create()
                .setInterval(30000 )
            .setFastestInterval(5000)
            .setMaxWaitTime(100)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    update();
                }
                else{
                    Toast.makeText(this, "Warning, map cannot be used without permission. ",Toast.LENGTH_SHORT).show();
                    
            }

        }
    }


    private void update(){
        client = LocationServices.getFusedLocationProviderClient(GeolocationSetup.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            client.getLastLocation();
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_FINE_LOCATION);
            }
        }
    }

}

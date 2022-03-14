package com.cmput301w22t13.inquiry.geo;

import com.cmput301w22t13.inquiry.R;
import com.google.android.gms.location.LocationRequest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeolocationSetup extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    public LocationRequest locationRequest;

    public FusedLocationProviderClient fusedLocationProviderClient;
    TextView latitude, longitude;

    Switch aSwitch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.geo_test);
        locationRequest =  LocationRequest.create()
                .setInterval(30000 )
            .setFastestInterval(5000)
            .setMaxWaitTime(100)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        latitude = findViewById(R.id.latitidue);
        longitude = findViewById(R.id.longitude);
        aSwitch = findViewById(R.id.switch1);
        update();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            longitude.setText("penis");
        }



            aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked()){
                    //update();
                }
            }
        });



    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    update();
                    latitude.setText("permissions granted");
                }
                else{
                    Toast.makeText(this, "Warning, map cannot be used without permission. ",Toast.LENGTH_SHORT).show();
                    
            }

        }
    }


    private void update(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GeolocationSetup.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //assume that permissions have been allowed, and gets you the location
                }
            });
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_FINE_LOCATION);
            }
        }
    }


    //method for testing the location
    //shouldnt need for main activity
    private void updateUI(Location location ){
        //longitude.setText((int) location.getLongitude());
        //latitude.setText((int) location.getLatitude());

    }

}

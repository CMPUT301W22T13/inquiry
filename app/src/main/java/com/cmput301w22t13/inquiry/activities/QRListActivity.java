package com.cmput301w22t13.inquiry.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.classes.QRListArrayAdapter;
import com.cmput301w22t13.inquiry.classes.RelativeQRLocation;
import com.cmput301w22t13.inquiry.ui.map.MapViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Screen to show nearby QR Codes
 */
public class QRListActivity extends AppCompatActivity {
    Double lat;
    Double lng;
    ArrayList<RelativeQRLocation> qrlist = new ArrayList<>();
    MapViewModel model;
    QRListArrayAdapter adapter;

    /**
     * Calculate the distance in metres in between a given QR code and the current location.
     * @param qr QR code
     * @return distance in metres
     */
    private double calculateDist(QRCode qr) {

        // Taken from: https://stackoverflow.com/a/20296966
        // Website: https://stackoverflow.com/
        // Author: https://stackoverflow.com/users/2720929/sandeepmaaram

        Location start = new Location("Start");
        start.setLatitude(lat);
        start.setLongitude(lng);

        Location end = new Location("End");
        end.setLatitude(qr.getLocation().latitude);
        end.setLongitude(qr.getLocation().longitude);

        return start.distanceTo(end);
    }

    /**
     * Find all nearby QR codes and put them into a list, sorted.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void obtainQRCodes() {
        model.getNearbyPoints(lat, lng, qr -> adapter.add(new RelativeQRLocation(qr, calculateDist(qr))),
                () -> adapter.sort(Comparator.comparingDouble(RelativeQRLocation::getDist)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_InQuiRy_NoActionBar);
        setContentView(R.layout.activity_qrlist);

        // get data model
        model = new MapViewModel();

        // get current location from intent
        lat = getIntent().getDoubleExtra("LAT", 0);
        lng = getIntent().getDoubleExtra("LNG", 0);

        // setup list adapter
        adapter = new QRListArrayAdapter(this, qrlist);
        ListView listView = findViewById(R.id.activity_qrlist_list);
        listView.setAdapter(adapter);

        // obtain closest points
        Button backButton = findViewById(R.id.qr_list_back_button);
        backButton.setOnClickListener(view -> finish());

        obtainQRCodes();

    }
}
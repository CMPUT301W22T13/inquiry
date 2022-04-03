package com.cmput301w22t13.inquiry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ListView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.classes.QRListArrayAdapter;
import com.cmput301w22t13.inquiry.classes.RelativeQRLocation;
import com.cmput301w22t13.inquiry.ui.map.MapViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class QRListActivity extends AppCompatActivity {
    Double lat;
    Double lng;
    ArrayList<RelativeQRLocation> qrlist = new ArrayList<>();
    MapViewModel model;
    QRListArrayAdapter adapter;

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

    private void obtainQRCodes() {
        model.getNearbyPoints(lat, lng, qr -> {
            adapter.add(new RelativeQRLocation(qr, calculateDist(qr)));
        });

        adapter.sort((item1, item2) -> Double.compare(item1.getDist(), item2.getDist()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlist);

        model = new MapViewModel();

        lat = getIntent().getDoubleExtra("LAT", 0);
        lng = getIntent().getDoubleExtra("LNG", 0);

        adapter = new QRListArrayAdapter(this, qrlist);
        ListView listView = findViewById(R.id.activity_qrlist_list);
        listView.setAdapter(adapter);

        obtainQRCodes();



    }
}
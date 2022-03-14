package com.cmput301w22t13.inquiry.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.PlayerStatusQRCodeListAdapter;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class PlayerStatusActivity extends AppCompatActivity {

    private ArrayList<QRCode> qrCodeArrayList;
    private final Database db = new Database();


    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_player_status);

        String uid = getIntent().getStringExtra("uid");
        DocumentSnapshot document = db.getById("users", uid).getResult();
        Player player = new Player((String) document.get("username"), (String) document.get("id"));

        ListView qrCodeListView = findViewById(R.id.playerQrCodesListView);
        qrCodeArrayList = player.getQRCodes(); //not sure what final function will be
        PlayerStatusQRCodeListAdapter qrCodeListAdapter = new PlayerStatusQRCodeListAdapter(this, qrCodeArrayList, player);
        qrCodeListView.setAdapter(qrCodeListAdapter);
        qrCodeListView.setClickable(true);

        qrCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRCode clickedQR = qrCodeArrayList.get(i);
                // move to qr code fragment or activity
            }
        });


        // back button ends the activity and returns to caller
        Button backButton = findViewById(R.id.profileViewBackButton);
        backButton.setOnClickListener(view -> finish());
    }
}

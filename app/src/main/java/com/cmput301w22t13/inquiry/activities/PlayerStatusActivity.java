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

import java.util.ArrayList;

public class PlayerStatusActivity extends AppCompatActivity {

    ListView qrCodeListView;
    PlayerStatusQRCodeListAdapter qrCodeListAdapter;
    ArrayList<QRCode> qrCodeArrayList;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_Player_Status);

        player = (Player) getIntent().getSerializableExtra("game");

        qrCodeListView = findViewById(R.id.playerQrCodesListView);
        qrCodeArrayList = player.getQRCodes(); //not sure what final function will be
        qrCodeListAdapter = new PlayerStatusQRCodeListAdapter(this, qrCodeArrayList, player);
        qrCodeListView.setAdapter(qrCodeListAdapter);
        qrCodeListView.setClickable(true);

        qrCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QRCode clickedQR = qrCodeArrayList.get(i);
                // move to qr code view
            }
        });


        // back button ends the activity and returns to caller
        Button backButton = findViewById(R.id.profileViewBackButton);
        backButton.setOnClickListener(view ->{
            finish();
        });
    }
}

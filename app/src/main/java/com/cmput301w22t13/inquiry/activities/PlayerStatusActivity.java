package com.cmput301w22t13.inquiry.activities;
/** Populates views of activity_player_status.xml with a specific player's
 * data when the user wants to browse a specific player's QR codes.
 * Responsible for getting QR codes from database to populate the listview
 */

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
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_player_status);

        // gets player data from database to be displayed
        String uid = getIntent().getStringExtra("uid");
        db.getById("users", uid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    player = new Player((String) document.get("username"), (String) document.get("id"));
                } else finish();
            } else finish();
        });

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

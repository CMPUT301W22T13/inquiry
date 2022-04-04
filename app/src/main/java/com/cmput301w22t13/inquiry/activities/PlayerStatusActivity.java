package com.cmput301w22t13.inquiry.activities;
/**
 * Populates views of activity_player_status.xml with a specific player's
 * data when the user wants to browse a specific player's QR codes.
 * Responsible for getting QR codes from database to populate the listview
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.PlayerStatusQRCodeListAdapter;
import com.cmput301w22t13.inquiry.classes.QRCode;

import java.util.ArrayList;

/**
 * An activity to represent player status.
 */
public class PlayerStatusActivity extends AppCompatActivity {

    private ArrayList<QRCode> qrCodeArrayList = new ArrayList<>();

    /**
     * Setup player status activity.
     * @param savedInstances
     */
    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);

        // setup theme
        setTheme(R.style.Theme_InQuiRy_NoActionBar);
        setContentView(R.layout.activity_player_status);

        // gets player data from database to be displayed
        Player player = (Player) getIntent().getSerializableExtra("Player");
        TextView playerName = findViewById(R.id.playerDetailsTextView);
        playerName.setText(player.getUsername());

        // fetch all QR codes and put them in array
        ListView qrCodeListView = findViewById(R.id.playerQrCodesListView);
        player.fetchQRCodes(Task -> {});
        qrCodeArrayList.addAll(player.getQRCodes());

        // link QRCodeList to array
        PlayerStatusQRCodeListAdapter qrCodeListAdapter = new PlayerStatusQRCodeListAdapter(this, qrCodeArrayList, player);
        qrCodeListView.setAdapter(qrCodeListAdapter);
        qrCodeListView.setClickable(true);

        // setup time handler
        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                qrCodeArrayList.clear();
                qrCodeArrayList.addAll(player.getQRCodes());
                bubbleSortQRCodes(qrCodeArrayList);
                qrCodeListAdapter.notifyDataSetChanged();
                timerHandler.postDelayed(this, 500);
            }
        };
        timerHandler.post(timerRunnable);


        qrCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // go to details page on click
                QRCode clickedQR = qrCodeArrayList.get(i);
                Intent intent = new Intent(getApplicationContext(), QRDetailsActivity.class);
                intent.putExtra("code", clickedQR);
                intent.putExtra("player", player.getUsername());
                startActivity(intent);
                }
        });

        // back button ends the activity and returns to caller
        Button backButton = findViewById(R.id.profileViewBackButton);
        backButton.setOnClickListener(view -> finish());
    }

    /**
     * Update list for player
     * @param player Player
     */
    private void updateList(Player player){
        ListView qrCodeListView = findViewById(R.id.playerQrCodesListView);
        qrCodeArrayList = player.getQRCodes();
        TextView playerName = findViewById(R.id.playerDetailsTextView);
        playerName.setText(player.getUsername() + "'s QR Codes");

        PlayerStatusQRCodeListAdapter qrCodeListAdapter = new PlayerStatusQRCodeListAdapter(this, qrCodeArrayList, player);
        qrCodeListView.setAdapter(qrCodeListAdapter);
        qrCodeListView.setClickable(true);

    }

    /**
     * Sort list of QR codes.
     * @param a list of QR codes.
     */
    public static void bubbleSortQRCodes(ArrayList<QRCode> a){
        boolean sorted = false;
        QRCode temp;
        while(!sorted){
            sorted = true;
            for (int i = a.size()-1; i > 0; i--){
                if (a.get(i).getScore() > a.get(i-1).getScore()){
                    temp = a.get(i);
                    a.set(i,a.get(i-1));
                    a.set(i-1,temp);
                    sorted = false;
                }
            }
        }
    }
}

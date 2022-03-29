package com.cmput301w22t13.inquiry.activities;

/** Populates views of activity_player_profile.xml with a specific player's
 * data when the user wants to browse a specific player's profile.
 * The views show a specific player's information and their game wide rankings
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.cmput301w22t13.inquiry.ui.leaderboard.LeaderboardFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;

public class PlayerProfileActivity extends AppCompatActivity {

    private final Database db = new Database();
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        // gets player data from database to be displayed
        String uid = getIntent().getStringExtra("uid");
        //refreshes textViews every 2 seconds so if userdata changes it updates

        ArrayList<Player> players = new ArrayList<>();
        LeaderboardFragment.getPlayers(players);
        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                db.getById("users", uid).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            player = new Player((String) document.get("username"), (String) document.get("id"),true);
                            //refreshes textViews after 1/2 of a second to give qr codes time to fetch

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    LeaderboardFragment.bubbleSort(players,1);
                                    for (int i = 0; i < players.size(); i++){
                                        if (players.get(i).getUid().equals(player.getUid())) player.setRank(i+1);
                                    }
                                    setTexts();
                                }
                            }, 500);


                        } else finish();
                    } else finish();
                });
                timerHandler.postDelayed(this, 2000);
            }
        };
        timerHandler.post(timerRunnable);


        // moves to the gameStatus activity if the button is pressed
        Button gameStatusButton = findViewById(R.id.playerProfileGameStatusButton);
        gameStatusButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PlayerStatusActivity.class);
            intent.putExtra("uid", uid);
            //startActivity(intent); removed until getQRCodes implemented
            setTexts();
        });

        // ends activity
        Button backButton = findViewById(R.id.playerProfileBackButton);
        backButton.setOnClickListener(view -> finish());

    }

    private void updateQRCodes(String uid) {
        ArrayList<QRCode> qrList = new ArrayList<>();

    }

    public void setTexts() {
        // sets the player data TextViews to show the proper data
        TextView userNameView = findViewById(R.id.playerProfileUserNameTextView);
        userNameView.setText(player.getUsername());

        TextView lowestScoreView = findViewById(R.id.playerProfileLowestScoreTextView);
        String lowestScoreString = "Lowest Score: " + player.getLowestScore();
        lowestScoreView.setText(lowestScoreString);

        TextView highestScoreView = findViewById(R.id.playerProfileHighestScoreTextView);
        String highestScoreString = "Highest Score: " + player.getHighestScore();
        highestScoreView.setText(highestScoreString);

        TextView totalScoreView = findViewById(R.id.playerProfileTotalScoreTextView);
        String totalScoreString = "Total Score: " + player.getTotalScore();
        totalScoreView.setText(totalScoreString);

        TextView QRCodeCountView = findViewById(R.id.playerProfileQRCodeCountTextView);
        String QRCodeCountString = player.getQRCodeCount() + " QR Codes";
        QRCodeCountView.setText(QRCodeCountString);

        TextView QRCodeRankView = findViewById(R.id.playerProfileRankingTextView);
        String QRCodeRankString = "rank: " + player.getRank();
        QRCodeRankView.setText(QRCodeRankString);
    }
}
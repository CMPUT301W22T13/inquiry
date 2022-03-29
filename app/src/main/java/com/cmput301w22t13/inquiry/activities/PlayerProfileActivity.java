package com.cmput301w22t13.inquiry.activities;

/** Populates views of activity_player_profile.xml with a specific player's
 * data when the user wants to browse a specific player's profile.
 * The views show a specific player's information and their game wide rankings
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.ui.leaderboard.LeaderboardFragment;


import java.util.ArrayList;

public class PlayerProfileActivity extends AppCompatActivity {

    private Player player;
    private final Auth auth = new Auth();
    private ArrayList<Player> players = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        // gets player data from database to be displayed
        player = (Player) getIntent().getSerializableExtra("Player");
        player.fetchQRCodes(Task -> {});
        //refreshes textViews every 2 seconds so if userdata changes it updates
        LeaderboardFragment.getPlayers(players);
        LeaderboardFragment.bubbleSort(players,1);
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getUid().equals(player.getUid())) player.setRank(i+1);
        }
        setTexts();
        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                //refreshes textViews after 1/2 of a second to give qr codes time to fetch
                    LeaderboardFragment.bubbleSort(players,1);
                    for (int i = 0; i < players.size(); i++){
                        if (players.get(i).getUid().equals(player.getUid())) player.setRank(i+1);
                    }
                    setTexts();
                timerHandler.postDelayed(this, 500);
            }
        };
        timerHandler.post(timerRunnable);


        // moves to the gameStatus activity if the button is pressed
        Button gameStatusButton = findViewById(R.id.playerProfileGameStatusButton);
        gameStatusButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PlayerStatusActivity.class);
            intent.putExtra("Player", player);
            //startActivity(intent); removed until getQRCodes implemented
            setTexts();
        });

        // ends activity
        Button backButton = findViewById(R.id.playerProfileBackButton);
        backButton.setOnClickListener(view -> finish());

        if(auth.getPlayer().getIsOwner()){
            Log.d("VERBS",auth.getPlayer().getUsername());
            Log.d("VERBS", "isOwner");
            Button deletePlayerButton = findViewById(R.id.deletePlayer);
            deletePlayerButton.setVisibility(View.VISIBLE);
            deletePlayerButton.setOnClickListener(view -> {
                auth.getPlayer().deletePlayer(player);
                finish();

            });
        }else{
            Log.d("VERBS",auth.getPlayer().getUsername());
            Log.d("VERBS", "notOWner");
        }
    }

    private void updateQRCodes(String uid) {
        ArrayList<QRCode> qrList = new ArrayList<>();

    }

    public void setTexts() {
        // sets the player data TextViews to show the proper data
        TextView userNameView = findViewById(R.id.playerProfileUserNameTextView);
        userNameView.setText(player.getUsername());

        LeaderboardFragment.bubbleSort(players,2);
        int highestRank = -1;
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getUid().equals(player.getUid())) highestRank = i+1;
        }
        LeaderboardFragment.bubbleSort(players,3);
        int qrCodeCountRank = -1;
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getUid().equals(player.getUid())) qrCodeCountRank = i+1;
        }
        LeaderboardFragment.bubbleSort(players,4);
        int lowestRank = -1;
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getUid().equals(player.getUid())) lowestRank = i+1;
        }

        TextView lowestScoreView = findViewById(R.id.playerProfileLowestScoreTextView);
        String lowestScoreString = "Lowest Score: " + player.getLowestScore() + " Rank: " + lowestRank;
        lowestScoreView.setText(lowestScoreString);

        TextView highestScoreView = findViewById(R.id.playerProfileHighestScoreTextView);
        String highestScoreString = "Highest Score: " + player.getHighestScore() + " Rank: " + highestRank;
        highestScoreView.setText(highestScoreString);

        TextView totalScoreView = findViewById(R.id.playerProfileTotalScoreTextView);
        String totalScoreString = "Total Score: " + player.getTotalScore() + " Rank: " + player.getRank();
        totalScoreView.setText(totalScoreString);

        TextView QRCodeCountView = findViewById(R.id.playerProfileQRCodeCountTextView);
        String QRCodeCountString = player.getQRCodeCount() + " QR Codes"+ " Rank: " + qrCodeCountRank;
        QRCodeCountView.setText(QRCodeCountString);

        TextView QRCodeRankView = findViewById(R.id.playerProfileRankingTextView);
        String QRCodeRankString = "Rank: " + player.getRank();
        QRCodeRankView.setText(QRCodeRankString);
    }
}
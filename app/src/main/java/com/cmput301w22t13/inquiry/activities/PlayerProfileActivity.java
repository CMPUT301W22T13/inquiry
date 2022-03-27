package com.cmput301w22t13.inquiry.activities;

/** Populates views of activity_player_profile.xml with a specific player's
 * data when the user wants to browse a specific player's profile.
 * The views show a specific player's information and their game wide rankings
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.firebase.firestore.DocumentSnapshot;

public class PlayerProfileActivity extends AppCompatActivity {

    private final Database db = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        // gets player data from database to be displayed
        Player player = (Player) getIntent().getSerializableExtra("Player");

        player.fetchQRCodes(qrCodes -> {
            setTexts(player);
        });


        // moves to the gameStatus activity if the button is pressed
        Button gameStatusButton = findViewById(R.id.playerProfileGameStatusButton);
        gameStatusButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PlayerStatusActivity.class);
            intent.putExtra("Player", player);
            //startActivity(intent); removed until getQRCodes implemented
        });

        // ends activity
        Button backButton = findViewById(R.id.playerProfileBackButton);
        backButton.setOnClickListener(view -> finish());

    }

    private void setTexts(Player player) {
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
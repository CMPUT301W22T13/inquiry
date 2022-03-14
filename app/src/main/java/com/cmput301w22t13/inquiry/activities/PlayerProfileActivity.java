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

        String uid = getIntent().getStringExtra("uid");
        db.getById("users", uid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Player player = new Player((String) document.get("username"), (String) document.get("id"));
                    setTexts(player);
                } else finish();
            } else finish();
        });




        Button gameStatusButton = findViewById(R.id.playerProfileGameStatusButton);

        gameStatusButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PlayerStatusActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        // ends activity
        Button backButton = findViewById(R.id.playerProfileBackButton);
        backButton.setOnClickListener(view -> finish());

    }

    private void setTexts(Player player) {
        TextView userNameView = findViewById(R.id.playerProfileUserNameTextView);
        userNameView.setText(player.getUserName());

        /*
        TextView lowestScoreView = findViewById(R.id.playerProfileLowestScoreTextView);
        String lowestScoreString = "Lowest Score: " + player.getLowestScore();
        lowestScoreView.setText(lowestScoreString);

        TextView highestScoreView = findViewById(R.id.playerProfileHighestScoreTextView);
        String highestScoreString = "Highest Score: " + player.getHighestScore();
        highestScoreView.setText(highestScoreString);

        TextView totalScoreView = findViewById(R.id.playerProfileTotalScoreTextView);
        String totalScoreString = "Total Score: " + player.getTotalScore();
        totalScoreView.setText(totalScoreString);

        TextView rankView = findViewById(R.id.playerProfileRankingTextView);
        String rankString = "Rank: " + player.getRank();
        rankView.setText(rankString);

        TextView QRCodeCountView = findViewById(R.id.playerProfileQRCodeCountTextView);
        String QRCodeCountString = player.getQRCodeCount() + " QR Codes";
        QRCodeCountView.setText(QRCodeCountString);
        */
    }
}
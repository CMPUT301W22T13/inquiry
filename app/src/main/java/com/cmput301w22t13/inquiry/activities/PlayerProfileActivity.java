package com.cmput301w22t13.inquiry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.Player;

import java.util.ArrayList;

public class PlayerProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        Player player = (Player) getIntent().getSerializableExtra("player");

        TextView userNameView = findViewById(R.id.playerProfileUserNameTextView);
        userNameView.setText(player.getUserName());
        TextView lowestScoreView = findViewById(R.id.playerProfileLowestScoreTextView);
        lowestScoreView.setText(player.getLowestScore());
        TextView highestScoreView = findViewById(R.id.playerProfileHighestScoreTextView);
        highestScoreView.setText(player.getHighestScore());
        TextView totalScoreView = findViewById(R.id.playerProfileTotalScoreTextView);
        totalScoreView.setText(player.getTotalScore());
        TextView rankView = findViewById(R.id.playerProfileRankingTextView);
        rankView.setText(player.getRank());
        TextView QRCodeCountView = findViewById(R.id.playerProfileQRCodeCountTextView);
        QRCodeCountView.setText(player.getQRCodeCount());


        // ends activity
        Button backButton = findViewById(R.id.playerProfileBackButton);
        backButton.setOnClickListener(view -> finish());





    }
}
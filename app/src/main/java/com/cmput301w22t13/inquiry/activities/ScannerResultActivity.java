package com.cmput301w22t13.inquiry.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w22t13.inquiry.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class ScannerResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_InQuiRy_NoActionBar);
        setContentView(R.layout.activity_scanner_result);

        KonfettiView konfettiView = findViewById(R.id.scanner_result_confetti_view);

        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        Party party = new PartyFactory(emitterConfig)
                .spread(150)
                .angle(-90)
                .setSpeedBetween(0f, 50f)
                .position(new Position.Relative(0.5, 1.0f))
                .colors(Arrays.asList(getResources().getColor(R.color.purple_200), getResources().getColor(R.color.purple_500)))
                .build();

        konfettiView.start(party);

        String name = getIntent().getStringExtra("name");
        int score = getIntent().getIntExtra("score", 0);

        TextView nameTextView = findViewById(R.id.scanner_result_qr_name);
        TextView scoreTextView = findViewById(R.id.scanner_result_qr_score);

        nameTextView.setText(name);
        scoreTextView.setText(score + " pts");


        // ends activity
        Button backButton = findViewById(R.id.scannerResultBackButton);
        backButton.setOnClickListener(view -> {

            finish();
        });
    }
}
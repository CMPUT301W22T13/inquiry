package com.cmput301w22t13.inquiry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;

public class ScannerResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_InQuiRy_NoActionBar);
        setContentView(R.layout.activity_scanner_result);

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
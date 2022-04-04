package com.cmput301w22t13.inquiry.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.Result;

import java.util.Objects;

public class QRLoginActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlogin);
        CodeScannerView scannerView = findViewById(R.id.login_scanner_view);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            //ask for authorisation
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
        else {

            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(result -> {
                String resultString = result.getText();

                // if qr code string starts with "INQUIRY_LOGIN_", try to sign in
                if (resultString.startsWith("INQUIRY_LOGIN_")) {
                    String uid = resultString.substring(14);

                    Auth.login(uid, isSuccessful -> {
                        if (isSuccessful) {

                            // display message if successful
                            runOnUiThread(() -> Toast.makeText(getBaseContext(), "Found user account", Toast.LENGTH_SHORT).show());
                            finish();
                        } else {
                            // display error message
                            runOnUiThread(() -> Toast.makeText(getBaseContext(), "Unable to login", Toast.LENGTH_SHORT).show());
                        }
                    });

                } else {
                    // display error message
                    runOnUiThread(() -> Toast.makeText(this, "Incorrect code found", Toast.LENGTH_SHORT).show());
                }

            });
        }
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCodeScanner != null) {
            // restart scanner on resume
            mCodeScanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        if (mCodeScanner != null) {
            // pause scanner onn activity pause
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }
}
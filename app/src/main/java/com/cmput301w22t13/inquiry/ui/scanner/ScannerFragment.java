package com.cmput301w22t13.inquiry.ui.scanner;
/**
 * Class responsible for user navigation in scanner_fragment.xml
 * Let's user scan any code and add it to their profile
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.PlayerStatusActivity;
import com.cmput301w22t13.inquiry.activities.ScannerResultActivity;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.Result;

import java.util.Objects;

public class ScannerFragment extends Fragment {

    private CodeScanner mCodeScanner = null;
    Player player = Auth.getPlayer();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.scanner_fragment, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            //ask for authorisation
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 50);
        else {

            mCodeScanner = new CodeScanner(Objects.requireNonNull(activity), scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {

                    QRCode QR = new QRCode(result.getText());
                    QR.save();

                    Intent intent = new Intent(activity.getApplicationContext(), ScannerResultActivity.class);
                    intent.putExtra("name", QR.getName());
                    intent.putExtra("score", QR.getScore());
                    startActivity(intent);

                }
            });
        }
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCodeScanner!= null){
            mCodeScanner.startPreview();

        }
    }

    @Override
    public void onPause() {
        if(mCodeScanner!= null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }
}
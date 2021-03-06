package com.cmput301w22t13.inquiry.ui.scanner;
/**
 * Class responsible for user navigation in scanner_fragment.xml
 * Let's user scan any code and add it to their profile
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.PlayerProfileActivity;
import com.cmput301w22t13.inquiry.activities.ScannerResultActivity;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.firebase.firestore.DocumentSnapshot;
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
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDecoded(@NonNull final Result result) {
                    String resultString = result.getText();

                    // if qr code string starts with "INQUIRY_USER_", don't save to database
                    if (resultString.startsWith("INQUIRY_USER_")) {
                        requireActivity().runOnUiThread(() -> {
                            String uid = resultString.substring(13);
                            Database db = new Database();
                            db.getById("user_accounts", uid).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
//                                Log.d("ScannerFragment", "Successfully queried database + " + uid);
                                    DocumentSnapshot document = task.getResult();

                                    if (document != null && document.exists()) {
                                        Log.d("ScannerFragment", "DocumentSnapshot data: " + document.getData());
                                        String documentUsername = document.getString("username");
                                        Player player;
                                        if (documentUsername != null && !documentUsername.toString().equals(document.getId())) {
                                            player = new Player(document.getId(), document.getId(), true);
                                            Toast.makeText(requireActivity(), "You found " + documentUsername + "!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            player = new Player(document.getId(), document.getId(), true);
                                            Toast.makeText(requireActivity(), "You found " + document.getId() + "!", Toast.LENGTH_SHORT).show();
                                        }

                                        Intent intent = new Intent(getActivity(), PlayerProfileActivity.class);

//                                        requireActivity().runOnUiThread(() -> {

//                                        });
                                        intent.putExtra("Player", player);
                                        startActivity(intent);

                                    }

                                }
                            });
                        });

                    } else if (resultString.startsWith("INQUIRY_LOGIN_")) {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(),
                                        "To login to another account, navigate to the Profile tab.",
                                        Toast.LENGTH_LONG).show()
                        );
                    } else {
                        QRCode QR = new QRCode(result.getText());
                        QR.save();

                        Intent intent = new Intent(requireContext(), ScannerResultActivity.class);
                        intent.putExtra("name", QR.getName());
                        intent.putExtra("score", QR.getScore());
                        intent.putExtra("qrHash", QR.getHash());
                        startActivity(intent);
                    }

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
        if (mCodeScanner != null) {
            mCodeScanner.startPreview();

        }
    }

    @Override
    public void onPause() {
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }
}
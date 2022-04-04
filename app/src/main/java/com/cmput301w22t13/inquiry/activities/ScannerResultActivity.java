package com.cmput301w22t13.inquiry.activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.LocationPermission;
import com.cmput301w22t13.inquiry.db.Database;
import com.cmput301w22t13.inquiry.db.Storage;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.github.javafaker.Faker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.xml.KonfettiView;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ScannerResultActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Faker faker = new Faker();
    LocationPermission lp;

    String qrHash;

    @SuppressLint("MissingPermission")
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
                .setSpeedBetween(0f, 70f)
                .position(new Position.Relative(0.5, 1.0f))
                .colors(Arrays.asList(getResources().getColor(R.color.purple_200), getResources().getColor(R.color.purple_500)))
                .build();

        konfettiView.start(party);

        String name = getIntent().getStringExtra("name");
        int score = getIntent().getIntExtra("score", 0);
        qrHash = getIntent().getStringExtra("qrHash");

        TextView nameTextView = findViewById(R.id.scanner_result_qr_name);
        TextView scoreTextView = findViewById(R.id.scanner_result_qr_score);

        nameTextView.setText(name);
        scoreTextView.setText(score + " pts");
        // ends activity
        Button backButton = findViewById(R.id.scannerResultBackButton);
        backButton.setOnClickListener(view -> {
            finish();
        });


        Button addPictureButton = findViewById(R.id.button_add_image);
        addPictureButton.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Error: Camera unavailable", Toast.LENGTH_SHORT).show();
            }
        });


        // add location button
        lp = new LocationPermission(this::registerForActivityResult, () -> {
            // what to run when permission has been successfully requested
            FusedLocationProviderClient flp = LocationServices.getFusedLocationProviderClient(this);
            flp.getLastLocation().addOnCompleteListener(this, task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    uploadLocation(task.getResult());
                } else {
                    Toast.makeText(this, "Error determining location", Toast.LENGTH_SHORT).show();
                }
            });

        }, () -> Toast.makeText(this, "Location permissions must be provided", Toast.LENGTH_SHORT).show());

        Button addLocationButton = findViewById(R.id.button_save_location);
        addLocationButton.setOnClickListener(view -> lp.request());

        Button closeButton = findViewById(R.id.save_qr_button);
        closeButton.setOnClickListener(view -> {
           // finish activity and go to MyQrs page
            finish();
        });

        EditText commentEditTest = findViewById(R.id.scanner_result_edit_comment);
        Button saveCommentButton = findViewById(R.id.button_save_comment);
        saveCommentButton.setOnClickListener(view -> {
            String comment = commentEditTest.getText().toString();
            if (comment.length() > 0 && comment.length() < 100) {
                addCommentToQr(comment);

                // dismiss keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentEditTest.getWindowToken(), 0);

                saveCommentButton.setEnabled(false);
                commentEditTest.setEnabled(false);
                saveCommentButton.setText("Comment Added!");
            }
            else {
                Toast.makeText(this, "Comment must be between 1 and 100 characters", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            TextView imageUploadStatusText = findViewById(R.id.scanner_result_image_upload_status);
            imageUploadStatusText.setText("Uploading image...");

            Bundle extras = data.getExtras();

            String filename = "image_" + System.currentTimeMillis() + "_" + faker.number().digits(3);

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView imageView = findViewById(R.id.scanner_result_image_thumbnail);
            imageView.setImageBitmap(imageBitmap);

            uploadImage(imageBitmap, filename);
        }
    }

    // image upload to storage
    // reference: https://firebase.google.com/docs/storage/android/upload-files
    private void uploadImage(Bitmap imageBitmap, String filename) {
        if (imageBitmap != null) {
            Storage storage = new Storage();

//            ProgressBar progressBar = findViewById(R.id.scanner_result_image_progress);
            TextView imageUploadStatusText = findViewById(R.id.scanner_result_image_upload_status);
            View addImageContainer = findViewById(R.id.scanner_result_add_image);
            View imageAddedContainer = findViewById(R.id.scanner_result_add_image_success);

//            progressBar.setVisibility(View.VISIBLE);
            addImageContainer.setVisibility(View.GONE);
            imageAddedContainer.setVisibility(View.VISIBLE);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
            byte[] data = byteStream.toByteArray();


            storage
                .storeFileBytes("location_images/" + filename + ".jpg", data)
                .addOnSuccessListener(taskSnapshot -> {
                    imageUploadStatusText.setText("Image uploaded!");
//                    progressBar.setVisibility(View.GONE);

                    // add image to database
                    addImageToQr(filename);

                    String imageUrl = Objects.requireNonNull(taskSnapshot.getUploadSessionUri()).toString();
                    Log.d("uploadImage", "uploadImage done: " + imageUrl);
                })
//                .addOnProgressListener(taskSnapshot -> {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    progressBar.setProgress((int) progress);
//                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Couldn't upload image", Toast.LENGTH_SHORT).show();
                    imageUploadStatusText.setText("Image upload failed.");
//                    progressBar.setVisibility(View.GONE);
                });
        }
        else {
            Toast.makeText(this, "Couldn't upload image", Toast.LENGTH_SHORT).show();
        }
    }

    // from the hash, find the qr_code document and update it to include the newly added image
    private void addImageToQr(String imageName) {
        Database db = new Database();

        db.query("qr_codes", "hash", qrHash).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                String qrId = document.getId();

                Map<String, Object> data = new HashMap<>();
                data.put("location_image", imageName);
                db.update("qr_codes", qrId, data);
            }
        });
    }

    private void uploadLocation(Location loc) {

        Database db = new Database();

        db.query("qr_codes", "hash", qrHash).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                String qrId = document.getId();

                // append location to document
                Map<String, Object> data = new HashMap<>();
                data.put("lat", loc.getLatitude());
                data.put("lng", loc.getLongitude());
                GeoLocation l = new GeoLocation(loc.getLatitude(), loc.getLongitude());
                data.put("geohash", GeoFireUtils.getGeoHashForLocation(l));
                db.update("qr_codes", qrId, data);

                View locationContainer = findViewById(R.id.scanner_result_add_location);
                View savedContainer = findViewById(R.id.scanner_result_saved_location);

                locationContainer.setVisibility(View.INVISIBLE);
                savedContainer.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "Error uploading location", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addCommentToQr(String comment) {
        Database db = new Database();

        db.query("qr_codes", "hash", qrHash).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                String qrId = document.getId();

                // append comment to field array
                Map<String, Object> data = new HashMap<>();
                data.put("comments", FieldValue.arrayUnion(comment));
                db.update("qr_codes", qrId, data);
            }
        });
    }

}
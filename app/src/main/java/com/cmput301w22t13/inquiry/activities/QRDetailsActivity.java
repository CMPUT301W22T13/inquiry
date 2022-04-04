package com.cmput301w22t13.inquiry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * shows details about a QRCode when the user clicks on it
 */

public class QRDetailsActivity extends AppCompatActivity {
    String qrHash;
    Database db = new Database();
    String currentPlayer;

    ArrayList<String> commentsList = new ArrayList<>();


    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_InQuiRy_NoActionBar);
        setContentView(R.layout.activity_qrdetails);

        QRCode code = (QRCode) getIntent().getSerializableExtra("code");
        currentPlayer = (String) getIntent().getSerializableExtra("player");

        TextView qrName = findViewById(R.id.myqrs_qr_name);
        qrName.setText(code.getName());
        qrHash = code.getHash();

        TextView qrInitials = findViewById(R.id.qr_details_initials);
        String[] qrNameSplit = code.getName().split(" ");
        qrInitials.setText(qrName.getText().toString().substring(0,1) + qrNameSplit[1].substring(0,1));

        TextView qrScore = findViewById(R.id.myqrs_qr_score);

        if (code.getScore() ==1){
            qrScore.setText("1 point");
        }
        else {
            qrScore.setText(String.valueOf(code.getScore()) + " points");
        }

        LatLng coordinates = code.getLocation();
        TextView qrLocation = findViewById(R.id.qr_details_location_text);

        if (coordinates != null) {
            Log.d("QRDetailsActivity", "coordinates not null" + coordinates.toString());
            // convert coordinates to readable format (address)
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                List<android.location.Address> addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                qrLocation.setText(address);
                qrLocation.setVisibility(TextView.VISIBLE);
            } catch (Exception e) {
//            qrLocation.setText("Location not found");
            }
        }


        // ends activity
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

        //gets the ID of the qrcode from the database
        Task<QuerySnapshot> querySnapshotTask = db.query("qr_codes", "hash", code.getHash());
        querySnapshotTask.addOnCompleteListener(task -> {
            List<DocumentSnapshot> documents = task.getResult().getDocuments();
            Log.d("size", String.valueOf(documents.size()));
            if (documents.size() == 1) {
                String id = documents.get(0).getId();
                findPlayers(id);
                findComments(id);
            }
        });

        Button leaveCommentButton = findViewById(R.id.qr_details_add_comment_button);
        EditText leaveCommentText = findViewById(R.id.leave_comment);
        leaveCommentButton.setOnClickListener(view -> {
            String comment = leaveCommentText.getText().toString();
            if (comment.length() > 0 && comment.length() < 100) {
                addCommentToQr(comment);

                // dismiss keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(leaveCommentText.getWindowToken(), 0);
                leaveCommentText.setText("");
                leaveCommentText.clearFocus();

                Toast.makeText(this, "Comment added", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Comment must be between 1 and 100 characters", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * adds a comment to a QRCode
     * @param comment String to be added as a comment
     */
    private void addCommentToQr(String comment) {
        db.query("qr_codes", "hash", qrHash).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                String qrId = document.getId();

                // append comment to field array
                Map<String, Object> data = new HashMap<>();
                data.put("comments", FieldValue.arrayUnion(comment));
                db.update("qr_codes", qrId, data);

                commentsList.add(comment);
                updateCommentUI(commentsList);
            }
        });
    }

    /**
     * updates a list of other users who have scanned the QR Code
     * @param list list of users names to update the listview with
     */
    private void updateUI(ArrayList<String> list){
        ListView usernameList = findViewById(R.id.MatchingQrsList);

        ArrayAdapter<String> userList = new ArrayAdapter<String>(
                this, R.layout.matching_qr_list_content, list);
        usernameList.setAdapter(userList);
    }

    /**
     * updates a list of comments having been posted about the QR Code
     * @param list list of QR Codes to update the listview with
     */
    private void updateCommentUI(ArrayList<String> list){
        ListView usernameList = findViewById(R.id.comments_list);

        ArrayAdapter<String> commentList = new ArrayAdapter<String>(
                this, R.layout.comments_list_content, list);
        // reverse comments so newest is first
        Collections.reverse(list);
        usernameList.setAdapter(commentList);
    }

    /**
     * get all players who have scanned the qr code
     * @param id id of the qr code that should be found
     */
    private void findPlayers(String id){
        ArrayList<String> namesList = new ArrayList<>();

        DocumentReference reference = db.getDocReference("qr_codes/"+id);
        db.arrayQuery("user_accounts","qr_codes",reference).addOnCompleteListener(task ->{
            if (task.isSuccessful()){
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                int size = documents.size();
                if (size!= 0) {
                    for (DocumentSnapshot document: documents) {
                        String username = document.getId();
                        namesList.add(username);
                        Log.d("Before: ", document.getId());
                    }
                }
            }

            namesList.remove(currentPlayer); //removes the current player from the list, as it is clear that they have scanned already
            updateUI(namesList);

        });
    }

    /**
     * get comments that has been made about the QRCode
     * @param qrDocumentId qrcodes id to be searched for
     */
    private void findComments(String qrDocumentId){


        db.getById("qr_codes",qrDocumentId).addOnCompleteListener(task ->{
            if (task.isSuccessful()){
                DocumentSnapshot qrDocument = task.getResult();
                ArrayList<String> qrComments = (ArrayList<String>) qrDocument.get("comments");

                if(qrComments != null) {
                    int size = qrComments.size();
                    if (size != 0) {
                        Log.d("size", String.valueOf(size));
                        Log.d("qrComments", String.valueOf(qrComments));
                        commentsList.addAll(qrComments);
                    }
                }
            }

            updateCommentUI(commentsList);

        });
    }
}
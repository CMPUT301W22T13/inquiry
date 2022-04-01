package com.cmput301w22t13.inquiry.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;

public class MatchingQRActivity extends AppCompatActivity {
    Database db = new Database();

    String currentPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_qr);


        QRCode code = (QRCode) getIntent().getSerializableExtra("code");
        currentPlayer = (String) getIntent().getSerializableExtra("player");

        TextView qrName = findViewById(R.id.qr_name);
        qrName.setText(code.getName());

        // ends activity
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());




        Task<QuerySnapshot> querySnapshotTask = db.query("qr_codes", "hash", code.getHash());
        querySnapshotTask.addOnCompleteListener(task -> {
            List<DocumentSnapshot> documents = task.getResult().getDocuments();
            Log.d("size", String.valueOf(documents.size()));
            if (documents.size() == 1) {
                String id = documents.get(0).getId();
                findPlayers(id);
            }
        });

    }

    private void updateUI(ArrayList<String> list){
        ListView usernameList = findViewById(R.id.matchingQRList);

        ArrayAdapter<String> userList = new ArrayAdapter<String>(
                this, R.layout.matching_qr_list_content, list);
        usernameList.setAdapter(userList);
    }

    private void findPlayers(String id){
        ArrayList<String> namesList = new ArrayList<>();

        DocumentReference reference = db.getDocReference("qr_codes/"+id);
        db.arrayQuery("users","qr_codes",reference).addOnCompleteListener(task ->{
            if (task.isSuccessful()){
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                int size = documents.size();
                if (size!= 0) {
                    for (DocumentSnapshot document: documents) {
                        String username = (String) document.get("username");
                        namesList.add(username);
                        Log.d("Before: ", (String) document.get("username"));
                    }
                }
            }

            namesList.remove(currentPlayer);
            if (namesList.size() == 0) {
                TextView others = findViewById(R.id.others);
                others.setText("No other players have this QR code");
            }
            else {
                updateUI(namesList);
            }
        });
    }



}

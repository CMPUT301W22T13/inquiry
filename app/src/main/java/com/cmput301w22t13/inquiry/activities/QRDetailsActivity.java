package com.cmput301w22t13.inquiry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QRDetailsActivity extends AppCompatActivity {
    Database db = new Database();
    String currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_InQuiRy_NoActionBar);
        setContentView(R.layout.activity_qrdetails);

        QRCode code = (QRCode) getIntent().getSerializableExtra("code");
        currentPlayer = (String) getIntent().getSerializableExtra("player");

        TextView qrName = findViewById(R.id.myqrs_qr_name);
        qrName.setText(code.getName());

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


        // ends activity
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());





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
    }

    private void updateUI(ArrayList<String> list){
        ListView usernameList = findViewById(R.id.MatchingQrsList);

        ArrayAdapter<String> userList = new ArrayAdapter<String>(
                this, R.layout.matching_qr_list_content, list);
        usernameList.setAdapter(userList);
    }

    private void updateCommentUI(ArrayList<String> list){
        ListView usernameList = findViewById(R.id.comments_list);

        ArrayAdapter<String> commentList = new ArrayAdapter<String>(
                this, R.layout.comments_list_content, list);
        usernameList.setAdapter(commentList);
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
            updateUI(namesList);

        });
    }

    private void findComments(String qrDocumentId){
        ArrayList<String> commentsList = new ArrayList<>();

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

            commentsList.remove(currentPlayer);
            updateCommentUI(commentsList);

        });
    }
}
package com.cmput301w22t13.inquiry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class QRDetailsTest {

    private Database db;
    private QRCode code;


    @Before
    public void setUp(){
        db = new Database();
    }




    /**
     * Testing to see if the number of comments is correct
     */
    @Test
    public void test_comment_length(){
        Task<QuerySnapshot> queryTask = db.query("qr_codes", "hash", "de7d1b721a1e0632b7cf04edf5032c8ecffa9f9a08492152b926f1a5a7e765d7");
        queryTask.addOnCompleteListener(task -> {
            assertTrue(queryTask.isSuccessful());

            QuerySnapshot queryResults = queryTask.getResult();
            List<DocumentSnapshot> documents = queryResults.getDocuments();

            assertTrue(documents.size() != 0);

            DocumentSnapshot document = documents.get(0);
            String hash = (String) document.get("hash");
            Integer score =  (Integer) document.get("score");
            ArrayList comments = (ArrayList) document.get("comments");


            code = new QRCode(hash, score, null, null, comments);
            assertEquals(code.getComment().size(), 1);


        });


    }

    /**
     * Testing to see if the received comments are correct
     */
    @Test
    public void test_comment(){
        Task<QuerySnapshot> queryTask = db.query("qr_codes", "hash", "de7d1b721a1e0632b7cf04edf5032c8ecffa9f9a08492152b926f1a5a7e765d7");
        queryTask.addOnCompleteListener(task -> {
            assertTrue(queryTask.isSuccessful());

            QuerySnapshot queryResults = queryTask.getResult();
            List<DocumentSnapshot> documents = queryResults.getDocuments();

            assertTrue(documents.size() != 0);

            DocumentSnapshot document = documents.get(0);
            String hash = (String) document.get("hash");
            Integer score =  (Integer) document.get("score");
            ArrayList comments = (ArrayList) document.get("comments");


            code = new QRCode(hash, score, null, null, comments);
            assertEquals(code.getComment().get(0), "Cool QR!");


        });

    }

    @Test
    public void test_players(){
        Task<QuerySnapshot> querySnapshotTask = db.query("qr_codes", "hash", "de7d1b721a1e0632b7cf04edf5032c8ecffa9f9a08492152b926f1a5a7e765d7");
        querySnapshotTask.addOnCompleteListener(task -> {
            List<DocumentSnapshot> documents = task.getResult().getDocuments();
            Log.d("size", String.valueOf(documents.size()));
            if (documents.size() == 1) {
                String id = documents.get(0).getId();

                ArrayList<String> namesList = new ArrayList<>();
                DocumentReference reference = db.getDocReference("qr_codes/"+id);
                db.arrayQuery("users","qr_codes",reference).addOnCompleteListener(task2 ->{
                    if (task2.isSuccessful()){
                        List<DocumentSnapshot> docs = task2.getResult().getDocuments();
                        int size = docs.size();
                        if (size!= 0) {
                            for (DocumentSnapshot document: docs) {
                                String username = (String) document.get("username");
                                namesList.add(username);
                                Log.d("Before: ", (String) document.get("username"));
                            }
                        }
                    }

                    assertEquals(namesList.size(),3);

                });


            }
        });



    }
    public void test_player_values() {
        Task<QuerySnapshot> querySnapshotTask = db.query("qr_codes", "hash", "de7d1b721a1e0632b7cf04edf5032c8ecffa9f9a08492152b926f1a5a7e765d7");
        querySnapshotTask.addOnCompleteListener(task -> {
            List<DocumentSnapshot> documents = task.getResult().getDocuments();
            Log.d("size", String.valueOf(documents.size()));
            if (documents.size() == 1) {
                String id = documents.get(0).getId();

                ArrayList<String> namesList = new ArrayList<>();
                DocumentReference reference = db.getDocReference("qr_codes/" + id);
                db.arrayQuery("users", "qr_codes", reference).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        List<DocumentSnapshot> docs = task2.getResult().getDocuments();
                        int size = docs.size();
                        if (size != 0) {
                            for (DocumentSnapshot document : docs) {
                                String username = (String) document.get("username");
                                namesList.add(username);
                                Log.d("Before: ", (String) document.get("username"));
                            }
                        }
                    }

                    assertEquals(namesList.contains("x"),true);
                    assertEquals(namesList.contains("YaBoyIlia"),true);
                    assertEquals(namesList.contains("IllustriousSheep66"),true);
                });


            }
        });
    }

}

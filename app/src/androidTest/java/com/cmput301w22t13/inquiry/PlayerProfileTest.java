package com.cmput301w22t13.inquiry;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PlayerProfileTest {

    private Database db;

    @Before
    public void createDB(){
        db = new Database();
    }

    @Test
    public void testQuery(){
        Task<QuerySnapshot> queryTask = db.query("users", "username", "Unjustodin");
        queryTask.addOnCompleteListener(task -> {
            assertTrue(queryTask.isSuccessful());

            QuerySnapshot queryResults = queryTask.getResult();
            List<DocumentSnapshot> documents = queryResults.getDocuments();

            assertTrue(documents.size() != 0);

            DocumentSnapshot document = documents.get(0);
            String uid = (String) document.get("id");
            assertEquals("3JZriGLblWMN9IEMXvWzy90VDPg2", uid);

        });
    }

    @Test
    public void testGetUserData(){
        Task<QuerySnapshot> queryTask = db.query("users", "username", "Unjustodin");
        queryTask.addOnCompleteListener(task -> {
            assertTrue(queryTask.isSuccessful());

            QuerySnapshot queryResults = queryTask.getResult();
            List<DocumentSnapshot> documents = queryResults.getDocuments();

            assertTrue(documents.size() != 0);

            DocumentSnapshot document = documents.get(0);
            String uid = (String) document.get("id");
            assertEquals("3JZriGLblWMN9IEMXvWzy90VDPg2", uid);

            db.getById("users", uid).addOnCompleteListener(task2 -> {
                assertTrue(task2.isSuccessful());
                DocumentSnapshot document2 = task2.getResult();
                assertTrue(document != null && document.exists());
                String id = (String) document.get("id");
                Player player = new Player((String) document.get("username"), id);
                assertEquals(player.getUsername(), "Unjustodin");
                assertEquals(id, "3JZriGLblWMN9IEMXvWzy90VDPg2");
            });
        });
    }




}
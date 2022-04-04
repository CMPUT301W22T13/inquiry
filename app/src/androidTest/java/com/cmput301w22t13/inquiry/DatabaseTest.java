package com.cmput301w22t13.inquiry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private Database db;
    @Before
    public void setUp(){
        db = new Database();
    }
    @Test
    public void putTest(){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", "uid");
        newUser.put("username", "username2");
        db.put("users",newUser);
        db.getById("users","uid").addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertEquals(task.getResult().getId(), "uid");
            }
        });
    }
    @Test
    public void setTest(){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", "uid");
        newUser.put("username", "different name");
        db.set("users","uid", newUser);
    }
    @Test
    public void getByIdTest() {
        Map<String, Object> qrCode = new HashMap<>();
        qrCode.put("hash", "asd");
        qrCode.put("score", 5);
        qrCode.put("id", "qrid");
        db.put("qrCode",qrCode) ;
        db.getById("qr_codes","qrid").addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertEquals(task.getResult().get("hash"), "asd");
            }
        });
    }
    @Test
    public void removeTest(){
        db.remove("users","uid");
        db.getById("users","uid").addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertNotEquals(task.getResult().getId(), "uid");
            }
        });
    }

}

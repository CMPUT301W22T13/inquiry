package com.cmput301w22t13.inquiry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Owner;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class OwnerTest {
    private Database db;
    private Owner owner;
    private Player player;
    private QRCode qr;
    @Before
    public void setUp(){
        db = new Database();
        owner = new Owner("username","id");
        player = new Player("username2","uid");
        qr = new QRCode("asd",5,"qrid");
    }
    @Test
    public void isOwnerTest(){

        assertEquals(true,owner.getIsOwner());
    }
    @Test
    public void deletePlayerTest(){
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

        owner.deletePlayer(player);
        db.getById("users","uid").addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                assertNotEquals(task.getResult().getId(), "uid");
            }
        });
        }

        @Test
    public void deleteQRCodeTest(){

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

            owner.deleteQRCode(qr);
            db.getById("qr_codes","qrid").addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    assertNotEquals(task.getResult().get("hash"), "asd");
                }
            });

        }




}

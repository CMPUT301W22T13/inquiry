package com.cmput301w22t13.inquiry;

import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.firebase.firestore.DocumentSnapshot;

import static org.junit.Assert.assertEquals;


import android.util.Log;

import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerTest2 {
    private Player player;
    private Database db = new Database();
    @Before
    public void addQr(){
        Log.i("before","");
        player = new Player("AgentDeer", "tpqVCcHJFadszvgNfoG4r6aL3JS2",true);
        /*
        db.getById("users", "tpqVCcHJFadszvgNfoG4r6aL3JS2").addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.i("successful task 1", "");
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Log.i("successful task 2", "");
                    String username = (String) document.get("username");
                    String email = (String) document.get("email");
                    String uid = (String) document.get("id");

                    //player = new Player(username, uid, true);

                    }
                }
            else{
                Log.i("task not succesful","yeet");
            }

        });

         */
    }



    @Test
    public void testCount(){
        int totalQRs = player.getQRCodeCount();
        assertEquals(totalQRs, 2);
    }

    @Test
    public void testTotal(){
        assertEquals(player.getTotalScore(),4);

    }

    @Test
    public void testHighest(){

        assertEquals(4, player.getHighestScore());

    }

    @Test
    public void testLowest(){

        assertEquals(4, player.getLowestScore());

    }

    @Test
    public void testUsername(){
        assertEquals("AgentDeer",player.getUsername());
    }



}

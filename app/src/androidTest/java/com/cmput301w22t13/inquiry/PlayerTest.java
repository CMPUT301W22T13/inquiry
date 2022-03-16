package com.cmput301w22t13.inquiry;

import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.db.Database;

import static org.junit.Assert.assertEquals;


import android.util.Log;

import org.junit.Before;
import org.junit.Test;



public class PlayerTest {
    private Player player;
    private Database db = new Database();
    @Before
    public void addQr(){
        Log.i("before","");
        player = new Player("AgentDeer", "tpqVCcHJFadszvgNfoG4r6aL3JS2");




    }



    @Test
    public void testCount(){
        player.fetchQRCodes(qrCodes -> {
            int totalQRs = player.getQRCodeCount();
            assertEquals(totalQRs, 2);
        });

    }

    @Test
    public void testTotal(){
        player.fetchQRCodes(qrCodes -> {
            assertEquals(player.getTotalScore(),7);

        });


    }

    @Test
    public void testHighest(){

        player.fetchQRCodes(qrCodes -> {
            assertEquals(player.getHighestScore(),4);

        });
    }

    @Test
    public void testLowest(){

        player.fetchQRCodes(qrCodes -> {
            assertEquals(player.getLowestScore(),3);

        });
    }

    @Test
    public void testUsername(){
        assertEquals("AgentDeer",player.getUsername());
    }



}

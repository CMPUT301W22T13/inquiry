package com.cmput301w22t13.inquiry;

import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

public class PlayerTest {
    private Player player;
    private Database db;
    @BeforeEach
    public void addQr(){
       player = new Player("AgentDeer", "tpqVCcHJFadszvgNfoG4r6aL3JS2");
    }

    @Test
    public void testCount(){
        int totalQRs = player.getQRCodeCount();
        assertEquals(totalQRs, 1);
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

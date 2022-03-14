package com.cmput301w22t13.inquiry;

import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

public class PlayerTest {
    private Player player;
    private ArrayList<QRCode> codes;
    QRCode qr1;
    QRCode qr2;
    @BeforeEach
    public void addQr(){
         qr1 = new QRCode("hello1234");
         qr2 = new QRCode("hi5678");

        player.addQRCode(qr1);
        player.addQRCode(qr2);
        codes = player.getQRCodes();
    }

    @Test
    public void testCount(){
        int totalQRs = player.getQRCodeCount();
        assertEquals(totalQRs, 2);
    }

    @Test
    public void testTotal(){
        int totalScore = qr1.getScore() + qr2.getScore();
        assertEquals(player.getTotalScore(),totalScore);

    }

    @Test
    public void testHighest(){
        int highestScore;
        if (qr1.getScore()>qr2.getScore()){
            highestScore = qr1.getScore();
        }
        else{
            highestScore = qr2.getScore();
        }
        assertEquals(highestScore, player.getHighestScore());

    }

    @Test
    public void testLowest(){
        int lowestScore;
        if (qr1.getScore()<qr2.getScore()){
            lowestScore = qr1.getScore();
        }
        else{
            lowestScore = qr2.getScore();
        }
        assertEquals(lowestScore, player.getLowestScore());

    }


}

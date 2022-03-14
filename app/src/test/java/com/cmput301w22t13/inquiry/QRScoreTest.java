package com.cmput301w22t13.inquiry;

import static org.junit.Assert.assertEquals;

import com.cmput301w22t13.inquiry.classes.QRCode;

import org.junit.Test;

/**
 * this tests that the score provided is correct
 */
public class QRScoreTest {

    /**
     * tests that the calculation performed for score is correct
     */
    @Test
    public void scoreIsCorrect() {
        QRCode qr = new QRCode("abc");
        int score = qr.createScore("hhh11");
        assertEquals(56,score);
        score = qr.createScore("0000055555fffff");
        assertEquals(157589,score);
        score = qr.createScore("abcdef");
        assertEquals(0,score);

    }
}

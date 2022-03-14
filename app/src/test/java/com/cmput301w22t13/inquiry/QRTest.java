package com.cmput301w22t13.inquiry;

import static org.junit.Assert.assertEquals;

import com.cmput301w22t13.inquiry.classes.QRCode;

import org.junit.Test;

public class QRTest {

    public class ExampleUnitTest {
        @Test
        public void addition_isCorrect() {
            QRCode qr = new QRCode("abc","someUser");
            int score = qr.getScore();
            assertEquals("edeaaff3f1774ad2888673770c6d64097e391bc362d7d6fb34982ddf0efd18cb",qr.getHash());
            assertEquals(0,score);

        }
    }
}

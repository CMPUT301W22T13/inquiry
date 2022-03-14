package com.cmput301w22t13.inquiry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.cmput301w22t13.inquiry.classes.QRName;

import org.junit.Test;

/**
 * This class tests hashing and name generation (QRName class).
 */
public class QRNameTest {

    /**
     * Tests whether the hash generation is truly SHA256.
     */
    @Test
    public void shaHashIsCorrect() {
        // test values taken from an online generator: https://passwordsgenerator.net/sha256-hash-generator/
        assertEquals(QRName.getHash("Hello world!"), "c0535e4be2b79ffd93291305436bf889314e4a3faec05ecffcbb7df31ad9e51a");
        assertEquals(QRName.getHash("Welcome 2 CMPUT 301"), "a9d25e879ab543cb5fa50d77871e69d3f4a2e6b6be7e7d2333303aa8abeb6f3b");
        assertEquals(QRName.getHash("password"), "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
    }

    /**
     * Tests if name generation has 2 values.
     */
    @Test
    public void twoWords() {
        assertEquals(QRName.fromText("Hello there").split("\\s+").length, 2);
    }

    /**
     * Tests if name generation creates the same name for same word using hash
     */
    @Test
    public void sameName() {
        assertEquals(QRName.fromHash("8f8ea9236ea6a2e02555faace47dc1e79a6524f148cb0556ac1873b6412c9c76"), QRName.fromText("yellow star"));
    }

    /**
     * Test if different values (even if very similar) give different names
     */
    @Test
    public void diffNames() {
        assertNotEquals(QRName.fromText("CMPUT"), QRName.fromText("301"));
        assertNotEquals(QRName.fromText("happy"), QRName.fromText("Happy"));
        assertNotEquals(QRName.fromText("SAD"), QRName.fromText("SAD!"));
    }

    /**
     * Test if an empty value raises an issue for fromText()
     */
    @Test(expected = IllegalArgumentException.class)
    public void emptyNameFromText() {
        QRName.fromText("");
    }


    /**
     * Test if an empty value raises an issue for getHash().
     */
    @Test(expected = IllegalArgumentException.class)
    public void emptyNameGetHash() {
        QRName.getHash("");
    }


    /**
     * Test if an empty value raises an issue for fromHash().
     */
    @Test(expected = IllegalArgumentException.class)
    public void emptyHashFromHash() {
        QRName.fromHash("");
    }

    /**
     * Test if a wrong length input as a hash raises an error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void incorrectLengthHash1() {
        QRName.fromHash("233");
    }

    /**
     * Test if a wrong length input as a hash raises an error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void incorrectLengthHash2() {
        QRName.fromHash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    /**
     * Test if name is capitalized properly.
     */
    @Test
    public void capitalization() {
        String[] name = QRName.fromText("Hello sire.").split("\\s+");
        for (int i = 0; i < 2; i++) assertEquals(name[i].substring(0, 1), name[i].substring(0, 1).toUpperCase());
        for (int i = 0; i < 2; i++) assertEquals(name[i].substring(1), name[i].substring(1).toLowerCase());
    }
}

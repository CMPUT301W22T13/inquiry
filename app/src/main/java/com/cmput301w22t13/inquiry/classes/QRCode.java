package com.cmput301w22t13.inquiry.classes;
/**
 * Calculates a SHA-256 hash of the QR code content
 * Saves scanned qr code to the firestore dabatase
 */


import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;


public class QRCode {
    private final String hash;
    private int score;

    Database db = new Database();

    /**
     * Initalized the QRCode with a sha-256 hash using the input string
     *
     * @param text input text that was gotten from the QR code
     */
    public QRCode(String text) {
        this.hash = QRName.getHash(text);
        this.score = createScore(this.hash);
    }

    public int createScore(String str) {
        int currentScore = 0;
        char prevChar = 'z';
        for (int i = 0; i < str.length(); i++) {
            int tempScore = 0;
            char c = str.charAt(i);
            if (tempScore == 0 && c == prevChar) {
                tempScore += 1;
            } else if (c == prevChar) {
                tempScore *= (int) c;
            } else {
                currentScore += tempScore;
                tempScore = 0;
            }
            prevChar = c;

        }
        return currentScore;

    }

    public String getHash() {
        return this.hash;

    }

    /**
     * Saves the given hash into a collection owned by user
     */
    public void save() {
        Map<String, Object> qrCode = new HashMap<>();
        qrCode.put("hash", this.hash);

        FirebaseUser currentUser = Auth.getCurrentUser();
        if (currentUser != null) {
            String id = currentUser.getUid();
            db.addToCollection("users", "hashes", id, qrCode);

        }
//        db.update("users", this.uid, "qr_codes");
    }

    /**
     * @return the score that was calculated
     */
    public int getScore() {
        return this.score;
    }

    /**
     * @return the name of the qr code
     */
    public String getName() { 
        return QRName.fromHash(this.hash);
    }
}

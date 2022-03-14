package com.cmput301w22t13.inquiry.classes;
/**
 * Calculates a SHA-256 hash of the QR code content
 * Saves scanned qr code to the firestore dabatase
 */


import android.util.Log;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

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

    public QRCode(String hash, int score) {
        this.hash = hash;
        this.score = score;
    }

    public int createScore(String str) {
        int currentScore = 0;
        char prevChar = 'z';
        int tempScore = 0;
        for (int i = 0; i < str.length(); i++) {
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
        // create a map of the data to be saved
        Map<String, Object> qrCode = new HashMap<>();
        qrCode.put("hash", this.hash);
        qrCode.put("score", this.score);

        FirebaseUser currentUser = Auth.getCurrentUser();
        if (currentUser != null) {
            String id = currentUser.getUid();

            // save the QR code to the qr_codes collection, then save a reference to it in the user's document
            db.put("qr_codes", qrCode).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
//                    Log.i("QRCode", "QRCode saved successfully");

                    // get document reference from the qr code's id
                    String qrDocumentId = task.getResult().getId();
                    DocumentReference qrRef = db.getDocReference("qr_codes/" + qrDocumentId);

                    // append the qr code's reference to the user's qr_codes array
                    // see: stackoverflow.com/a/51983589/12955797
                    Map<String, Object> userQrCode = new HashMap<>();
                    userQrCode.put("qr_codes", FieldValue.arrayUnion(qrRef));
                    db.update("users", id, userQrCode);
                }
            });
        }
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

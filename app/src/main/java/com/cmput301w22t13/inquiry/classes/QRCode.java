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
    private final String uid;
    private int score;

    Database db = new Database();

    /**
     * Initalized the QRCode with a sha-256 hash using the input string
     * @param text input text that was gotten from the QR code
     * @param uid input for the users id which created the QRCode
     */
    public QRCode(String text, String uid){
        this.hash = Hashing.sha256()
                .hashString(text, StandardCharsets.UTF_8)
                .toString();

        this.uid = uid;
    }

    /**
     *  Saves the given hash into a collection owned by user
     */
    public void save(){
        Map<String, Object> qrCode = new HashMap<>();
        qrCode.put("hash", this.hash);

        FirebaseUser currentUser = Auth.getCurrentUser();
        if( currentUser != null){
            String id = currentUser.getUid();
            db.addToCollection("users","hashes" , id,qrCode);

        }
//        db.update("users", this.uid, "qr_codes");
    }

    /**
     *
     * @return the score that was calculated
     */
    public int getScore(){
        return this.score;
    }
}

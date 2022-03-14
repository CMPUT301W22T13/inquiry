package com.cmput301w22t13.inquiry.classes;
/**
 * Calculates a SHA-256 hash of the QR code content
 * Saves scanned qr code to the firestore dabatase
 */

import com.cmput301w22t13.inquiry.db.Database;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QRCode {
    private final String hash;
    private final String uid;
    private int score;

    Database db = new Database();

    public QRCode(String text, String uid){
        this.hash = Hashing.sha256()
                .hashString(text, StandardCharsets.UTF_8)
                .toString();

        this.uid = uid;
    }
    public void save(){
        Map<String, Object> qrCode = new HashMap<>();
        qrCode.put("hash", this.hash);
        db.put("qr_codes", qrCode);
//        db.update("users", this.uid, "qr_codes");
    }

    public int getScore(){
        return this.score;
    }
}

package com.cmput301w22t13.inquiry.classes;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class QRCode {
    private String hash;
    private int score;
    public QRCode(String text){
        this.hash = Hashing.sha256()
                .hashString(text, StandardCharsets.UTF_8)
                .toString();

    }
    public String getHash(){
        return this.hash;
    }
    public void Save(){
        /*
        HashMap code = new HashMap<String, Object>();
        code.put("hash", this.hash);
        Database db = new Database();
        db.put("QRCodes", this);
        */
        // if someone could work on this and get the QRCode to save, that would be appreciated
    }
    public int getScore(){
        return this.score;
    }
}

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
        this.score = createScore(this.hash);
    }
    public int createScore(String str){
        int currentScore = 0;
        char prevChar = 'z';
        for(int i=0;i<str.length();i++){
            int tempScore = 0;
            char c = str.charAt(i);
            if(tempScore==0 && c == prevChar){
                tempScore += 1;
            }else if(c==prevChar){
                tempScore *= (int) c;
            }else{
                currentScore += tempScore;
                tempScore = 0;
            }
            prevChar = c;

        }
        return currentScore;

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

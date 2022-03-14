package com.cmput301w22t13.inquiry.classes;

import com.cmput301w22t13.inquiry.db.Database;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Player {

    private String uid;
    private String userName;
    private String email;

    Database db = new Database();

    public Player(String userName, String uid) {
        this.userName = userName;
        this.uid = uid;
        this.email = "";
    }

    public Player(String userName, String uid, String email) {
        this.userName = userName;
        this.uid = uid;
        if(email != null) {
            this.email = email;
        }
    }

    public ArrayList<QRCode> getQRCodes() {
        ArrayList<QRCode> QrList = new ArrayList<>();

        db.getFieldById("users", this.uid, "qr_codes").addOnCompleteListener(userQRs -> {
            if (userQRs.isSuccessful()) {
                for(Object qrId : Objects.requireNonNull(userQRs.getResult().getData()).values()) {
                    db.getById("qr_codes", qrId.toString()).addOnCompleteListener(qrDoc -> {
                        if (qrDoc.isSuccessful()) {
                            Map<String, Object> qr = qrDoc.getResult().getData();
                            QrList.add(new QRCode(Objects.requireNonNull(qr).get("hash").toString(), Integer.parseInt(qr.get("score").toString())));
                        }
                    });
                }
            }
        });

        if(QrList.size() == 0) {
            return null;
        }
        return QrList;
    }

    public String getUsername() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRank() {
        // returns Rank of player
        return 0;
    }

    public int getTotalScore() {
        // returns total score of QRCodes from database


        ArrayList<QRCode> QrList = getQRCodes();

        int totalScore = 0;
        for (int i = 0; i < QrList.size(); i++) {
            QRCode code = QrList.get(i);
            totalScore = totalScore + code.getScore();
        }
        return totalScore;
    }

    public int getHighestScore() {
        // returns highest score QRCode from database
        ArrayList<QRCode> QrList = getQRCodes();

        int maxScore = 0;
        for (int i = 0; i < QrList.size(); i++) {
            QRCode code = QrList.get(i);
            int score = code.getScore();
            if (score > maxScore) {
                maxScore = score;
            }
        }

        return 0;
    }

    public int getLowestScore() {
        // returns highest score QRCode from database
        ArrayList<QRCode> QrList = getQRCodes();

        int minScore = QrList.get(0).getScore();
        for (int i = 1; i < QrList.size(); i++) {
            QRCode code = QrList.get(i);
            int score = code.getScore();
            if (score < minScore) {
                minScore = score;
            }
        }
        return minScore;
    }

    public int getQRCodeCount() {
        // returns amount of QRCodes scanned by player from database

        ArrayList<QRCode> QrList = getQRCodes();


        return QrList.size();
    }

    // updates user data in database
    public void updateUser(Map<String, Object> userData) {
        db.update("users", this.uid, userData);
    }
}

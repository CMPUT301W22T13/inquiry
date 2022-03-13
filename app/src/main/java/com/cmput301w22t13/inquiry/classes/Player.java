package com.cmput301w22t13.inquiry.classes;

import com.cmput301w22t13.inquiry.db.Database;

import java.util.ArrayList;

public class Player {
    private String userName;
    private int pId;

    Database db = new Database();
    public Player(String userName){
        this.userName = userName;
    }

    public void addQRCode(String hash) {
        // add to database
    }

    public ArrayList<QRCode> getQRCodes(){
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public int getRank(){
        // returns Rank of player
        return 0;
    }
    public int getTotalScore(){
        // returns total score of QRCodes from database


        ArrayList<QRCode> QrList = getQRCodes();

        int totalScore = 0;
        for (int i = 0;i<QrList.size();i++){
            QRCode code = QrList.get(i);
            totalScore = totalScore + code.getScore();
        }
        return totalScore;
    }

    public int getHighestScore(){
        // returns highest score QRCode from database
        ArrayList<QRCode> QrList = getQRCodes();

        int maxScore = 0;
        for (int i = 0;i<QrList.size();i++){
            QRCode code = QrList.get(i);
            int score = code.getScore();
            if (score > maxScore){
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

    public int getQRCodeCount(){
        // returns amount of QRCodes scanned by player from database

        ArrayList<QRCode> QrList = getQRCodes();


        return QrList.size();
    }


}

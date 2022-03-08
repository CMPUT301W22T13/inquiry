package com.cmput301w22t13.inquiry.classes;

import java.util.ArrayList;

public class Player {
    private String userName;
    private int pId;
    public Player(String userName){
        this.userName = userName;
    }
    public void addQRCode(String hash) {
        // add to database
    }
    public ArrayList<QRCode> getQRCodes(){
        // get Players QRCodes
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
        return 0;
    }

    public int getHighestScore(){
        // returns highest score QRCode from database
        return 0;
    }

    public int getLowestScore(){
        // returns highest score QRCode from database
        return 0;
    }

    public int getQRCodeCount(){
        // returns amount of QRCodes scanned by player from database
        return 0;
    }

    
}

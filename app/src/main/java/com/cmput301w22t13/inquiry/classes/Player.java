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
}

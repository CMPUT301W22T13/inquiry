package com.cmput301w22t13.inquiry.classes;


import com.cmput301w22t13.inquiry.db.Database;

public class Owner extends Player{


    public Owner(String userName, String uid) {
        super(userName, uid);
    }
    public Owner(String userName, String uid, String email) {
        super(userName,uid,email);
    }

    public Owner(String userName, String uid, Boolean getQrCodes) {
        super(userName,uid,getQrCodes);
    }

    public boolean getIsOwner(){
        return true;
    }
    public void deleteQR(QRCode qr){
        Database db = new Database();
        String hash = qr.getHash();
        db.remove("qr_codes", hash);
        // need to add delete from players list

    }
    public void deletePlayer(Player player){
        Database db = new Database();
        String newId = player.getID();
        db.remove("users", newId);

    }
}

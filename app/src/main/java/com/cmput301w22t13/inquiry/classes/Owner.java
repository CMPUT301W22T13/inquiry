package com.cmput301w22t13.inquiry.classes;

import com.cmput301w22t13.inquiry.db.Database;

public class Owner extends Player{

    private Boolean isOwner = true;

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
        return this.isOwner;
    }
    public void deleteQR(QRCode qr){
        String hash = qr.getHash();
        //db.remove("qr_codes", hash);
        // need to add delete from players list

    }
    public void deletePlayer(Player player){
        String newId = player.getID();
        //db.remove("users", newId);

    }
}

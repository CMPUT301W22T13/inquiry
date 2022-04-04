package com.cmput301w22t13.inquiry.classes;


import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.db.Database;
import com.cmput301w22t13.inquiry.db.onQrDataListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Owner class with elevated privileges and modified methods
 */
public class Owner extends Player{

    private ArrayList<QRCode> qrCodes;
    public Owner(String userName, String uid) {
        super(userName, uid);
    }
    public Owner(String userName, String uid, String email) {
        super(userName,uid,email);
    }

    public Owner(String userName, String uid, Boolean getQrCodes) {
        super(userName,uid,getQrCodes);
    }

    /**
     * returns a boolean check if it is Owner
     * @return returns true as it is a Owner
     */
    public boolean getIsOwner(){
        return true;
    }

    /**
     * delete a player
     * @param player player object to be deleted
     */
    public void deletePlayer(Player player){
        Database db = new Database();
        db.remove("user_accounts", player.getUsername());

    }

    /**
     * fetch all qr codes in the game
     * @param onSuccess checks for when QRCode has been returned
     */
    public void fetchQRCodes(onQrDataListener onSuccess) {
        Log.d("VERBS","Fetching");
        ArrayList<QRCode> QrList = new ArrayList<>();

        Database db = new Database();

        db.getCollection("qr_codes").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                QRCode qrcode = new QRCode((String) document.getData().get("hash"), ((Long)document.getData().get("score")).intValue(),document.getId());

                                if(document.getDouble("lat") != null && document.getDouble("lng") != null) {
                                    double latitude = (double) document.getData().get("lat");
                                    double longitude = (double) document.getData().get("lng");

                                    if (latitude != 0 && longitude != 0) {
                                        qrcode.setLocation(latitude, longitude);
                                    }
                                }

                                QrList.add(qrcode);
                            }
                            onSuccess.getQrData(QrList);
                            Auth auth = new Auth();
                            Owner owner = (Owner) Auth.getPlayer();
                            owner.setQrCodes(QrList);
                        } else {
                            Log.d("VERBS","had issues");
                        }
                    }
                });

    }

    public void setQrCodes(ArrayList<QRCode> qrcodes){
        this.qrCodes = qrcodes;
    }

    /**
     * delete a given QRCode from both the collection and all qrcode lists
     * @param qrCode QRCode object to be deleted
     */
    public void deleteQRCode(QRCode qrCode){
        Database db = new Database();
        Log.d("VERBS",qrCode.getHash());
        //db.remove("qr_codes",qrCode.getId());

        DocumentReference qrRef = db.getDocReference("qr_codes/" + qrCode.getId());
        Log.d("VERBS","Hello there");
        final Map<String, Object> qrCodesFieldArray = new HashMap<>();
        qrCodesFieldArray.put("qr_codes", FieldValue.arrayRemove(qrRef));

        db.getCollection("user_accounts").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    db.update("user_accounts", document.getId(), qrCodesFieldArray);
                }
            } else {
                Log.d("VERBS","had issues");
            }
        });

    }
}

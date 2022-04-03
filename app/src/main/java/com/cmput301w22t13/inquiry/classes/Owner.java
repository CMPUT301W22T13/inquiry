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

    public void deleteQRCode(QRCode qrCode){
        Database db = new Database();
        Log.d("VERBS",qrCode.getHash());
        //db.remove("qr_codes",qrCode.getId());

        DocumentReference qrRef = db.getDocReference("qr_codes/" + qrCode.getId());
        Log.d("VERBS","Hello there");
        final Map<String, Object> qrCodesFieldArray = new HashMap<>();
        qrCodesFieldArray.put("qr_codes", FieldValue.arrayRemove(qrRef));

        db.getCollection("users").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.update("users", (String) document.getData().get("id"), qrCodesFieldArray);
                    }
                } else {
                    Log.d("VERBS","had issues");
                }
            }
        });

    }
}

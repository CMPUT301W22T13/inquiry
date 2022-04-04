package com.cmput301w22t13.inquiry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w22t13.inquiry.classes.Owner;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

public class OwnerTest {

    @Test
    public void isOwnerTrue() {
        Owner owner = new Owner("username","id");
        assertEquals(owner.getIsOwner(),true);


    }

    @Test
    public void deleteQRWorks() {
        Owner owner = new Owner("username","id");
        QRCode QR = new QRCode("testing");
        Database db = new Database();
        QR.save();
        final String[] id = {"'"};
        String hash = QR.getHash();
        db.getCollection("qr_codes").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (hash == document.getData().get("hash")){
                            id[0] = (String) document.getId();
                        }
                    }
                } else {
                    Log.d("VERBS","had issues");
                }
            }
        });
        owner.deleteQRCode(QR);
        db.getCollection("qr_codes").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        assertNotEquals(hash, document.getData().get("hash"));
                    }
                } else {
                    Log.d("VERBS","had issues");
                }
            }
        });

        db.getCollection("users").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        assertNotEquals(document.getId(),id);
                    }
                } else {
                    Log.d("VERBS","had issues");
                }
            }
        });
    }

}

package com.cmput301w22t13.inquiry.db;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Put a new document in a specified collection.
     *
     * Example usage:
     *     Map<String, Object> user = new HashMap<>();
     *     user.put("first", "Ada");
     *     user.put("last", "Lovelace");
     *     user.put("born", 1815);
     *
     *     Database db = new Database();
     *     db.put("users", user);
     *
     * @param  collection  the name of the collection, e.g. "users"
     * @param  data the document to create in the collection, must be of type Map<String, Object>
     */
    public void put(String collection, Map<String, Object> data) {
        this.db.collection(collection)
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Log.w(TAG, "Error adding document", e);
                }
            });
    }
}

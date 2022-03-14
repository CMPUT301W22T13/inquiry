package com.cmput301w22t13.inquiry.db;
//collection of general methods, being called whenever we need the base

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Put a new document in a specified collection with a randomly generated ID
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

    /**
     * Put a new document in a specified collection with a specified ID
     * If a non-unique ID is passed in, the old document will get overwritten
     *
     * Example usage:
     *     Map<String, Object> user = new HashMap<>();
     *     user.put("first", "Ada");
     *     user.put("last", "Lovelace");
     *     user.put("born", 1815);
     *
     *     Database db = new Database();
     *     db.put("users", "12345", user); // specify a unique document ID
     *
     * @param  collection  the name of the collection, e.g. "users"
     * @param  id custom id to assign to the document, ensure this is a unique ID otherwise data loss can occur
     * @param  data the document to create in the collection, must be of type Map<String, Object>
     */
    public void set(String collection, String id, Map<String, Object> data) {
        this.db.collection(collection)
                .document(id).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * get a document in a specified collection using its ID
     *
     * @param  collection  the name of the collection, e.g. "users"
     * @param  id the String id of the document to get
     */
    public Task<DocumentSnapshot> getById(String collection, String id) {

        return db.collection(collection).document(id).get();

    }


    /**
     * update a document in a specified collection using its ID
     */
    public void update(String collection, String id, Map<String, Object> data) {
        this.db.collection(collection)
                .document(id)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}

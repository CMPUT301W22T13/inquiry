package com.cmput301w22t13.inquiry.db;
//collection of general methods, being called whenever we need the base

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Owner;
import com.cmput301w22t13.inquiry.classes.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Database implements Serializable {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Put a new document in a specified collection with a randomly generated ID
     * <p>
     * Example usage:
     * Map<String, Object> user = new HashMap<>();
     * user.put("first", "Ada");
     * user.put("last", "Lovelace");
     * user.put("born", 1815);
     * <p>
     * Database db = new Database();
     * db.put("users", user);
     *
     * @param collection the name of the collection, e.g. "users"
     * @param data       the document to create in the collection, must be of type Map<String, Object>
     */
    public Task<DocumentReference> put(String collection, Map<String, Object> data) {
        return this.db.collection(collection)
                .add(data);

    }

    /**
     * Put a new document in a specified collection with a specified ID
     * If a non-unique ID is passed in, the old document will get overwritten
     *
     * Example usage:
     * Map<String, Object> user = new HashMap<>();
     * user.put("first", "Ada");
     * user.put("last", "Lovelace");
     * user.put("born", 1815);
     *
     * Database db = new Database();
     * db.put("users", "12345", user); // specify a unique document ID
     *
     * @param collection the name of the collection, e.g. "users"
     * @param id         custom id to assign to the document, ensure this is a unique ID otherwise data loss can occur
     * @param data       the document to create in the collection, must be of type Map<String, Object>
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
     * @param collection the name of the collection, e.g. "users"
     * @param id         the String id of the document to get
     */
    public Task<DocumentSnapshot> getById(String collection, String id) {
        return db.collection(collection).document(id).get();
    }

    /**
     * get a document in a specified collection using its ID
     *
     * @param collection the name of the collection, e.g. "users"
     * @param id         the String id of the document to get
     */
//    public Task<DocumentSnapshot> getFieldById(String collection, String id, String field) {
//        Task<DocumentSnapshot> task = db.collection(collection).document(id).get();
//        if(task.isSuccessful()) {
//            DocumentSnapshot document = task.getResult();
//            if(document.exists()) {
//                return (Task<DocumentSnapshot>) document.get(field);
//            }
//        }
//    }

    /**
     * get a reference to a document
     *
     * @param docPath the path to the document, e.g. "users/12345"
     */
    public DocumentReference getDocReference(String docPath) {
        return db.document(docPath);
    }


    /**
     * query a collection for documents matching a specified field
     *
     * @param collection the name of the collection, e.g. "users"
     * @param field      the name of the field to query, e.g. "username"
     * @param value      the query value, e.g. "Ada"
     * @return a QuerySnapshot of the documents matching the query (usage: query(...).addOnSuccessListener(...))
     */
    public Task<QuerySnapshot> query(String collection, String field, String value) {
        return db.collection(collection).whereEqualTo(field, value).get();
    }


    /**
     * query a collection for documents matching a specified field array
     *
     * @param collection the name of the collection, e.g. "users"
     * @param field      the name of the array field to query, e.g. "qr_codes"
     * @param value      the query value, e.g. "Ada"
     * @return a QuerySnapshot of the documents matching the query (usage: query(...).addOnSuccessListener(...))
     */
    public Task<QuerySnapshot> arrayQuery(String collection, String field, DocumentReference value){
        return db.collection(collection).whereArrayContains(field,value).get();
    }

    /**
     * update a document in a specified collection using its ID
     *
     * @param collection the name of the collection, e.g. "users"
     * @param id         the String id of the document to update
     * @param data       the new data to update the document, must be of type Map<String, Object>
     *                   note: this will overwrite the existing data
     */
    public void update(String collection, String id, Map<String, Object> data) {
        Log.d("DATABASE", "id: " + id);
        Log.d("DATABASE", "update: " + data);
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
                        Log.w("DATABSE", "Error updating document", e);
                    }
                });
    }

    /**
     * remove an item from a collection based on id
     * @param collection collection to be specified
     * @param id id of the item from the collection
     */
    public void remove(String collection,String id){
       this.db.collection(collection).document(id).delete()
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

    /**
     * returns a Task Query Snapshot from a collection
     * @param collection collection to get items frmo
     * @return returns a collection of the collections elements
     */
    public Task<QuerySnapshot> getCollection(String collection){
        return this.db.collection(collection).get();

    }

}

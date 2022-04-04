package com.cmput301w22t13.inquiry.classes;
/**
 * Calculates a SHA-256 hash of the QR code content
 * Saves scanned qr code to the firestore database
 */

import android.util.Log;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.db.Database;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoFireUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to represent a QR code
 */
public class QRCode implements Serializable {
    private final String hash;
    private int score;
    private LatLng location;
    private String id;
    private String locationImage;
    private ArrayList comments;


    /**
     * Initalized the QRCode with a sha-256 hash using the input string
     *
     * @param text input text that was gotten from the QR code
     */
    public QRCode(String text) {
        this.hash = QRName.getHash(text);
        this.score = createScore(this.hash);
    }

    public QRCode(String text, LatLng location) {
        this.hash = QRName.getHash(text);
        this.score = createScore(this.hash);
        this.location = location;
    }
    public QRCode(String hash, int score) {
        this.hash = hash;
        this.score = score;
    }

    public QRCode(String hash, int score, LatLng location) {
        this.hash = hash;
        this.score = score;
        this.location = location;
    }

    public QRCode(String hash, int score, String id) {
        this.hash = hash;
        this.score = score;
        this.id = id;
    }

    public QRCode(String hash, int score, String id, LatLng location) {
        this.hash = hash;
        this.score = score;
        this.id = id;
        this.location = location;
    }

    public QRCode(String hash, int score, String id, String location_image) {
        this.hash = hash;
        this.score = score;
        this.id = id;
        this.locationImage = location_image;
    }

    public QRCode(String hash, int score, String id, String location_image, ArrayList comment) {
        this.hash = hash;
        this.score = score;
        this.id = id;
        this.locationImage = location_image;
        this.comments = comments;
    }


    /**
     * Generate score from a string
     * @param str input hash
     * @return score
     */
    public int createScore(String str) {
        int currentScore = 0;
        char prevChar = 'z';
        int tempScore = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (tempScore == 0 && c == prevChar) {
                tempScore += 1;
            } else if (c == prevChar) {
                tempScore *= (int) c - 48;
            } else {
                currentScore += tempScore;
                tempScore = 0;
            }
            prevChar = c;

        }
        currentScore += tempScore;
        return currentScore;

    }

    /**
     * Get hash
     * @return hash
     */
    public String getHash() {
        return this.hash;
    }

    /**
     * Get location
     * @return location
     */
    public LatLng getLocation() { return this.location; }

    /**
     * Get location image
     * @return location image as a string
     * */
    public String getLocationImage() { return this.locationImage; }

    /**
     * Set location
     * @param location location
     */
    public void setLocation(LatLng location) { this.location = location; }

    /**
     * Get comments
     * @return comments
     */
    public ArrayList getComment(){ return this.comments = comments; }

    /**
     * Saves the given hash into a collection owned by user
     * before saving, check if a qr code with the same hash already exists
     */
    public void save() {

        Database db = new Database();

        // create a map of the data to be saved
        Map<String, Object> qrCode = new HashMap<>();
        qrCode.put("hash", this.hash);
        qrCode.put("score", this.score);

        // For location, we need to add a "geohash" -- added by Rajan
        if (location != null) {
            qrCode.put("lat", location.latitude);
            qrCode.put("lng", location.longitude);
            GeoLocation loc = new GeoLocation(location.latitude, location.longitude);
            qrCode.put("geohash", GeoFireUtils.getGeoHashForLocation(loc));
        }

        Player user = Auth.getPlayer();
        if (user != null) {
            Log.d("QRCode", "Saving qr code");

            // check if the qr code already exists
            Task<QuerySnapshot> qrQuery = db.query("qr_codes", "hash", this.hash);
            qrQuery.addOnCompleteListener(qrTask -> {
                if (qrQuery.isComplete()) {
                    Log.d("QRCode", "QR code query complete");
                    List<DocumentSnapshot> qrList = qrQuery.getResult().getDocuments();
                    // if it does, just add the qr to the user
                    if (qrList.size() > 0) {
                        String qrDocumentId = qrList.get(0).getId();
                        DocumentReference qrRef = db.getDocReference("qr_codes/" + qrDocumentId);
                        user.addQRCode(qrRef, this.hash);
                        Log.d("QRCode", "QR code already exists, adding to user");
                    }
                    // otherwise, create a new qr code and then add it to the user
                    else {
                        Log.d("QRCode", "QR code does not exist, creating new qr code");
                        // save the QR code to the qr_codes collection, then save a reference to it in the user's document
                        db.put("qr_codes", qrCode).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // get document reference from the qr code's id
                                String qrDocumentId = task.getResult().getId();
                                DocumentReference qrRef = db.getDocReference("qr_codes/" + qrDocumentId);
                                user.addQRCode(qrRef, this.hash);
                                Log.d("QRCode", "new QR code saved, adding to user");
                            }
                        });
                    }
                }
            });

        }
    }

    /**
     * @return the score that was calculated
     */
    public Integer getScore() {
        return this.score;
    }

    /**
     * @return the name of the qr code
     */
    public String getName() {
        return QRName.fromHash(this.hash);
    }

    /**
     * @return the id of the qr code
     */
    public String getId() {
        return this.id;
    }
}

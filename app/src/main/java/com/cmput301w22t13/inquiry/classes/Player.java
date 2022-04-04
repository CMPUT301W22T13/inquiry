package com.cmput301w22t13.inquiry.classes;

import android.util.Log;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.db.Database;
import com.cmput301w22t13.inquiry.db.onQrDataListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Player implements Serializable {

    private String uid;
    private String userName;
    private String email;
    private ArrayList<QRCode> qrCodes = new ArrayList<>();
    private int rank = 0;

    private final boolean isOwner;

    public Player(String userName, String uid) {
        this.userName = userName;
        this.uid = uid;
        this.email = "";
        this.isOwner = false;
    }

    public Player(String userName, String uid, String email) {
        this.userName = userName;
        this.uid = uid;
        if (email != null) {
            this.email = email;
        }
        this.isOwner = false;
    }

    public Player(String userName, String uid, Boolean getQrCodes) {
        this.userName = userName;
        this.uid = uid;
        if (getQrCodes) {
            fetchQRCodes(qrCodes1 -> {
                this.qrCodes = qrCodes1;
            });
        }
        this.isOwner = false;
    }

    public String getID() {
        return this.uid;
    }

    /**
     * store a new new QRCode reference to the user's qr_codes field array
     * first checks if the QRCode already exists in the database
     *
     * @param newQrRef the DocumentReference of the QRCode to be stored
     */
    public void addQRCode(DocumentReference newQrRef, String hash) {
        // append the qr code's reference to the user's qr_codes array
        // see: stackoverflow.com/a/51983589/12955797
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("qr_codes", FieldValue.arrayUnion(newQrRef));

        Database db = new Database();

        Log.d("addQRCode", "adding qr code to user...");
        this.fetchQRCodes(userQrs -> {
            Log.d("QRCode", "After fetchQRCodes");
            if (userQrs.size() > 0) {
                for (QRCode qr : userQrs) {
                    if (qr.getHash().equals(hash)) {
                        Log.d("QRCode", "QRCode already exists in user");
                    } else {
                        db.update("user_accounts", this.userName, userMap);
                        Log.d("QRCode", "QRCode added to user");
                    }
                }
            } else {
                Log.d("QRCode", "QRCodes is null");
                db.update("user_accounts", this.userName, userMap);
            }
        });
    }

    /**
     * Delete QR code reference from qr_code array
     * @param id QR id
     */
    public void deleteQRCode(String id) {
        // see: stackoverflow.com/a/51983589/12955797

        Database db = new Database();

        DocumentReference qrRef = db.getDocReference("qr_codes/" + id);

        final Map<String, Object> qrCodesFieldArray = new HashMap<>();
        qrCodesFieldArray.put("qr_codes", FieldValue.arrayRemove(qrRef));

        db.update("user_accounts", this.userName, qrCodesFieldArray);

    }

    /**
     * Get all QR codes for the user
     * @param onSuccess callback
     */
    public void fetchQRCodes(onQrDataListener onSuccess) {

        Database db = new Database();
        Auth.getUsername((player) -> {

            db.getById("user_accounts", player).addOnCompleteListener(userTask -> {
                if (userTask.isSuccessful()) {
                    // loop through qr_codes field array and add to QrList ArrayList
                    DocumentSnapshot user = userTask.getResult();
                    ArrayList<QRCode> QrList = new ArrayList<>();
                    ArrayList<DocumentReference> qrRefs = (ArrayList<DocumentReference>) user.get("qr_codes");
                    if (qrRefs != null && qrRefs.size() > 0) {
                        for (int i = 0; i < qrRefs.size(); i++) {
                            int finalI = i;
                            qrRefs.get(i).get().addOnCompleteListener(qrTask -> {
                                if (qrTask.isSuccessful()) {
                                    DocumentSnapshot qr = qrTask.getResult();
                                    if(qr.getLong("score") != null) {
                                        QRCode qrCode;

                                        if (qr.get("location_image") != null) {
                                            qrCode = new QRCode(qr.getString("hash"), Objects.requireNonNull(qr.getLong("score")).intValue(), qr.getId(), qr.getString("location_image"));
                                        } else {
                                            qrCode = new QRCode(qr.getString("hash"), Objects.requireNonNull(qr.getLong("score")).intValue(), qr.getId());
                                        }

                                        if(qr.getDouble("lat") != null && qr.getDouble("lng") != null) {
                                            double latitude = (double) qr.getDouble("lat");
                                            double longitude = (double) qr.getDouble("lng");

                                            if (latitude != 0 && longitude != 0) {
                                                qrCode.setLocation(latitude, longitude);
                                            }
                                        }

                                        QrList.add(qrCode);

                                        if (finalI == qrRefs.size() - 1) {
                                            this.qrCodes = QrList;
                                            onSuccess.getQrData(QrList);
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        // TODO: error handling
                        onSuccess.getQrData(new ArrayList<>());
                    }
                }
            });
        });
    }

    /**
     * Get QR codes
     * @return QR codes
     */
    public ArrayList<QRCode> getQRCodes() {
        return this.qrCodes;
    }

    /**
     * Get username
     * @return username
     */
    public String getUsername() {
        return userName;
    }

    /**
     * Get UID
     * @return UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * Check if user is an owner
     * @return true if owner, false otherwise
     */
    public boolean getIsOwner() {
        return this.isOwner;
    }

    /**
     * Get email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set uid
     * @param uid uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Set username
     * @param userName username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set email
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set rank
     * @param rank rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Set qr codes
     * @param qrCodes qr codes
     */
    public void setQrCodes(ArrayList<QRCode> qrCodes) {
        this.qrCodes = (ArrayList<QRCode>) qrCodes.clone();
    }

    /**
     * Get rank of player
     * @return rank
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Get total score of QR codes
     * @return total score
     */
    public int getTotalScore() {
        if (this.qrCodes.size() != 0) {
            int totalScore = 0;
            for (int i = 0; i < this.qrCodes.size(); i++) {
                QRCode code = this.qrCodes.get(i);
                totalScore = totalScore + code.getScore();
            }
            return totalScore;
        } else return 0;
    }

    /**
     * Get highest score QR code from database
     * @return highest score
     */
    public int getHighestScore() {
        if (this.qrCodes.size() != 0) {
            int maxScore = 0;
            for (int i = 0; i < this.qrCodes.size(); i++) {
                QRCode code = this.qrCodes.get(i);
                int score = code.getScore();
                if (score > maxScore) {
                    maxScore = score;
                }
            }
            return maxScore;
        } else return 0;
    }

    /**
     * Get lowest score QR code from database
     * @return lowest score
     */
    public int getLowestScore() {

        if (this.qrCodes.size() != 0) {
            int minScore = this.qrCodes.get(0).getScore();
            for (int i = 1; i < this.qrCodes.size(); i++) {
                QRCode code = this.qrCodes.get(i);
                int score = code.getScore();
                if (score < minScore) {
                    minScore = this.qrCodes.get(i).getScore();
                }
            }
            return minScore;
        } else return 0;
    }

    /**
     * Get amount of QR codes scanned by player
     * @return qr code count
     */
    public Integer getQRCodeCount() {
        if (this.qrCodes.size() != 0) return this.qrCodes.size();
        else return 0;
    }

    /**
     * Update user data in database
     * @param userData user data
     */
    public void updateUser(Map<String, Object> userData) {
        Database db = new Database();
        db.update("user_accounts", this.userName, userData);
    }

    /**
     * Delete player
     * @param id player id
     */
    public void deletePlayer(Player id) {

    }

    /**
     * Check if owner, deprecated
     * @return true if owner, false otherwise
     */
    public boolean isOwner() {
        Log.d("VERBS","I'm in trouble");
        return false;
    }
}

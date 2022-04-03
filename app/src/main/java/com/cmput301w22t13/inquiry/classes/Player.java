package com.cmput301w22t13.inquiry.classes;

import android.util.Log;
import android.widget.Toast;

import com.cmput301w22t13.inquiry.activities.MainActivity;
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
    private ArrayList<QRCode> qrCodes = new ArrayList<QRCode>();
    private int rank = -1;

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
                        db.update("users", this.uid, userMap);
                        Log.d("QRCode", "QRCode added to user");
                    }
                }
            } else {
                Log.d("QRCode", "QRCodes is null");
                db.update("users", this.uid, userMap);
            }
        });
    }

    public void deleteQRCode(String id) {
        // delete the qr code's reference from the user's qr_codes array
        // see: stackoverflow.com/a/51983589/12955797

        Database db = new Database();

        DocumentReference qrRef = db.getDocReference("qr_codes/" + id);

        final Map<String, Object> qrCodesFieldArray = new HashMap<>();
        qrCodesFieldArray.put("qr_codes", FieldValue.arrayRemove(qrRef));

        db.update("users", this.uid, qrCodesFieldArray);
    }


    public void fetchQRCodes(onQrDataListener onSuccess) {
        ArrayList<QRCode> QrList = new ArrayList<>();

        Database db = new Database();

        db.getById("users", this.uid).addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                // loop through qr_codes field array and add to QrList ArrayList
                DocumentSnapshot user = userTask.getResult();
                ArrayList<DocumentReference> qrRefs = (ArrayList<DocumentReference>) user.get("qr_codes");
                if (qrRefs != null && qrRefs.size() > 0) {
                    for (int i = 0; i < qrRefs.size(); i++) {
                        int finalI = i;
                        qrRefs.get(i).get().addOnCompleteListener(qrTask -> {
                            if (qrTask.isSuccessful()) {
                                DocumentSnapshot qr = qrTask.getResult();
                                if(qr.getLong("score") != null) {
                                    int score = qr.getLong("score").intValue();
//                                    Log.d("QRCode", "QRCode score" + finalI + ": " + qr.getLong("score").intValue());
                                    QRCode qrCode = new QRCode(qr.getString("hash"), score, qr.getId());
                                    QrList.add(qrCode);

                                    if (finalI == qrRefs.size() - 1) {
                                        onSuccess.getQrData(QrList);
                                        this.qrCodes = QrList;
                                    }
                                }
                            }
                        });
                    }
                } else {
                    // TODO: error handling
                    onSuccess.getQrData(new ArrayList<QRCode>());
                }
            }
        });
    }

    public ArrayList<QRCode> getQRCodes() {
        return this.qrCodes;
    }

    public String getUsername() {
        return userName;
    }

    public String getUid() {
        return uid;
    }

    public boolean getIsOwner() {
        return this.isOwner;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setQrCodes(ArrayList<QRCode> qrCodes) {
        this.qrCodes = (ArrayList<QRCode>) qrCodes.clone();
    }

    public int getRank() {
        // returns Rank of player
        return this.rank;
    }

    public int getTotalScore() {
        // returns total score of QRCodes from database
        if (this.qrCodes.size() != 0) {
            int totalScore = 0;
            for (int i = 0; i < this.qrCodes.size(); i++) {
                QRCode code = this.qrCodes.get(i);
                totalScore = totalScore + code.getScore();
            }
            return totalScore;
        } else return 0;
    }

    public int getHighestScore() {
        // returns highest score QRCode from database
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

    public int getLowestScore() {
        // returns highest score QRCode from database

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

    public Integer getQRCodeCount() {
        // returns amount of QRCodes scanned by player from database
        if (this.qrCodes.size() != 0) return this.qrCodes.size();
        else return 0;
    }

    // updates user data in database
    public void updateUser(Map<String, Object> userData) {
        Database db = new Database();
        db.update("users", this.uid, userData);
    }

    public void deletePlayer(Player id) {

    }
}

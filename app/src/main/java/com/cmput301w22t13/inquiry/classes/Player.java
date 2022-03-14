package com.cmput301w22t13.inquiry.classes;

import android.util.Log;

import com.cmput301w22t13.inquiry.db.Database;
import com.cmput301w22t13.inquiry.db.onQrDataListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Player {

    private String uid;
    private String userName;
    private String email;
    private ArrayList<QRCode> qrCodes;

    Database db = new Database();

    public Player(String userName, String uid) {
        this.userName = userName;
        this.uid = uid;
        this.email = "";
    }

    public Player(String userName, String uid, String email) {
        this.userName = userName;
        this.uid = uid;
        if (email != null) {
            this.email = email;
        }
    }

    public Player(String userName, String uid, Boolean getQrCodes) {
        this.userName = userName;
        this.uid = uid;
        if (getQrCodes) {
            fetchQRCodes(qrCodes1 -> {
                this.qrCodes = qrCodes1;
            });
        }
    }

    public void fetchQRCodes(onQrDataListener onSuccess) {
        ArrayList<QRCode> QrList = new ArrayList<>();


        db.getById("users", this.uid).addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                // loop through qr_codes field array and add to QrList ArrayList
                DocumentSnapshot user = userTask.getResult();
                ArrayList<DocumentReference> qrRefs = (ArrayList<DocumentReference>) user.get("qr_codes");
                if (qrRefs != null) {
                    for (int i = 0; i < qrRefs.size(); i++) {
                        int finalI = i;
                        qrRefs.get(i).get().addOnCompleteListener(qrTask -> {
                            if (qrTask.isSuccessful()) {
                                DocumentSnapshot qr = qrTask.getResult();
                                QRCode qrCode = new QRCode(qr.getString("hash"), qr.getLong("score").intValue());
                                Log.d("QRCode", qrCode.getHash());
                                QrList.add(qrCode);

                                if (finalI == qrRefs.size() - 1) {
                                    onSuccess.getQrData(QrList);
                                    this.qrCodes = QrList;
                                }
                            }
                        });
                    }
                }
                else {
                    // TODO: error handling
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

    public int getRank() {
        // returns Rank of player
        return -1;
    }

    public int getTotalScore() {
        // returns total score of QRCodes from database

        if (this.qrCodes != null) {
            int totalScore = 0;
            for (int i = 0; i < this.qrCodes.size(); i++) {
                QRCode code = this.qrCodes.get(i);
                totalScore = totalScore + code.getScore();
            }
            return totalScore;
        }else return -1;
    }

    public int getHighestScore() {
        // returns highest score QRCode from database
        if (this.qrCodes != null) {
            int maxScore = 0;
            for (int i = 0; i < this.qrCodes.size(); i++) {
                QRCode code = this.qrCodes.get(i);
                int score = code.getScore();
                if (score > maxScore) {
                    maxScore = score;
                }
            }
            return maxScore;
        }else return -1;
    }

    public int getLowestScore() {
        // returns highest score QRCode from database

        if (this.qrCodes != null) {
            int minScore = this.qrCodes.get(0).getScore();
            for (int i = 1; i < this.qrCodes.size(); i++) {
                QRCode code = this.qrCodes.get(i);
                int score = code.getScore();
                if (score < minScore) {
                    minScore = score;
                }
            }
            return minScore;
        }else return -1;
    }

    public int getQRCodeCount() {
        // returns amount of QRCodes scanned by player from database
        if (this.qrCodes != null) return this.qrCodes.size();
        else return -1;
    }

    // updates user data in database
    public void updateUser(Map<String, Object> userData) {
        db.update("users", this.uid, userData);
    }
}

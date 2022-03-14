package com.cmput301w22t13.inquiry.db;

/**
 *  Callback to fetch user's qr data from firestore database
 */


import com.cmput301w22t13.inquiry.classes.QRCode;
import java.util.ArrayList;

public interface onQrDataListener {
    void getQrData(ArrayList<QRCode> qrCodes);
}

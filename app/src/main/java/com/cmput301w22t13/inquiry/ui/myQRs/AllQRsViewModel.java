package com.cmput301w22t13.inquiry.ui.myQRs;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Owner;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;

/**
 * View model to see all possible QRCodes with a modification to have a Owner
 */
public class AllQRsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final Owner player = (Owner) Auth.getPlayer();
    public AllQRsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my QRs fragment");
    }

    public Owner getOwner(){
        return this.player;

    }

    /**
     * gets the mutable live data text
     * @return returns a LiveData with Strings
     */
    public LiveData<String> getText() {
       return this.mText;
    }

    /**
     * deletes a given QR Code
     * @param qrCode QRCode object to be deleted
     */
    public void deleteQRCode(QRCode qrCode){
        player.deleteQRCode(qrCode);
    }
}

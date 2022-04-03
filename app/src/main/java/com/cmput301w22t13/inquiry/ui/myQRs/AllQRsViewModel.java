package com.cmput301w22t13.inquiry.ui.myQRs;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Owner;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.classes.QRCode;

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
    public LiveData<String> getText() {
       return this.mText;
    }
    public void deleteQRCode(QRCode qrCode){
        player.deleteQRCode(qrCode);
    }
}

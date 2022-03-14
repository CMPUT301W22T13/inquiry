package com.cmput301w22t13.inquiry.ui.myQRs;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;

public class MyQRsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Player player = Auth.getPlayer();

    public MyQRsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my QRs fragment");
    }

    public void getData(onQrDataListener onSuccess) {
        onSuccess.getQrData(player.getQRCodes());
    }

    public LiveData<String> getText() {
        return mText;
    }
}
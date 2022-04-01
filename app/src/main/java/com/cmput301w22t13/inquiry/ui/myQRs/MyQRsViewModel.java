package com.cmput301w22t13.inquiry.ui.myQRs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;

public class MyQRsViewModel extends ViewModel {


    private final MutableLiveData<String> mText;
    private final Player player = Auth.getPlayer();

    public MyQRsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my QRs fragment");
    }

    public Player getPlayer() {
        return player;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void deleteQRFromPlayer(String id) {
        player.deleteQRCode(id);
    }
}
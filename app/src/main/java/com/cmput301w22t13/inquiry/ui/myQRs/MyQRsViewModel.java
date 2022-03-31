package com.cmput301w22t13.inquiry.ui.myQRs;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void deleteQRFromPlayer(DocumentReference QRtoDelete) {
    }
}
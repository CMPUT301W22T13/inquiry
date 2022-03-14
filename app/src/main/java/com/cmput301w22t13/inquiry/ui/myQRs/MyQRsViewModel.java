package com.cmput301w22t13.inquiry.ui.myQRs;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyQRsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyQRsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my QRs fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
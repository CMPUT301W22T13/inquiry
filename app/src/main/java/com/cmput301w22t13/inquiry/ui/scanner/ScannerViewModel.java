package com.cmput301w22t13.inquiry.ui.scanner;
/**
 * Class that is responsible for preparing and managing the data for ScannerFragment.
 *  It also handles the communication of the  Fragment with the rest of the application
 */

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScannerViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ScannerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Scanner fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
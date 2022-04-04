package com.cmput301w22t13.inquiry.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * View model for the leaderboard tab
 */
public class LeaderboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeaderboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");

    }

    /**
     * Get text
     * @return text
     */
    public LiveData<String> getText() {
        return mText;
    }
}
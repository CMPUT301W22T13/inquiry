package com.cmput301w22t13.inquiry.ui.profile;
/**
 *  Class that is responsible for preparing and managing the data for ProfileFragment.
 *  It also handles the communication of the  Fragment with the rest of the application
 *  This class fetches the user data and uses the onProfileDataListener
 *  callback to send it to the ProfileFragment
 */


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;


public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    FirebaseUser currentUser = Auth.getCurrentUser();
    Database db = new Database();

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
            mText.setValue("This is profile fragment");
    }

    /**
     * get the user's data from firestore (using a callback)
     *
     * @param  onSuccess callback function that gets called when the username is available
     */
    public void getData(onProfileDataListener onSuccess) {
        if(currentUser!= null) {
            String id = currentUser.getUid();

            // get the user's data from firestore
            db.getById("users", id).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String username = (String) document.get("username");
                        String email = (String) document.get("email");
                        String uid = (String) document.get("id");

                        // create a map of the user's data to pass to the success callback
                        Map<String, Object> userData = new HashMap<>();

                        userData.put("username", username);
                        userData.put("email", email);
                        userData.put("uid", uid);

                        if(username != null && uid != null) {
                            onSuccess.getProfileData(userData);
                        }
                    }
                }
            });
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}
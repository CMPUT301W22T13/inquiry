package com.cmput301w22t13.inquiry.ui.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    FirebaseUser currentUser = Auth.getCurrentUser();
    Database db = new Database();

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        if(currentUser!= null) {
            String id = currentUser.getUid();
            db.getById("users", id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Log.i("GETPROFILE", "successful");
                            Log.d("GETPROFILE", "DocumentSnapshot data: " + document.getData());

                            String username = (String) document.get("username");
                            mText.setValue("Hi, " + username + "!");
                        }

                    }
                }
            });
        }
        else {
            mText.setValue("This is profile fragment");
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}
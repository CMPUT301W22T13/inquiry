package com.cmput301w22t13.inquiry.auth;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    private static FirebaseAuth firebaseAuth;

    public static void init() {
        // initialize Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // check if user is signed in
        if(currentUser == null) {
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Log.i("AUTH", "Successfully Authenticated");
                    }
                }
            });
        }
        else {
            Log.i("AUTH", "Current user: " + currentUser.getUid());
        }
    }

    public static FirebaseUser getCurrentUser() {
        if(firebaseAuth != null) {
            return Auth.firebaseAuth.getCurrentUser();
        }
        return null;
    }
}

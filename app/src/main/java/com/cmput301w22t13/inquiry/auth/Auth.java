package com.cmput301w22t13.inquiry.auth;
/**
 * Handles user sign-in and log-in
 * Gets called in MainActivity to authenticate user
 * If user does not exist in database, this class is responsible for
 * authenticating the user and saving the user in the firestore database
 */

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w22t13.inquiry.db.Database;
import com.github.javafaker.Faker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Auth {
    private static FirebaseAuth firebaseAuth;

    public static void init() {
        // initialize Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        Database db = new Database();
        Faker faker = new Faker();

        // check if user is signed in
        if (currentUser == null) {
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String username = faker.superhero().prefix() + StringUtils.capitalize(faker.animal().name()) + faker.number().digits(2);

                        Map<String, Object> newUser = new HashMap<>();
                        String uid = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        newUser.put("id", uid);
                        newUser.put("username", username);
                        db.set("users", uid, newUser);
                    }
                }
            });
        } else {
            Log.i("AUTH", "Current user: " + currentUser.getUid());
        }
    }

    public static FirebaseUser getCurrentUser() {
        if (firebaseAuth != null) {
            return Auth.firebaseAuth.getCurrentUser();
        }
        return null;
    }
}

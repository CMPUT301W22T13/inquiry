package com.cmput301w22t13.inquiry.auth;
/**
 * Handles user sign-in and log-in
 * Gets called in MainActivity to authenticate user
 * If user does not exist in database, this class is responsible for
 * authenticating the user and saving the user in the firestore database
 */

import androidx.annotation.NonNull;

import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.db.Database;
import com.github.javafaker.Faker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Auth {
    private static FirebaseAuth firebaseAuth;
    private static Player player;

    public static void init() {
        // initialize Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        Database db = new Database();
        Faker faker = new Faker();

        // check if user is signed in
        if (currentUser == null) {
            // if not, create a new user and save its info
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful() && getCurrentUser() != null) {
                        String username = faker.superhero().prefix() + StringUtils.capitalize(faker.animal().name()) + faker.number().digits(2);

                        // create a new user document
                        Map<String, Object> newUser = new HashMap<>();
                        String uid = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        newUser.put("id", uid);
                        newUser.put("username", username);
                        db.set("users", uid, newUser);

                        // set user display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username).build();
                        getCurrentUser().updateProfile(profileUpdates);

                        // initialize player with new user details
                        player = new Player(uid, username);
                    }
                }
            });
        } else {
            // initialize player with current user details
            player = new Player(currentUser.getDisplayName(), currentUser.getUid(), currentUser.getEmail());
        }
    }


    // gets the current user as a Player object
    public static Player getPlayer() {
        return player;
    }

    public static FirebaseUser getCurrentUser() {
        if (firebaseAuth != null) {
            return Auth.firebaseAuth.getCurrentUser();
        }
        return null;
    }
}

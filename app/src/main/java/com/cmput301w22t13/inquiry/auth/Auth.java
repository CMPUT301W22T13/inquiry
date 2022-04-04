package com.cmput301w22t13.inquiry.auth;
/**
 * Handles user sign-in and log-in
 * Gets called in MainActivity to authenticate user
 * If user does not exist in database, this class is responsible for
 * authenticating the user and saving the user in the firestore database
 */

import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w22t13.inquiry.classes.Owner;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.db.Database;
import com.github.javafaker.Faker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class Auth {
    private static FirebaseAuth firebaseAuth;
    private static Player player;
    private static Database db;

    public interface UsernameCallback {
        void onComplete(String username);
    }

    public interface PlayerCallback {
        void onComplete(Player player);
    }

    public interface LoginCallback {
        void onComplete(Boolean isSuccessful);
    }

    private static void createNewAccount(String username) {
        // create a new user account
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("qr_codes", new ArrayList<String>());
        db.set("user_accounts", username, newUser);
    }

    private static void linkAnontoAccount(String uid, String username) {

        Map<String, Object> newLink = new HashMap<>();
        newLink.put("username", username);
        db.set("user_links", uid, newLink);

        // set user display name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
        Objects.requireNonNull(getCurrentUser()).updateProfile(profileUpdates);


    }

    private static Player newUserFlow(String uid) {

        Faker faker = new Faker();
        String username = faker.superhero().prefix() + StringUtils.capitalize(faker.animal().name()) + faker.number().digits(2);

        // create a new user linked document
        createNewAccount(username);
        linkAnontoAccount(uid, username);

        // initialize player with new user details
        player = new Player(username, uid, true);

        return player;
    }

    public static void login(String uid, LoginCallback l) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        db.getById("user_links", uid).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        String username = (String) task.getResult().get("username");

                        assert currentUser != null;
                        assert username != null;

                        linkAnontoAccount(currentUser.getUid(), username);
                        Auth.player = new Player(username, currentUser.getUid(), true);
                        l.onComplete(true);
                    } else {
                        l.onComplete(false);
                    }
                }
        );


    }

    public static void init(PlayerCallback p) {
        // initialize Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        db = new Database();

        // check if user is signed in
        if (currentUser == null) {
            // if not, create a new user and save its info
            firebaseAuth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful() && getCurrentUser() != null) {
                    player = newUserFlow(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());
                    p.onComplete(player);
                }
            });
        } else {
            // initialize player with current user details

            getUsername(username -> {
                db.getById("user_accounts", username).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document2 = task.getResult();
                        if (document2 != null && document2.exists()) {
                            if (document2.contains("isOwner") && String.valueOf(document2.get("isOwner")).equals("true")) {
                                player = new Owner(username, currentUser.getUid(), true);
                                Log.d("VERBS","OWNR");
                            } else {
                                player = new Player(username, currentUser.getUid(), true);
                                Log.d("VERBS","USR");
                            }
                            p.onComplete(player);
                        }
                    } else {
                        Log.i("LeaderboardFragment", "No users found");
                    }
                });
            });

        }
    }


    // gets the current user as a Player object
    public static Player getPlayer() {
        return player;
    }

    public static void withPlayer(PlayerCallback p) {
        if (player != null) p.onComplete(player);
        init(p);
    }

    public static void getUsername(UsernameCallback u) {

        if (player != null) u.onComplete(player.getUsername());

        String uid = Objects.requireNonNull(getCurrentUser()).getUid();

        Database db = new Database();
        Task<DocumentSnapshot> task = db.getById("user_links", uid);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {

                DocumentSnapshot doc = task1.getResult();

                if (doc.exists()) {
                    String username = (String) doc.get("username");
                    u.onComplete(username);
                } else {
                    u.onComplete(newUserFlow(uid).getUsername());
                }
            } else {
                u.onComplete("NULL");
            }
        });
    }

    public static FirebaseUser getCurrentUser() {
        if (firebaseAuth != null) {
            return Auth.firebaseAuth.getCurrentUser();
        }
        return null;
    }
}

package com.cmput301w22t13.inquiry.ui.profile;

/** Handles navigation for fragment_edit_profile.xml
 * Calls method db.update() when user wants to save changes
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.Player;
import com.cmput301w22t13.inquiry.db.Database;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class EditProfileFragment extends Fragment {

    private final Player user;

    Database db = new Database();

    public EditProfileFragment(Player user) {
        this.user = user;
    }

    /**
     * Close fragment
     */
    private void closeFragment() {
        this.getParentFragmentManager().popBackStack();
    }

    /** Action bar menu set up */
    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_edit_profile_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel_edit_profile) {
            closeFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /** end action bar menu set up */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Edit Profile");

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // fill in username and email fields from user data
        TextView usernameInput = root.findViewById(R.id.input_edit_username);
        usernameInput.setText(user.getUsername());

        TextView emailInput = root.findViewById(R.id.input_edit_email);
        String userEmail = user.getEmail();
        if(userEmail != null) {
            emailInput.setText(userEmail);
        }

        Button saveChangesButton = root.findViewById(R.id.save_edit_profile_button);

        FirebaseUser currentUser = Auth.getCurrentUser();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameToSave = usernameInput.getText().toString();
                String emailToSave = emailInput.getText().toString();

                Map<String, Object> userData = new HashMap<>();

//                Log.i("EditProfileFragment", "Saving changes to profile");
//                Log.i("EditProfileFragment", "Username: " + usernameToSave);
//                Log.i("EditProfileFragment", "Email: " + emailToSave);
//                Log.i("EditProfileFragment", "UID: " + uid);

                String userId = user.getUid();

                if(!usernameToSave.isEmpty() && userId != null && currentUser != null) {
                    // update user document in database
                    userData.put("username", usernameToSave);
                    userData.put("email", emailToSave);

                    // update FirebaseUser info
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usernameToSave).build();
                    currentUser.updateProfile(profileUpdates);
                    if (!emailToSave.equals("")) currentUser.updateEmail(emailToSave);

                    profileViewModel.updateData(user, userData);

                    // hide the keyboard and go back to the profile
                    final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
                    closeFragment();
                }
                else {
                    usernameInput.setError("Please enter a username");
                }
            }
        });
        
        return root;
    }
}
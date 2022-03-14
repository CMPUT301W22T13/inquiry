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

import android.util.Log;
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
import com.cmput301w22t13.inquiry.db.Database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 *
 */
public class EditProfileFragment extends Fragment {

    private final String username;
    private final String email;
    private final String uid;

    Database db = new Database();

    public EditProfileFragment(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

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
        
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // fill in username and email fields from user data
        TextView usernameInput = root.findViewById(R.id.input_edit_username);
        usernameInput.setText(username);

        TextView emailInput = root.findViewById(R.id.input_edit_email);
        if(email != null) {
            emailInput.setText(email);
        }

        Button saveChangesButton = root.findViewById(R.id.save_edit_profile_button);

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

                if(!usernameToSave.isEmpty() && uid != null) {
                    // update user document in database
                    userData.put("username", usernameToSave);
                    userData.put("email", emailToSave);
                    db.update("users", uid, userData);

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
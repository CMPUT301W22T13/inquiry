package com.cmput301w22t13.inquiry.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public EditProfileFragment(String username) {
        this.username = username;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

//        if (getArguments() != null) {
//
//
//        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_edit_profile_menu, menu);

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Edit Profile");
        
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        TextView usernameInput = root.findViewById(R.id.input_edit_username);
        usernameInput.setText(username);
        
        return root;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel_edit_profile) {
            this.getParentFragmentManager().popBackStack();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
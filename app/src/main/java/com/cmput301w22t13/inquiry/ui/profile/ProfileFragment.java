package com.cmput301w22t13.inquiry.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.databinding.FragmentProfileBinding;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Profile");

        final TextView usernameText = root.findViewById(R.id.username);
        profileViewModel.getUsername(new onProfileDataListener() {
            @Override
            public void getUsername(String usernameString) {
                setUsername(usernameString);
                usernameText.setText(String.format(getResources().getString(R.string.profile_greeting), usernameString));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_profile_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }


    @SuppressLint({"NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_profile) {
            Fragment newFragment = new EditProfileFragment(username);
            FragmentTransaction ft = this.getParentFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("username", "PROFILE");

            newFragment.setArguments(bundle);
            ft.replace(R.id.nav_host_fragment_activity_main, newFragment, "PROFILE");
            ft.addToBackStack("PROFILE");
            ft.setReorderingAllowed(true);
            ft.commit();

//            transaction.replace(R.id.nav_host_fragment_activity_main, newFragment);
//
//            transaction.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
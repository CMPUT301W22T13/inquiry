package com.cmput301w22t13.inquiry.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.databinding.FragmentProfileBinding;

import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private String username;
    private String email;
    private String uid;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUid(String uid) { this.uid = uid; }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHasOptionsMenu(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Profile");

        final TextView usernameText = root.findViewById(R.id.username_text);
        final TextView emailText = root.findViewById(R.id.user_email_text);
        final ProgressBar spinner = root.findViewById(R.id.profile_progress_spinner);

        profileViewModel.getData(new onProfileDataListener() {
            // get data from success listener and display it
            // TODO: Handle error
            @Override
            public void getProfileData(Map<String, Object> data) {
                String usernameString = (String) data.get("username");
                String emailString = (String) data.get("email");
                String uidString = (String) data.get("uid");

                if(usernameString != null) {
                    setUsername(usernameString);
                }
                if(emailString != null) {
                    setEmail(emailString);
                    emailText.setText(emailString);
                }
                setUid(uidString);

                usernameText.setText(String.format(getResources().getString(R.string.profile_greeting), usernameString));

                spinner.setVisibility(View.GONE);
                usernameText.setVisibility(View.VISIBLE);
                emailText.setVisibility(View.VISIBLE);
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
            // navigate to edit profile fragment and pass user data to it
            Fragment newFragment = new EditProfileFragment(uid, username, email);
            FragmentTransaction ft = this.getParentFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("email", email);
            bundle.putString("uid", uid);

            newFragment.setArguments(bundle);
            ft.replace(R.id.nav_host_fragment_activity_main, newFragment, "PROFILE");

            // configure navigation options
            ft.addToBackStack("PROFILE");
            ft.setReorderingAllowed(true);
            ft.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
package com.cmput301w22t13.inquiry.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private @NonNull FragmentProfileBinding binding;

    public ProfileFragment() {
        binding = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.text;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        final TextView usernameText = root.findViewById(R.id.username);
        profileViewModel.getUsername(new onProfileDataListener() {
            @Override
            public void getUsername(String username) {
                usernameText.setText(username);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = binding.getRoot();
        Button button = root.findViewById(R.id.edit_profile_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
    }
}
package com.cmput301w22t13.inquiry.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.databinding.ActivityOnboardingBinding;
import com.cmput301w22t13.inquiry.ui.profile.ProfileViewModel;

/**
 * An activity that displays the first time the application is run.
 */
public class OnboardingActivity extends AppCompatActivity {

    private View mContentView;
    public static final String PROF_NAV = "profNav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Auth.init((player)->{});

        ActivityOnboardingBinding binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setTheme(R.style.Theme_InQuiRy_NoActionBar);
        setContentView(binding.getRoot());

        // get view ids
        TextView message = findViewById(R.id.onboarding_message);
        Button scanning = findViewById(R.id.onboarding_start_button);
        Button profileButton = findViewById(R.id.onboarding_profile_button);
        Button loginButton = findViewById(R.id.onboarding_login_button);

        // get the username and set the message to include the username
        // use the string "user" if the user cannot be found.
        message.setText(String.format(getString(R.string.onboarding_message), "user"));
        mContentView = binding.fullscreenContent;
        ProfileViewModel profile = new ProfileViewModel();
        profile.getData(data -> {
            // if the user exists on the database (will be created by the view model)
            Object user = data.get("username");
            if (user != null) message.setText(String.format(getString(R.string.onboarding_message), user));
        });

        // set what happens when the start scanning button is clicked
        scanning.setOnClickListener(view -> {
            finish();
        });

        // set what happens when the profile button is clicked
        profileButton.setOnClickListener(view -> {

            // set shared preferences across activities to tell the first activity to open profile view
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            prefs
                    .edit()
                    .putBoolean(PROF_NAV, Boolean.TRUE)
                    .apply();
            finish();
        });

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, QRLoginActivity.class);
            startActivity(intent);
            finish();
        });

        // hide status bar and ui
        hide();

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().hide(
                    WindowInsets.Type.statusBars());

        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
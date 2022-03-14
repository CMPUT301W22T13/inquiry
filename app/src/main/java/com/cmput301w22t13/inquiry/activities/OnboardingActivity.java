package com.cmput301w22t13.inquiry.activities;

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
import com.cmput301w22t13.inquiry.databinding.ActivityOnboardingBinding;
import com.cmput301w22t13.inquiry.ui.profile.ProfileViewModel;
import com.cmput301w22t13.inquiry.ui.profile.onProfileDataListener;

import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class OnboardingActivity extends AppCompatActivity {

    private View mContentView;
    public static final String PROF_NAV = "profNav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityOnboardingBinding binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView message = findViewById(R.id.onboarding_message);
        Button scanning = findViewById(R.id.onboarding_start_button);
        Button profileButton = findViewById(R.id.onboarding_profile_button);

        message.setText(String.format(getString(R.string.onboarding_message), "user"));
        mContentView = binding.fullscreenContent;
        ProfileViewModel profile = new ProfileViewModel();
        profile.getData(data -> {
            Object user = data.get("username");
            if (user != null) message.setText(String.format(getString(R.string.onboarding_message), user));
        });

        scanning.setOnClickListener(view -> {
            finish();
        });

        profileButton.setOnClickListener(view -> {
            // set shared preferences across activities

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            prefs
                    .edit()
                    .putBoolean(PROF_NAV, Boolean.TRUE)
                    .apply();
            finish();
        });

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
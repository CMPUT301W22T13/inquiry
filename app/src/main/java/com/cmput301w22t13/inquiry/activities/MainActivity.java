package com.cmput301w22t13.inquiry.activities;

import static com.cmput301w22t13.inquiry.activities.OnboardingActivity.PROF_NAV;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cmput301w22t13.inquiry.databinding.ActivityMainBinding;

/** First class launched when app starts running,
 * sets up bottom navigation, authorization for user is initialized,
 * binding class is generated for each XML layout file present in the module
 */
public class MainActivity extends AppCompatActivity {

    public static final String PREV_STARTED = "prevStarted";
    SharedPreferences prefs;
    BottomNavigationView navView;

    /**
     * Start activity to display onboarding screen.
     */
    private void showHelp() {
        Intent intent = new Intent(this, OnboardingActivity.class);
        startActivity(intent);
    }


    /**
     * Check if onboarding has been run already. If not, run it.
     */
    private void checkForHelp() {
        // start on-boarding screen if never done before
        // uses SharedPreferences to store this value

        // Taken from StackOverflow.com
        // Answer: https://stackoverflow.com/a/7238549
        // Author: https://stackoverflow.com/users/691688/femi


        // NOTE: FOR TESTING ONLY, THIS CLEARS ALL PREFERENCES BEFORE RUNNING
//        prefs.edit().clear().apply();
        boolean previouslyStarted = prefs.getBoolean(PREV_STARTED, false);
        if (!previouslyStarted) {
            prefs
                    .edit()
                    .putBoolean(PREV_STARTED, Boolean.TRUE)
                    .apply();
            showHelp();
        }
    }


    /**
     * On resume, check if we need to move to another tab.
     */

    @Override
    protected void onResume() {
        super.onResume();

        // check if we need to navigate to the profile tab
        boolean profile = prefs.getBoolean(PROF_NAV, false);
        if (profile) {
            prefs
                    .edit()
                    .putBoolean(PROF_NAV, true)
                    .apply();

            // change tab
            if (navView != null) navView.setSelectedItemId(R.id.navigation_myqrs);
        }
    }

    /**
     * Setup main activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate layout
        com.cmput301w22t13.inquiry.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());

        // get settings
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_maps,
                R.id.navigation_myqrs,
                R.id.navigation_scanner,
                R.id.navigation_leaderboard,
                R.id.navigation_profile)
                .build();


        // setup navigation UI
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;
        final NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Auth.init((player) -> {});

        checkForHelp();

    }
}

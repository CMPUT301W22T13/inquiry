package com.cmput301w22t13.inquiry.classes;

import android.Manifest;
import android.os.Build;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;

import java.util.Map;

/**
 * Class to manage locatin permission
 */
public class LocationPermission {

    ActivityResultLauncher<Object> locationPermissionRequest;
    Boolean locationPermissionGranted;

    public interface LocationPermissionCallback {
        ActivityResultLauncher<Object> onCreate(ActivityResultContract a, ActivityResultCallback<Map<String, Boolean>> c);
    }

    public interface OnGrantedCallback {
        void onGranted();
    }

    public interface OnFailedCallback {
        void onFailed();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LocationPermission(LocationPermissionCallback l, OnGrantedCallback c, OnFailedCallback f) {

        locationPermissionGranted = false;

        locationPermissionRequest = l.onCreate(new ActivityResultContracts
                .RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted = result.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
//            Boolean coarseLocationGranted = result.getOrDefault(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
            if (fineLocationGranted != null && fineLocationGranted) {
                locationPermissionGranted = true;
                c.onGranted();
//            } else if (coarseLocationGranted != null && coarseLocationGranted) {
//                locationPermissionGranted = false;
            } else {
                locationPermissionGranted = false;
                f.onFailed();
            }
        });

    }

    /**
     * Request permission
     */
    public void request() {
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    /**
     * Check if permission is complete
     * @return true if permission granted
     */
    public boolean getPermission() {
        return locationPermissionGranted;
    }


}

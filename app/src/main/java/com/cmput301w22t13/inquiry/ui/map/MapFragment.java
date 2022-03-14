package com.cmput301w22t13.inquiry.ui.map;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MapFragment extends Fragment {

    private FragmentMapsBinding binding;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        // Code taken from Google Maps example application:
        // https://github.com/googlemaps/android-samples/blob/3f6f0fff873263fa82da78bb45c11a4fd774b4ac/tutorials/java/CurrentPlaceDetailsOnMap/app/src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.java

        private static final int DEFAULT_ZOOM = 15;
        private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
        GoogleMap map;
        Location lastKnownLocation;
        private boolean locationPermissionGranted;

        private void getLocationPermission() {
            /*
             * Request location permission, so that we can get the location of the
             * device. The result of the permission request is handled by a callback,
             * onRequestPermissionsResult.
             */
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        private void getDeviceLocation() {
            /*
             * Get the best and most recent location of the device, which may be null in rare
             * cases when a location is not available.
             */
            try {
                if (locationPermissionGranted) {
                    Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                // Set the map's camera position to the current location of the device.
                                lastKnownLocation = task.getResult();
                                if (lastKnownLocation != null) {
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(lastKnownLocation.getLatitude(),
                                                    lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                }
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                map.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                map.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage(), e);
            }
        }

        private void updateLocationUI() {
            if (map == null) {
                return;
            }
            try {
                if (locationPermissionGranted) {
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(true);

                    // Website: StackOverflow
                    // Answer: https://stackoverflow.com/a/16492921
                    // Author: https://stackoverflow.com/users/2296798/nick

                    if (lastKnownLocation != null) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude())).zoom(DEFAULT_ZOOM).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory
                                .newCameraPosition(cameraPosition);
                        map.moveCamera(cameraUpdate);
                    }

                } else {
                    map.setMyLocationEnabled(false);
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    lastKnownLocation = null;
                    getLocationPermission();
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            this.map = googleMap;
            // Create a sample test point
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Construct a PlacesClient
        Places.initialize(getContext(), getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(getContext());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
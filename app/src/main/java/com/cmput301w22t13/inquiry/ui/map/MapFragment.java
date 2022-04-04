package com.cmput301w22t13.inquiry.ui.map;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.QRListActivity;
import com.cmput301w22t13.inquiry.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapFragment extends Fragment {

    private Location lastKnownLocation;
    private boolean locationPermissionGranted;
    private MapViewModel model;

    private GoogleMap map;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    /**
     * search for nearby points and populate map
     * @param firstRun if first run, target is around location, otherwise it is around the map center
     */
    private void updateNearbyPoints(boolean firstRun) {

        if (map == null) return;

        LatLng target;

        if (!firstRun) {
            target = map.getCameraPosition().target;
        } else {
            target = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        }

        map.clear();
        model.getNearbyPoints(target.latitude,
                target.longitude,
                qr -> map.addMarker(new MarkerOptions()
                        .position(qr.getLocation()).title(qr.getName() + " - " + qr.getScore() + "pts").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                ));

    }

    /**
     * Update UI elements to display proper buttons
     */
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

                    updateNearbyPoints(true);
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
     * Get permissions
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        // Code taken from Google Maps example application:
        // https://github.com/googlemaps/android-samples/blob/3f6f0fff873263fa82da78bb45c11a4fd774b4ac/tutorials/java/CurrentPlaceDetailsOnMap/app/src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.java


        private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

        /**
         * Get location of device
         */
        private void getDeviceLocation() {
            /*
             * Get the best and most recent location of the device, which may be null in rare
             * cases when a location is not available.
             */
            try {
                if (locationPermissionGranted) {
                    Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                map.setMyLocationEnabled(true);
                                map.getUiSettings().setMyLocationButtonEnabled(true);

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage(), e);
            }
        }

        /**
         * Style map
         */
        private void styleMap() {

            if (map == null) return;

            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(true);

            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = map.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                requireContext(), R.raw.style_json));

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);
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
        public void onMapReady(@NonNull GoogleMap googleMap) {
            map = googleMap;

            map.setOnInfoWindowClickListener(marker -> {
                // TODO launch QR code activity
            });

            styleMap();
            getDeviceLocation();
            updateLocationUI();
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        FragmentMapsBinding binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        model = new MapViewModel();

        FloatingActionButton refresh = root.findViewById(R.id.fragment_maps_refresh);

        refresh.setOnClickListener(view -> updateNearbyPoints(false));

        ExtendedFloatingActionButton listButton = root.findViewById(R.id.fragment_maps_list);
        listButton.setOnClickListener(view -> {
            if (map != null) {
                Intent intent = new Intent(requireContext(), QRListActivity.class);
                intent.putExtra("LAT", map.getCameraPosition().target.latitude);
                intent.putExtra("LNG", map.getCameraPosition().target.longitude);
                startActivity(intent);
            }
        });

        // Construct a PlacesClient
        Places.initialize(requireContext(), getString(R.string.google_maps_key));
        Places.createClient(requireContext());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Request location permission
        requestPermission();

        return root;

    }


    /**
     * Request permission
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void requestPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                locationPermissionGranted = true;
                                updateLocationUI();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                locationPermissionGranted = false;
                            } else {
                                locationPermissionGranted = false;
                            }
                        }
                );

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
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
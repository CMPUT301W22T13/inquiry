package com.cmput301w22t13.inquiry.ui.map;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cmput301w22t13.inquiry.R;
import com.cmput301w22t13.inquiry.activities.QRDetailsActivity;
import com.cmput301w22t13.inquiry.activities.QRListActivity;
import com.cmput301w22t13.inquiry.auth.Auth;
import com.cmput301w22t13.inquiry.classes.LocationPermission;
import com.cmput301w22t13.inquiry.classes.QRCode;
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

import java.util.Map;
import java.util.Objects;

public class MapFragment extends Fragment {

    private Location lastKnownLocation;
    private boolean locationPermissionGranted;
    private MapViewModel model;

    private GoogleMap map;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private LocationPermission lp;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);


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
                qr -> Objects.requireNonNull(map.addMarker(new MarkerOptions()
                        .position(qr.getLocation()).title(qr.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).snippet(qr.getScore() + " pts")
                )).setTag(qr), () -> {});

    }

    private void moveCameraToCurrentLocation() {

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


    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLocationUI() {
        try {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.
                    lastKnownLocation = task.getResult();
                    moveCameraToCurrentLocation();
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    Log.e(TAG, "Exception: %s", task.getException());
                    map.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private FusedLocationProviderClient fusedLocationProviderClient;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        // Code taken from Google Maps example application:
        // https://github.com/googlemaps/android-samples/blob/3f6f0fff873263fa82da78bb45c11a4fd774b4ac/tutorials/java/CurrentPlaceDetailsOnMap/app/src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.java

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
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            map = googleMap;

            map.setOnInfoWindowClickListener(marker -> {
                Intent intent = new Intent(requireContext(), QRDetailsActivity.class);
                intent.putExtra("code", (QRCode) marker.getTag());
                intent.putExtra("player", Auth.getPlayer().getUsername());
                requireContext().startActivity(intent);
            });

            styleMap();

            // Request location permission
            lp.request();
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


        return root;

    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lp = new LocationPermission(this::registerForActivityResult,
            () -> {
                getDeviceLocation();
                updateLocationUI();
            }, () -> {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
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
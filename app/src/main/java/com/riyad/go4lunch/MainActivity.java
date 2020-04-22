package com.riyad.go4lunch;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.riyad.go4lunch.fragments.RestaurantsFragment;
import com.riyad.go4lunch.fragments.WorkmateFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.riyad.go4lunch.utils.Constants.DEFAULT_ZOOM;
import static com.riyad.go4lunch.utils.Constants.KEY_CAMERA_POSITION;
import static com.riyad.go4lunch.utils.Constants.KEY_Location;
import static com.riyad.go4lunch.utils.Constants.M_MAX_ENTRIES;
import static com.riyad.go4lunch.utils.Constants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    // For testing rv workmates


    private static final String TAG = "MainActivity";
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout myDrawerLayout;
    private NavigationView myNavView;
    private Toolbar myMainToolbar;

    // The entry point to the fused Location Provider
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-know location retrieved by the Fused Location Provider
    private Location mLastKnownLocation;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;

    //
    private boolean mLocationPermissionGranted;

    // Used for selecting the current place.
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private String[] mLikelyPlaceType;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
//        mPlacesClient = Places.createClient(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        myNavView = findViewById(R.id.main_navigation_view);
        myMainToolbar = findViewById(R.id.main_toolbar);
        myDrawerLayout = findViewById(R.id.main_drawer_layout);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        mGeoDataClient = Places.

        getLocationPermission();

        this.configureBottomView();
        this.configureNavView();
        myNavView.setCheckedItem(R.id.action_map_view);
        initPlaces();
        openMap();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                myDrawerLayout,
                myMainToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        myDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    /**
     * Save the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_Location, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    /**
     * Gets the current location of the device, and the positions the map's camera.
     */
    private void getDeviceLocation() {

        //TODO gérer les problèmes de permissions

        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    //Set the map's camera position to the current location of the device.
                    mLastKnownLocation = task.getResult();
                    if (mLastKnownLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    }
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    Log.e(TAG, "Exception: %s", task.getException());
                }

            }
        });

    }


    private void configureNavView() {
        myNavView.setNavigationItemSelectedListener(item -> updateButtons(item.getItemId()));
    }

    private void configureBottomView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateButtons(item.getItemId()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionGranted = false;
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
    private void getLocationPermission() {
        String[] perms = {ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            mLocationPermissionGranted = true;
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION, perms);
        }
    }

    private Boolean updateButtons(Integer integer) {

        switch (integer) {

            case R.id.action_map_view:
                //TODO afficher le fragement map view
                openMap();
//                showPlacesGetStarted();
                showCurrentPlace();
                break;
            case R.id.action_list_view:
                //TODO afficher le fragement list map
                displayRestaurantFragment();
//                    showPlacesGetStarted();
//              showCurrentPlace();
//                Toast.makeText(this, "Map List View", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_workmates:
                //TODO afficher le fragement workmates

                displayWorkematesFragment();

                break;
            case R.id.nav_your_lunch:
                //TODO intent vers le fragment/Activité souhaité.
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "serieux", Toast.LENGTH_LONG).show();
                intentToProfileActivity();

            case R.id.nav_logout:
                //TODO ce deconnecter.
        }

        return true;
    }

    private void openMap() {

        mMapFragment = SupportMapFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, mMapFragment).commit();
        mMapFragment.getMapAsync(this);
    }

    private void displayWorkematesFragment() {
        Log.i("MainActivity", "displayWorkerFragment");
        WorkmateFragment workmateFragment = WorkmateFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, workmateFragment).commit();
    }

    private void displayRestaurantFragment(){
        RestaurantsFragment restaurantsFragment = RestaurantsFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.activity_main_frame_layout, restaurantsFragment).commit();
    }

    private void intentToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.custom_info_title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.custom_info_snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        getDeviceLocation();
//        mMap.moveCamera(CameraUpdateFactory.newLatLng());
    }

    private void initPlaces() {
        Places.initialize(getApplicationContext(), "AIzaSyANygkUK5W0oWtDJQ6TV6dwYEdAe2zkONA");
        mPlacesClient = Places.createClient(this);
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Ex UpdateLocation: %s", e.getMessage());
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Prompts the user to select the current place from a List of Lickly places,
     * and shows the current place on the map - provided the user has granted Location Location permission.
     */

    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.TYPES);

            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    mPlacesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        mLikelyPlaceNames = new String[count];
                        mLikelyPlaceAddresses = new String[count];
                        mLikelyPlaceAttributions = new List[count];
                        mLikelyPlaceLatLngs = new LatLng[count];


                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {

                                mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                                mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                                mLikelyPlaceAttributions[i] = placeLikelihood.getPlace().getAttributions();
                                mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();


                                i++;
                                if (i > (count - 1)) {
                                    break;
                                }

                        }

                        MainActivity.this.openPlacesDialog();
                    } else {
                        Log.e(TAG, "Exception Non pas elle : %s", task.getException());
                    }
                }
            });
        } else {
            Log.i(TAG, "The User did not grant location permission.");

            Toast.makeText(this, "Aucune Permission", Toast.LENGTH_LONG).show();

            getLocationPermission();
        }
    }

    private void showPlacesGetStarted() {
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.TYPES);
        Place place;
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.i("GetStarted", String.format("Place '%s' likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                } else {
                    Exception e = task.getException();
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        Log.e("GetStarted", "Place not found" + apiException.getStatusCode());
                    }
                }

            });


        } else {
            Log.i("GetStarted", "non pas elle punaise");


        }

    }

}


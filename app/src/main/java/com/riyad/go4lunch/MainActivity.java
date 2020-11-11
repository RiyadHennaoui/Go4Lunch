package com.riyad.go4lunch;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riyad.go4lunch.fragments.RestaurantsFragment;
import com.riyad.go4lunch.fragments.WorkmateFragment;
import com.riyad.go4lunch.ui.Restaurant;
import com.riyad.go4lunch.viewmodels.RestaurantsViewModel;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.riyad.go4lunch.utils.Constants.API_KEY_PLACES;
import static com.riyad.go4lunch.utils.Constants.AUTOCOMPLETE_REQUEST_CODE;
import static com.riyad.go4lunch.utils.Constants.CURRENT_DEVICE_LOCATION;
import static com.riyad.go4lunch.utils.Constants.DEFAULT_ZOOM;
import static com.riyad.go4lunch.utils.Constants.KEY_CAMERA_POSITION;
import static com.riyad.go4lunch.utils.Constants.KEY_Location;
import static com.riyad.go4lunch.utils.Constants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;
import static com.riyad.go4lunch.utils.Constants.PLACE_ID;
import static com.riyad.go4lunch.utils.Constants.SIGN_OUT_TASK;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String TAG = "MainActivity";
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout myDrawerLayout;
    private NavigationView myNavView;
    private Toolbar myMainToolbar;
    private ImageView profileNavDrawer;
    private TextView usernameNavDrawer;
    private TextView userMailNavDrawer;
    private Button toolbarSearch;
    private SearchView searchView;
    private List<Place.Field> fields;

    // The entry point to the fused Location Provider
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-know location retrieved by the Fused Location Provider
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;


    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("go4lunch", MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        myNavView = findViewById(R.id.main_navigation_view);
        myMainToolbar = findViewById(R.id.main_toolbar);
        myDrawerLayout = findViewById(R.id.main_drawer_layout);


        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


//      TODO Modifier l'image profile dans le nav drawer problème de context avec Glide.with(getContext)??
//        userMailNavDrawer.setText(getCurrentUser().getEmail());
//        usernameNavDrawer.setText(getCurrentUser().getDisplayName());
//          Glide.with(MainActivity.this.profileNavDrawer).load(getCurrentUser().getPhotoUrl()).centerCrop().into(profileNavDrawer);



        getLocationPermission();
        myMainToolbar.inflateMenu(R.menu.action_bar_menu);
        this.configureToolbar();
        this.configureBottomView();
        this.configureNavView();
        myNavView.setCheckedItem(R.id.action_map_view);
//        initPlaces();
//        openMap();

        configureActionBarDrawer();
        View headerView = myNavView.getHeaderView(0);
        profileNavDrawer = headerView.findViewById(R.id.nav_header_profile_picture);
        usernameNavDrawer = headerView.findViewById(R.id.nav_header_profile_name);
        userMailNavDrawer = headerView.findViewById(R.id.nav_header_profile_email);
        usernameNavDrawer.setText(getCurrentUser().getDisplayName());
        userMailNavDrawer.setText(getCurrentUser().getEmail());
        Glide.with(MainActivity.this.profileNavDrawer).load(getCurrentUser().getPhotoUrl()).centerCrop().into(profileNavDrawer);

        //Initilaze Places
        Places.initialize(getApplicationContext(), API_KEY_PLACES);

        //Creat new places client instance
        PlacesClient placesClient = Places.createClient(this);

    }

    private void configureActionBarDrawer() {
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
        locationResult.addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                //Set the map's camera position to the current location of the device.
                mLastKnownLocation = task.getResult();
                AppControler.getInstance().setCurrentLocation(mLastKnownLocation);

                if (mLastKnownLocation != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    String nearbySearchLocationFormat = mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude();

                    sharedPreferences.edit().putString(CURRENT_DEVICE_LOCATION, nearbySearchLocationFormat).apply();
                    displayRestaurants(nearbySearchLocationFormat);
                }
            } else {
                Log.d(TAG, "Current location is null. Using defaults.");
                Log.e(TAG, "Exception: %s", task.getException());
            }

        });

    }

    private void configureNavView() {
        myNavView.setNavigationItemSelectedListener(item -> updateButtons(item.getItemId()));
    }

    private void configureBottomView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateButtons(item.getItemId()));
    }

    private void configureToolbar(){
        myMainToolbar.setOnMenuItemClickListener(item -> updateButtons(item.getItemId()));
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
            openMap();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION, perms);
        }
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private Boolean updateButtons(Integer integer) {

        switch (integer) {

            //TODO ajouter le case de la searchView.
            case R.id.action_map_view:
                openMap();
                break;

            case R.id.action_list_view:
                displayRestaurantFragment();
                break;

            case R.id.action_workmates:
                displayWorkematesFragment();
                break;

            case R.id.nav_your_lunch:
                //TODO intent vers le fragment/Activité souhaité.
                break;

            case R.id.nav_settings:
                Toast.makeText(this, "serieux", Toast.LENGTH_LONG).show();
                intentToProfileActivity();

            case R.id.nav_logout:
                signOutUserFromFirebase();
                break;

            case R.id.menu_search:
                Log.e("in Search", "in");
                autocompleltePlace();
                break;
                //TODO ce deconnecter.
        }

        return true;
    }

    private void displayRestaurants(String nearbySearchLocationFormat) {
        RestaurantsViewModel restaurantsViewModel;
        restaurantsViewModel = ViewModelProviders.of(MainActivity.this).get(RestaurantsViewModel.class);
        restaurantsViewModel.init(nearbySearchLocationFormat);
        restaurantsViewModel.getRestaurantRepository().observe(MainActivity.this, restaurants -> {
//                            restaurantAdapter.setData(restaurants);
            Log.e("mapList", restaurants.size() + "");

            int index = 0;
            for (Restaurant restaurant: restaurants) {
                if (!restaurants.get(index).getBookingUser().isEmpty()) {
                    //TODO trouver comment ajouter l'info de l'id du restaurant.
                    mMap.addMarker(new MarkerOptions()
                            .title(restaurant.getName())
                            .snippet(restaurant.getId())
                            .position(new LatLng(restaurant.getRestaurantLocation().getLat(), restaurant.getRestaurantLocation().getLng())))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    mMap.addMarker(new MarkerOptions()
                            .title(restaurant.getName())
                            .snippet(restaurant.getId())
                            .position(new LatLng(restaurant.getRestaurantLocation().getLat(), restaurant.getRestaurantLocation().getLng())))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }
                index++;
            }
            //TODO le rendre plus propre.

        });
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

    private void displayRestaurantFragment() {
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

//                TextView snippet = infoWindow.findViewById(R.id.custom_info_snippet);
//                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(marker -> {
            Log.e("intent marker", marker.getId());
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(PLACE_ID, marker.getSnippet());
            startActivity(intent);
            //TODO intent vers detail restaurant
            Log.e("test", "test");
        });
        getDeviceLocation();
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

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
            switch (origin){
                case SIGN_OUT_TASK:
                    finish();
                    break;
                default:
                    break;
            }
        };
    }

//    /**
//     * Displays a form allowing the user to select a place from a list of likely places.
//     */
//    private void openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // The "which" argument contains the position of the selected item.
//                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
//                String markerSnippet = mLikelyPlaceAddresses[which];
//                if (mLikelyPlaceAttributions[which] != null) {
//                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                mMap.addMarker(new MarkerOptions()
//                        .title(mLikelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet));
//
//                // Position the map's camera at the location of the marker.
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                        DEFAULT_ZOOM));
//            }
//        };
//
//        // Display the dialog.
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(R.string.pick_place)
//                .setItems(mLikelyPlaceNames, listener)
//                .show();
//    }

    public void autocompleltePlace(){
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setCountry("FR")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("Autocomplete Main", "Place : " + place.getName() + ", " + place.getId() );
            }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
                Status status = Autocomplete.getStatusFromIntent(data);
            }else if(resultCode == RESULT_CANCELED){

            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


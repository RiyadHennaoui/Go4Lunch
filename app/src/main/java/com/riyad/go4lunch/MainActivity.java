package com.riyad.go4lunch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.riyad.go4lunch.viewmodels.DetailRestaurantViewModel;
import com.riyad.go4lunch.viewmodels.RestaurantsViewModel;
import com.riyad.go4lunch.viewmodels.UserViewModel;

import org.jetbrains.annotations.NotNull;

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
    private int go4restaurantList;

    // The entry point to the fused Location Provider
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-know location retrieved by the Fused Location Provider
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;


    private SharedPreferences sharedPreferences;

    //List view Fragment
    RestaurantsFragment restaurantsFragment = RestaurantsFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getString(R.string.sharedpreference_name_go4lunch), MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        myNavView = findViewById(R.id.main_navigation_view);
        myMainToolbar = findViewById(R.id.main_toolbar);
        myDrawerLayout = findViewById(R.id.main_drawer_layout);


        go4restaurantList = getIntent().getIntExtra("fragement value", 0);

        Log.e("intent", go4restaurantList + "");

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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

        displayUserInfos();


        //Initilaze Places
        Places.initialize(getApplicationContext(), API_KEY_PLACES);

        //Creat new places client instance
        PlacesClient placesClient = Places.createClient(this);


        if(go4restaurantList != 0){
            displayRestaurantFragment();
        }

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

    private void displayUserInfos(){
        UserViewModel userViewModel = initUserViewModel();
        userViewModel.getCurrentUserInFirestore().observe(MainActivity.this, user -> {
            usernameNavDrawer.setText(user.getmUsername());
            userMailNavDrawer.setText(user.getmMail());
            Glide.with(MainActivity.this.profileNavDrawer).load(user.getmUrlPicture()).circleCrop().into(profileNavDrawer);
        });
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

    private void configureToolbar() {
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
                goToUserRestaurentBook();
                break;

            case R.id.nav_settings:
                intentToProfileActivity();
                break;

            case R.id.nav_logout:
                signOutUserFromFirebase();
                break;

            case R.id.menu_search:
                autocompleltePlace();
                break;
            //TODO ce deconnecter.
        }

        return true;
    }

    private void displayAutocompleteRestaurant(String restaurantId) {

        DetailRestaurantViewModel detailRestaurantViewModel;
        detailRestaurantViewModel = ViewModelProviders.of(MainActivity.this).get(DetailRestaurantViewModel.class);
        detailRestaurantViewModel.init();
        detailRestaurantViewModel.getDetailRestaurant(restaurantId).observe(MainActivity.this, restaurant -> {

            if (restaurant == null) {
                Toast.makeText(this, R.string.mainactivty_toast_restaurant_not_in_range, Toast.LENGTH_SHORT).show();
            } else {
                mMap.addMarker(new MarkerOptions()
                        .title(restaurant.getName())
                        .snippet(restaurant.getId())
                        .position(new LatLng(restaurant.getRestaurantLocation().getLat(),
                                restaurant.getRestaurantLocation().getLng())));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(restaurant.getRestaurantLocation().getLat(),
                                restaurant.getRestaurantLocation().getLng()), DEFAULT_ZOOM));
            }
        });


    }

    private void displayRestaurants(String nearbySearchLocationFormat) {
        RestaurantsViewModel restaurantsViewModel;
        restaurantsViewModel = ViewModelProviders.of(MainActivity.this).get(RestaurantsViewModel.class);
        restaurantsViewModel.init(nearbySearchLocationFormat);
        restaurantsViewModel.getRestaurantRepository().observe(MainActivity.this, restaurants -> addMarkers(restaurants));
    }

    private void addMarkers(List<Restaurant> restaurants) {
        int index = 0;
        for (Restaurant restaurant : restaurants) {
            if (!restaurants.get(index).getBookingRestaurant().isEmpty()) {
                mMap.addMarker(new MarkerOptions()
                        .title(restaurant.getName())
                        .snippet(restaurant.getId())
                        .position(new LatLng(restaurant.getRestaurantLocation().getLat(), restaurant.getRestaurantLocation().getLng())))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_restaurant_book));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .title(restaurant.getName())
                        .snippet(restaurant.getId())
                        .position(new LatLng(restaurant.getRestaurantLocation().getLat(), restaurant.getRestaurantLocation().getLng())))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_restaurant_not_book));
            }
            index++;
        }
    }

    private void openMap() {

        mMapFragment = SupportMapFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, mMapFragment).commit();
        mMapFragment.getMapAsync(this);


    }

    private void displayWorkematesFragment() {
        WorkmateFragment workmateFragment = WorkmateFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, workmateFragment).commit();
    }

    private void displayRestaurantFragment() {

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
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                       findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.custom_info_title);
                title.setText(marker.getTitle());

                return infoWindow;
            }
        });

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(marker -> {
            intentToDetailRestaurant(marker.getSnippet());
        });
        getDeviceLocation();
    }

    private void intentToDetailRestaurant(String restaurantId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(PLACE_ID, restaurantId);
        startActivity(intent);
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

    private void signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return aVoid -> {
            switch (origin) {
                case SIGN_OUT_TASK:
                    finish();
                    break;
                default:
                    break;
            }
        };
    }

    public void autocompleltePlace() {
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setCountry("FR")
                .setLocationBias(RectangularBounds.newInstance(setBounds(mLastKnownLocation, 1000)))
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                if (restaurantsFragment.isVisible()) {
                    restaurantsFragment.displayAutocompleteRestaurant(place.getId());
                } else {
                    displayAutocompleteRestaurant(place.getId());
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {

            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private LatLngBounds setBounds(Location location, int mDistanceInMeters) {
        double latRadian = Math.toRadians(location.getLatitude());

        double degLatKm = 110.574235;
        double degLongKm = 110.572833 * Math.cos(latRadian);
        double deltaLat = mDistanceInMeters / 1000.0 / degLatKm;
        double deltaLong = mDistanceInMeters / 1000.0 / degLongKm;

        double minLat = location.getLatitude() - deltaLat;
        double minLong = location.getLongitude() - deltaLong;
        double maxLat = location.getLatitude() + deltaLat;
        double maxLong = location.getLongitude() + deltaLong;



        return new LatLngBounds(new LatLng(minLat, minLong), new LatLng(maxLat, maxLong));
    }

    private void goToUserRestaurentBook(){
        UserViewModel userViewModel = initUserViewModel();
        userViewModel.getCurrentUserInFirestore().observe(MainActivity.this, user -> {
            if (user.getBookingRestaurant().getRestaurantId() != null) {
                String restaurantId = user.getBookingRestaurant().getRestaurantId();
                intentToDetailRestaurant(restaurantId);
            }else{
                Toast.makeText(this, R.string.navigation_drawer_toast_your_lunch_no_lunch, Toast.LENGTH_SHORT).show();
            }
            });
    }

    @NotNull
    private UserViewModel initUserViewModel() {
        UserViewModel userViewModel;
        userViewModel = ViewModelProviders.of(MainActivity.this).get(UserViewModel.class);
        userViewModel.init();
        return userViewModel;
    }


}


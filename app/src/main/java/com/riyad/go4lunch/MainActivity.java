package com.riyad.go4lunch;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.riyad.go4lunch.utils.Constants.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout myDrawerLayout;
    private NavigationView myNavView;
    private Toolbar myMainToolbar;
    private FusedLocationProviderClient mFusedLocationProviderClient;


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


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        myNavView = findViewById(R.id.main_navigation_view);
        myMainToolbar = findViewById(R.id.main_toolbar);
        myDrawerLayout = findViewById(R.id.main_drawer_layout);

        requiresLocationPermission();

        this.configureBottomView();
        this.configureNavView();
        myNavView.setCheckedItem(R.id.action_map_view);
        openMap();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                myDrawerLayout,
                myMainToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        myDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


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

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
    private void requiresLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
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

                break;
            case R.id.action_list_view:
                //TODO afficher le fragement list map

//                Toast.makeText(this, "Map List View", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_workmates:
                //TODO afficher le fragement workmates
                Toast.makeText(this, "Workmates", Toast.LENGTH_LONG).show();
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

    private void intentToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

       Task locationResult =  mFusedLocationProviderClient.getLastLocation();
       locationResult.addOnCompleteListener(this, new OnCompleteListener() {
           @Override
           public void onComplete(@NonNull Task task) {
               if(task.isSuccessful()){

                   

               }
           }
       })
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng());
    }

    private void openMap() {
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.activity_main_frame_layout, mMapFragment);
        ft.commit();
        mMapFragment.getMapAsync(this);
    }

}

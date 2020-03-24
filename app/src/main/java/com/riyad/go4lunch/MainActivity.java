package com.riyad.go4lunch;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {


    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout myDrawerLayout;
    private NavigationView myNavView;
    private Toolbar myMainToolbar;


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


//        this.setSupportActionBar(myMainToolbar);
        this.configureBottomView();
        this.configureNavView();

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

    private Boolean updateButtons(Integer integer) {

        switch (integer) {

            case R.id.action_map_view:
                //TODO afficher le fragement map view

                mMapFragment = MapFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.activity_main_frame_layout, mMapFragment);
                ft.commit();

//                fm.beginTransaction().hide(active).show(mapFragment).commit();
//                active = mapFragment;
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}

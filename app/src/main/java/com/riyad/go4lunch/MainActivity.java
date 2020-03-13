package com.riyad.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


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
//        myNavView = findViewById(R.id.main_drawer_layout);
        myMainToolbar = findViewById(R.id.main_toolbar);
        myDrawerLayout =findViewById(R.id.main_drawer_layout);

        this.setSupportActionBar(myMainToolbar);
        this.configureBottomView();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                myDrawerLayout,
                myMainToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        myDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureBottomView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateBottons(item.getItemId()));
    }

    private Boolean updateBottons(Integer integer) {

        switch (integer) {

            case R.id.action_map_view:
                //TODO afficher le fragement map view
//                Toast.makeText(this, "Map View", Toast.LENGTH_LONG).show();
                intentToProfileActivity();
                break;
            case R.id.action_list_view:
                //TODO afficher le fragement list map
                Toast.makeText(this, "Map List View", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_workmates:
                //TODO afficher le fragement workmates
                Toast.makeText(this, "Workmates", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_your_lunch:
                //TODO intent vers le fragment/Activité souhaité.
                break;
            case R.id.nav_settings:
                Toast.makeText(this,"serieux",Toast.LENGTH_LONG).show();
                intentToProfileActivity();

            case R.id.nav_logout:
                //TODO ce deconnecter.
        }

        return true;
    }

    private void intentToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }



}

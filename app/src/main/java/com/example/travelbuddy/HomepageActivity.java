package com.example.travelbuddy;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.travelbuddy.Maps.MapsFragment;
import com.example.travelbuddy.Profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

// Default fragment when opening homepage (optional)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_fragment_container, new MapsFragment())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();

            if (id == R.id.nav_itinerary) {
                selected = new ItineraryFragment();
            } else if (id == R.id.nav_maps) {
                selected = new MapsFragment();   // this is enough
            } else if (id == R.id.nav_profile) {
                selected = new ProfileFragment();
            } else if (id == R.id.nav_create) {
                selected = new CreateFragment();
            }

            if (selected != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_fragment_container, selected)
                        .commit();
                return true;
            }
            return false;
        });



    }
}
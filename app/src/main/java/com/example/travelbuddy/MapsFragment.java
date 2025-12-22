package com.example.travelbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private BottomSheetBehavior<MaterialCardView> bottomSheetBehavior;

    public MapsFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // fragment_maps = CoordinatorLayout with map_container + info_sheet
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ----- MAP SETUP (your existing code) -----
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map_container);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);

        // ----- BOTTOM SHEET SETUP (added) -----
        MaterialCardView sheet = view.findViewById(R.id.info_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(sheet);

        // allow 3 states: collapsed -> half -> expanded
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.28f);  // your “1/3” state

        int peek = getResources().getDimensionPixelSize(R.dimen.bottom_sheet_peek);
        bottomSheetBehavior.setPeekHeight(peek);          // edge height when collapsed

        // set max expanded height to about 70% of screen
        sheet.post(() -> {
            int parentHeight = ((View) sheet.getParent()).getHeight();
            int expandedOffset = (int) (parentHeight * 0.30f);  // top is 30% from top -> 70% sheet [web:22][web:25]
            bottomSheetBehavior.setExpandedOffset(expandedOffset);
        });

        // start with only the edge visible (state 3)
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        // optional: listen for state changes / sliding
        bottomSheetBehavior.addBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        // handle EXPANDED, HALF_EXPANDED, COLLAPSED if you want
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // slideOffset from -1 to 1
                    }
                }
        );


        // TAB
        Button btnFriends = view.findViewById(R.id.btn_friends);
        Button btnItinerary = view.findViewById(R.id.btn_itinerary);

        // turn off default background tint that can hide your selector
        btnFriends.setBackgroundTintList(null);
        btnItinerary.setBackgroundTintList(null);

        View.OnClickListener tabClick = v -> {
            boolean friends = v.getId() == R.id.btn_friends;
            btnFriends.setSelected(friends);
            btnItinerary.setSelected(!friends);

            // later: swap RecyclerView adapter here
        };

        btnFriends.setOnClickListener(tabClick);
        btnItinerary.setOnClickListener(tabClick);

        // default: Friends selected
        btnFriends.setSelected(true);
        btnItinerary.setSelected(false);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        LatLng tokyo = new LatLng(35.6895, 139.6917);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tokyo, 10f));
    }

    // helper methods if you want to trigger states manually

    private void expandFull() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);      // full screen
    }

    private void expandOneThird() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED); // 1/3 screen
    }

    private void showEdgeOnly() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);     // only edge
    }
}

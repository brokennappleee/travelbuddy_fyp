package com.example.travelbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

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

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;

import java.util.Arrays;
import java.util.List;


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

        // turn off default background tint that can hide selector
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

        RecyclerView rv = view.findViewById(R.id.rv_items);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addItemDecoration(new DividerItemDecoration(
                requireContext(), DividerItemDecoration.VERTICAL));

        // temporary hard‑coded data
        List<Friend> friends = Arrays.asList(
                new Friend("Karen Li", "Tuen Mun ZVE", "10:30 pm"),
                new Friend("Lam", "Ocean Park", "11:00 pm"),
                new Friend("Miffy Chan", "Space Museum", "11:00 pm"),
                new Friend("Hello Kitty", "Linda Hotel", "11:00 pm")
        );

        FriendsAdapter friendsAdapter = new FriendsAdapter(friends);
        rv.setAdapter(friendsAdapter);

        // Search Bar Mock up data
        AutoCompleteTextView etSearch = view.findViewById(R.id.et_search);

        // temporary suggestions; later replace with real data
        String[] suggestions = new String[] {
                "Tuen Mun ZVE",
                "Ocean Park",
                "Space Museum",
                "Linda Hotel",
                "Karen Li",
                "Miffy Chan"
        };

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions
        );

        etSearch.setAdapter(searchAdapter);
        etSearch.setThreshold(1); // start suggesting after 1 character typed



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

    //Displaying Friends
    static class Friend {
        String name, location, since;
        Friend(String n, String l, String s) { name=n; location=l; since=s; }
    }

    static class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.VH> {
        private final List<Friend> data;
        FriendsAdapter(List<Friend> data) { this.data = data; }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvInitial, tvName, tvLocation, tvSince;
            VH(View itemView) {
                super(itemView);
                tvInitial = itemView.findViewById(R.id.tv_initial);
                tvName = itemView.findViewById(R.id.tv_name);
                tvLocation = itemView.findViewById(R.id.tv_location);
                tvSince = itemView.findViewById(R.id.tv_since);
            }
        }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friend, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int position) {
            Friend f = data.get(position);
            h.tvInitial.setText(f.name.substring(0, 1));
            h.tvName.setText(f.name);
            h.tvLocation.setText("At " + f.location);
            h.tvSince.setText("Since " + f.since);
        }

        @Override
        public int getItemCount() { return data.size(); }
    }

}

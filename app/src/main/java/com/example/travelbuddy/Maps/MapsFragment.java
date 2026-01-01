package com.example.travelbuddy.Maps;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private BottomSheetBehavior<MaterialCardView> bottomSheetBehavior;

    // list + adapters
    private RecyclerView rvItems;
    private FriendsAdapter friendsAdapter;
    private ItineraryDaysAdapter itineraryAdapter;

    public MapsFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ----- MAP SETUP -----
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

        // ----- BOTTOM SHEET -----
        MaterialCardView sheet = view.findViewById(R.id.info_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(sheet);

        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.28f);

        int peek = getResources().getDimensionPixelSize(R.dimen.bottom_sheet_peek);
        bottomSheetBehavior.setPeekHeight(peek);

        sheet.post(() -> {
            int parentHeight = ((View) sheet.getParent()).getHeight();
            int expandedOffset = (int) (parentHeight * 0.30f);
            bottomSheetBehavior.setExpandedOffset(expandedOffset);
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // optional listener (you can leave empty)
        bottomSheetBehavior.addBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) { }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
                }
        );

        // ----- TABS -----
        Button btnFriends = view.findViewById(R.id.btn_friends);
        Button btnItinerary = view.findViewById(R.id.btn_itinerary);

        btnFriends.setBackgroundTintList(null);
        btnItinerary.setBackgroundTintList(null);

        // ----- RECYCLER VIEW + ADAPTERS -----
        rvItems = view.findViewById(R.id.rv_items);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.addItemDecoration(new DividerItemDecoration(
                requireContext(), DividerItemDecoration.VERTICAL));

        // friends dummy data
        List<Friend> friends = Arrays.asList(
                new Friend("Karen Li", "Tuen Mun ZVE", "10:30 pm"),
                new Friend("Lam", "Ocean Park", "11:00 pm"),
                new Friend("Miffy Chan", "Space Museum", "11:00 pm"),
                new Friend("Hello Kitty", "Linda Hotel", "11:00 pm")
        );
        friendsAdapter = new FriendsAdapter(friends);

        // itinerary dummy data
        itineraryAdapter = new ItineraryDaysAdapter(buildDummyItineraryDays());

        // default tab = Friends
        rvItems.setAdapter(friendsAdapter);
        btnFriends.setSelected(true);
        btnItinerary.setSelected(false);

        btnFriends.setOnClickListener(v -> {
            btnFriends.setSelected(true);
            btnItinerary.setSelected(false);
            rvItems.setAdapter(friendsAdapter);
        });

        btnItinerary.setOnClickListener(v -> {
            btnFriends.setSelected(false);
            btnItinerary.setSelected(true);
            rvItems.setAdapter(itineraryAdapter);
        });

        // ----- SEARCH BAR MOCK DATA -----
        AutoCompleteTextView etSearch = view.findViewById(R.id.et_search);

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
        etSearch.setThreshold(1);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        LatLng tokyo = new LatLng(35.6895, 139.6917);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tokyo, 10f));
    }

    // ----- optional helpers for sheet -----

    private void expandFull() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void expandOneThird() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void showEdgeOnly() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    // ---------- dummy itinerary days ----------

    private List<ItineraryDayItem> buildDummyItineraryDays() {
        List<ItineraryDayItem> list = new ArrayList<>();
        list.add(new ItineraryDayItem("Day 1 - 15 Oct 2026", "10 total destinations"));
        list.add(new ItineraryDayItem("Day 2 - 16 Oct 2026", "6 total destinations"));
        list.add(new ItineraryDayItem("Day 3 - 17 Oct 2026", "8 total destinations"));
        list.add(new ItineraryDayItem("Day 4 - 18 Oct 2026", "11 total destinations"));
        list.add(new ItineraryDayItem("Day 5 - 19 Oct 2026", "7 total destinations"));
        list.add(new ItineraryDayItem("Day 6 - 20 Oct 2026", "3 total destinations"));
        return list;
    }

    // ---------- Friends list classes ----------

    static class Friend {
        String name, location, since;
        Friend(String n, String l, String s) { name = n; location = l; since = s; }
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

        @NonNull
        @Override
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

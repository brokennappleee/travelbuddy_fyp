package com.example.travelbuddy;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelbuddy.Itinerary.TripDetailFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ItineraryFragment extends Fragment
        implements TripDetailFragment.OnTripTitleChangedListener {

    private final List<Trip> allTrips = new ArrayList<>();
    @Nullable private String lastOpenedTripId = null;

    private Trip recentTrip;
    private final List<Trip> yourTrips = new ArrayList<>();

    private TripsAdapter tripsAdapter;

    public ItineraryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itinerary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) only create mock data once
        if (allTrips.isEmpty()) {
            loadMockTrips();
        }
        // 2) ALWAYS recompute recentTrip + yourTrips from allTrips
        splitTrips();

        CardView cardRecent = view.findViewById(R.id.card_recent_trip);
        ImageView imgRecent = view.findViewById(R.id.iv_trip_cover);
        TextView tvRecentTitle = view.findViewById(R.id.tv_trip_title);
        TextView tvRecentDate = view.findViewById(R.id.tv_trip_date);
        ImageButton btnToggle = view.findViewById(R.id.btn_toggle_recent);
        RecyclerView rvYourTrips = view.findViewById(R.id.rv_your_trips);

        // recent card data
        if (recentTrip == null) {
            cardRecent.setVisibility(View.GONE);
        } else {
            cardRecent.setVisibility(View.VISIBLE);
            imgRecent.setImageResource(recentTrip.coverResId);
            tvRecentTitle.setText(recentTrip.title);
            tvRecentDate.setText(recentTrip.dateRange);
        }

        // recent card three-dots
        View recentCardContent = cardRecent.findViewById(R.id.trip_card_root);
        View btnMoreRecent = recentCardContent.findViewById(R.id.btn_more);
        btnMoreRecent.setOnClickListener(v -> {
            if (recentTrip != null) {
                showTripSettingsSheet(recentTrip);
            }
        });

        // RecyclerView
        rvYourTrips.setLayoutManager(new LinearLayoutManager(getContext()));

        tripsAdapter = new TripsAdapter(yourTrips,
                new TripsAdapter.OnTripClickListener() {
                    @Override
                    public void onTripClick(Trip trip) {
                        // remember last opened
                        lastOpenedTripId = trip.id;

                        // create TripDetailFragment and pass id + data
                        TripDetailFragment detail = TripDetailFragment.newInstance(
                                trip.id,
                                trip.title,
                                trip.dateRange
                        );

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_fragment_container, detail)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onTripMoreClick(Trip trip, View anchor) {
                        showTripSettingsSheet(trip);
                    }
                });

        rvYourTrips.setAdapter(tripsAdapter);

        // toggle recent card
        final boolean[] isRecentVisible = { true };
        btnToggle.setOnClickListener(v -> {
            isRecentVisible[0] = !isRecentVisible[0];
            cardRecent.setVisibility(isRecentVisible[0] ? View.VISIBLE : View.GONE);
            btnToggle.setImageResource(
                    isRecentVisible[0] ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_right
            );
        });
    }

    // ---------- TripDetailFragment.OnTripTitleChangedListener ----------

    @Override
    public void onTripTitleChanged(String tripId, String newTitle) {
        // update in allTrips
        Trip trip = findTripById(tripId);
        if (trip != null) {
            trip.title = newTitle;
        }

        // re-split into recent + yourTrips using updated allTrips
        splitTrips();

        // refresh whole list (simple for now)
        if (tripsAdapter != null) {
            tripsAdapter.notifyDataSetChanged();
        }
    }

    private Trip findTripById(String tripId) {
        for (Trip t : allTrips) {
            if (t.id.equals(tripId)) {
                return t;
            }
        }
        return null;
    }

    // ---------- data helpers ----------

    private void loadMockTrips() {
        allTrips.clear();
        allTrips.add(new Trip("1", "Paris with the city Girls", "Paris",
                "Oct 20 – 27, 2023", R.drawable.swissalps));
        allTrips.add(new Trip("2", "Swiss Alps Adventure", "Swiss Alps",
                "Jul 1 – 8, 2024", R.drawable.swissalps));
        allTrips.add(new Trip("3", "Fucking Roman LETS GO", "Rome",
                "Dec 15 – 19, 2024", R.drawable.swissalps));
        allTrips.add(new Trip("4", "New York Baby", "New York",
                "Jul 1 – 8, 2024", R.drawable.swissalps));
    }

    private void splitTrips() {
        // recentTrip: last opened if exists, otherwise last in list
        recentTrip = null;
        if (lastOpenedTripId != null) {
            for (Trip t : allTrips) {
                if (t.id.equals(lastOpenedTripId)) {
                    recentTrip = t;
                    break;
                }
            }
        }
        if (recentTrip == null && !allTrips.isEmpty()) {
            recentTrip = allTrips.get(allTrips.size() - 1);
        }

        // yourTrips: everything except recentTrip
        yourTrips.clear();
        for (Trip t : allTrips) {
            if (recentTrip == null || !t.id.equals(recentTrip.id)) {
                yourTrips.add(t);
            }
        }
    }


    // ---------- bottom sheet & delete ----------

    private void showTripSettingsSheet(Trip trip) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View sheetView = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_trip_actions, null);
        dialog.setContentView(sheetView);

        View rowShare      = sheetView.findViewById(R.id.row_share);
        View rowEditTitle  = sheetView.findViewById(R.id.row_edit_title);
        View rowEditDate   = sheetView.findViewById(R.id.row_edit_date);
        View rowPrivacy    = sheetView.findViewById(R.id.row_privacy);
        View rowDeleteTrip = sheetView.findViewById(R.id.row_delete_trip);

        rowShare.setOnClickListener(v -> {
            // TODO
            dialog.dismiss();
        });

        rowEditTitle.setOnClickListener(v -> {
            // TODO
            dialog.dismiss();
        });

        rowEditDate.setOnClickListener(v -> {
            // TODO
            dialog.dismiss();
        });

        rowPrivacy.setOnClickListener(v -> {
            // TODO
            dialog.dismiss();
        });

        rowDeleteTrip.setOnClickListener(v -> {
            dialog.dismiss();
            showDeleteConfirmDialog(trip);
        });

        dialog.show();
    }

    private void showDeleteConfirmDialog(Trip trip) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Delete \"" + trip.title + "\"?")
                .setMessage("Are you sure you want to delete this trip?")
                .setCancelable(false)
                .setPositiveButton("Yes, delete it", (d, which) -> deleteTrip(trip))
                .setNegativeButton("No, keep it", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.red));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.black));
        });

        dialog.show();
    }

    private void deleteTrip(Trip trip) {
        allTrips.remove(trip);
        splitTrips();
        tripsAdapter.notifyDataSetChanged();
    }
}

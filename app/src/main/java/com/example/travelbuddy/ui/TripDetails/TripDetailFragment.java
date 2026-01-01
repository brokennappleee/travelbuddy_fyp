package com.example.travelbuddy.ui.TripDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelbuddy.R;
import com.example.travelbuddy.ui.ItinerarySectionFragments.TripFragment;

public class TripDetailFragment extends Fragment {

    private String title;
    private String dateRange;

    // nav views
    private LinearLayout tabTrip, tabFlight, tabLodging, tabRental, tabOthers;
    private ImageView iconTrip, iconFlight, iconLodging, iconRental, iconOthers;
    private TextView labelTrip, labelFlight, labelLodging, labelRental, labelOthers;

    private int colorActive;
    private int colorInactive;

    private enum Tab { TRIP, FLIGHT, LODGING, RENTAL, OTHERS }

    // Factory method used from ItineraryFragment
    public static TripDetailFragment newInstance(String title, String dateRange) {
        TripDetailFragment f = new TripDetailFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("dateRange", dateRange);
        f.setArguments(b);
        return f;
    }

    public TripDetailFragment() {
        // empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            dateRange = getArguments().getString("dateRange");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_trip_detail_fragment, container, false);

        // Header: set trip title and dates
        TextView tvTitle = view.findViewById(R.id.tv_trip_title);
        TextView tvDates = view.findViewById(R.id.tv_trip_dates);
        tvTitle.setText(title);
        tvDates.setText(dateRange);

        // default inner fragment = Trip
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, new TripFragment())
                .commit();

        // ---------- nav color + click logic ----------

        colorActive = requireContext().getColor(R.color.skyblue);
        colorInactive = requireContext().getColor(R.color.darkblack);

        // find nav views
        tabTrip    = view.findViewById(R.id.tab_trip);
        tabFlight  = view.findViewById(R.id.tab_flight);
        tabLodging = view.findViewById(R.id.tab_lodging);
        tabRental  = view.findViewById(R.id.tab_rental);
        tabOthers  = view.findViewById(R.id.tab_others);

        iconTrip   = (ImageView) tabTrip.getChildAt(0);
        labelTrip  = (TextView)  tabTrip.getChildAt(1);

        iconFlight  = (ImageView) tabFlight.getChildAt(0);
        labelFlight = (TextView)  tabFlight.getChildAt(1);

        iconLodging  = (ImageView) tabLodging.getChildAt(0);
        labelLodging = (TextView)  tabLodging.getChildAt(1);

        iconRental  = (ImageView) tabRental.getChildAt(0);
        labelRental = (TextView)  tabRental.getChildAt(1);

        iconOthers  = (ImageView) tabOthers.getChildAt(0);
        labelOthers = (TextView)  tabOthers.getChildAt(1);

        // default tab highlight
        switchTab(Tab.TRIP);

        // click listeners
        tabTrip.setOnClickListener(v -> switchTab(Tab.TRIP));
        tabFlight.setOnClickListener(v -> switchTab(Tab.FLIGHT));
        tabLodging.setOnClickListener(v -> switchTab(Tab.LODGING));
        tabRental.setOnClickListener(v -> switchTab(Tab.RENTAL));
        tabOthers.setOnClickListener(v -> switchTab(Tab.OTHERS));

        // this must be the LAST line
        return view;
    }



    private void switchTab(Tab tab) {
        Fragment content;

        switch (tab) {
            case FLIGHT:
                content = new FlightFragment();   // TODO: create these fragments
                break;
            case LODGING:
                content = new LodgingFragment();
                break;
            case RENTAL:
                content = new DetailsFragment();  // you named this "Details" in XML
                break;
            case OTHERS:
                content = new OthersFragment();
                break;
            case TRIP:
            default:
                content = new TripFragment();
                break;
        }

        // swap inner fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, content)
                .commit();

        // update nav colors
        setAllInactive();
        switch (tab) {
            case TRIP:
                setActive(iconTrip, labelTrip);
                break;
            case FLIGHT:
                setActive(iconFlight, labelFlight);
                break;
            case LODGING:
                setActive(iconLodging, labelLodging);
                break;
            case RENTAL:
                setActive(iconRental, labelRental);
                break;
            case OTHERS:
                setActive(iconOthers, labelOthers);
                break;
        }
    }

    private void setAllInactive() {
        setInactive(iconTrip, labelTrip);
        setInactive(iconFlight, labelFlight);
        setInactive(iconLodging, labelLodging);
        setInactive(iconRental, labelRental);
        setInactive(iconOthers, labelOthers);
    }

    private void setActive(ImageView icon, TextView label) {
        icon.setColorFilter(colorActive);
        label.setTextColor(colorActive);
    }

    private void setInactive(ImageView icon, TextView label) {
        icon.setColorFilter(colorInactive);
        label.setTextColor(colorInactive);
    }
}

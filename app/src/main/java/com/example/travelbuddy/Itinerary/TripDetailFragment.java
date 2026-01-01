package com.example.travelbuddy.Itinerary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.travelbuddy.R;
import com.example.travelbuddy.ui.ItinerarySectionFragments.TripFragment;

public class TripDetailFragment extends Fragment {

    // ------- data -------
    private String tripId;
    private String title;
    private String dateRange;

    // callback to list screen
    public interface OnTripTitleChangedListener {
        void onTripTitleChanged(String tripId, String newTitle);
    }

    private OnTripTitleChangedListener titleChangedListener;

    // ------- header views -------
    private TextView tvTitle;
    private EditText etTitle;
    private boolean isEditingTitle = false;

    // ------- nav views -------
    private LinearLayout tabTrip, tabFlight, tabLodging, tabRental, tabOthers;
    private ImageView iconTrip, iconFlight, iconLodging, iconRental, iconOthers;
    private TextView labelTrip, labelFlight, labelLodging, labelRental, labelOthers;

    private int colorActive;
    private int colorInactive;

    private enum Tab { TRIP, FLIGHT, LODGING, RENTAL, OTHERS }

    // ------- factory method -------
    public static TripDetailFragment newInstance(String tripId, String title, String dateRange) {
        TripDetailFragment f = new TripDetailFragment();
        Bundle b = new Bundle();
        b.putString("tripId", tripId);
        b.putString("title", title);
        b.putString("dateRange", dateRange);
        f.setArguments(b);
        return f;
    }

    public TripDetailFragment() {
        // empty public constructor
    }

    // ------- lifecycle -------

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnTripTitleChangedListener) {
            titleChangedListener = (OnTripTitleChangedListener) context;
        } else if (getParentFragment() instanceof OnTripTitleChangedListener) {
            titleChangedListener = (OnTripTitleChangedListener) getParentFragment();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripId = getArguments().getString("tripId");
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

        // insets padding at the very top
        View root = view.findViewById(R.id.trip_detail_root);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            v.setPadding(
                    v.getPaddingLeft(),
                    top,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });

        // ------- header: title + dates -------
        tvTitle = view.findViewById(R.id.tv_trip_title);
        etTitle = view.findViewById(R.id.et_trip_title);
        TextView tvDates = view.findViewById(R.id.tv_trip_dates);

        tvTitle.setText(title);
        etTitle.setText(title);
        tvDates.setText(dateRange);

        // title edit setup
        tvTitle.setOnClickListener(v1 -> startEditTitle());

        etTitle.setOnEditorActionListener((v12, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                finishEditTitle();
                return true;
            }
            return false;
        });

        etTitle.setOnFocusChangeListener((v13, hasFocus) -> {
            if (!hasFocus && isEditingTitle) {
                finishEditTitle();
            }
        });

        // ------- default inner fragment = Trip -------
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, new TripFragment())
                .commit();

        // ------- nav color + click logic -------
        colorActive = requireContext().getColor(R.color.skyblue);
        colorInactive = requireContext().getColor(R.color.darkblack);

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

        switchTab(Tab.TRIP);

        tabTrip.setOnClickListener(v1 -> switchTab(Tab.TRIP));
        tabFlight.setOnClickListener(v -> {
            switchTab(Tab.FLIGHT);

            // TODO: later: only show if no flights in DB
            FlightEmptySheetFragment sheet = new FlightEmptySheetFragment();
            sheet.show(getChildFragmentManager(), "FlightEmptySheet");
        });

        tabLodging.setOnClickListener(v1 -> switchTab(Tab.LODGING));
        tabRental.setOnClickListener(v1 -> switchTab(Tab.RENTAL));
        tabOthers.setOnClickListener(v1 -> switchTab(Tab.OTHERS));

        return view;
    }

    // ------- title helpers -------

    private void startEditTitle() {
        isEditingTitle = true;
        tvTitle.setVisibility(View.GONE);
        etTitle.setVisibility(View.VISIBLE);
        etTitle.requestFocus();
        etTitle.setSelection(etTitle.getText().length());

        InputMethodManager imm =
                (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etTitle, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void finishEditTitle() {
        isEditingTitle = false;
        String newTitle = etTitle.getText().toString().trim();
        if (!newTitle.isEmpty()) {
            title = newTitle;
            tvTitle.setText(newTitle);

            // IMPORTANT: notify ItineraryFragment
            if (titleChangedListener != null) {
                titleChangedListener.onTripTitleChanged(tripId, newTitle);
            }
        }

        etTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);

        InputMethodManager imm =
                (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
        }
    }


    // ------- nav helpers -------

    private void switchTab(Tab tab) {
        Fragment content;

        switch (tab) {
            case FLIGHT:
                content = new FlightFragment();   // TODO
                break;
            case LODGING:
                content = new LodgingFragment();  // TODO
                break;
            case RENTAL:
                content = new DetailsFragment();  // TODO
                break;
            case OTHERS:
                content = new OthersFragment();   // TODO
                break;
            case TRIP:
            default:
                content = new TripFragment();
                break;
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, content)
                .commit();

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

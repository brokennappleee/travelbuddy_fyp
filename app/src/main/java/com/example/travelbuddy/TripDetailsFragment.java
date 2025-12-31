package com.example.travelbuddy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class TripDetailsFragment extends Fragment {

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_DATES = "arg_dates";

    public static TripDetailsFragment newInstance(String title, String dates) {
        TripDetailsFragment f = new TripDetailsFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TITLE, title);
        b.putString(ARG_DATES, dates);
        f.setArguments(b);
        return f;
    }

    @Nullable
    private String tripTitle;
    @Nullable
    private String tripDates;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripTitle = getArguments().getString(ARG_TITLE);
            tripDates = getArguments().getString(ARG_DATES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tv_trip_title);
        TextView tvDates = view.findViewById(R.id.tv_trip_dates);

        if (tripTitle != null) tvTitle.setText(tripTitle);
        if (tripDates != null) tvDates.setText(tripDates);

        tvTitle.setOnClickListener(v -> {
            // TODO: edit title
        });

        tvDates.setOnClickListener(v -> {
            // TODO: edit dates
        });

        TabLayout tabs = view.findViewById(R.id.tab_trip_sections);
    }

}

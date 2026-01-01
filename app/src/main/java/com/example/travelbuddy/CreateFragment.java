package com.example.travelbuddy;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travelbuddy.ui.DestinationSearch.DestinationSearchFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateFragment extends Fragment {

    // ... existing ARG_PARAM1, ARG_PARAM2, constructor, newInstance, onCreate ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View containerDestination = view.findViewById(R.id.container_destination);
        EditText etDestination = view.findViewById(R.id.et_destination);

        // Single click listener for opening destination search
        View.OnClickListener openSearch = v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_fragment_container, new DestinationSearchFragment())
                    .addToBackStack("destination_search")
                    .commit();
        };
        containerDestination.setOnClickListener(openSearch);
        etDestination.setOnClickListener(openSearch);

        // Receive result from DestinationSearchFragment
        requireActivity().getSupportFragmentManager().setFragmentResultListener(
                "destination_result",
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    String city = bundle.getString("selected_city");
                    String country = bundle.getString("selected_country");
                    if (city != null && country != null) {
                        etDestination.setText(city + ", " + country);
                    }
                }
        );

        // Calendar tab
        View datesContainer = view.findViewById(R.id.container_dates);
        TextView tvStart = view.findViewById(R.id.tv_start_date);
        TextView tvEnd   = view.findViewById(R.id.tv_end_date);

        View.OnClickListener openCalendar = v -> openDateRangePicker(tvStart, tvEnd);

        datesContainer.setOnClickListener(openCalendar);
        view.findViewById(R.id.row_start_date).setOnClickListener(openCalendar);
        view.findViewById(R.id.row_end_date).setOnClickListener(openCalendar);
    }

    private void openDateRangePicker(TextView tvStart, TextView tvEnd) {
        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select trip dates")
                        .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            if (selection == null) return;
            Long start = selection.first;
            Long end = selection.second;
            if (start == null || end == null) return;

            SimpleDateFormat fmt = new SimpleDateFormat("MMM d", Locale.getDefault());
            tvStart.setText(fmt.format(new Date(start)));
            tvEnd.setText(fmt.format(new Date(end)));
            tvStart.setTextColor(requireContext().getColor(R.color.black));
            tvEnd.setTextColor(requireContext().getColor(R.color.black));
        });

        picker.show(getParentFragmentManager(), "trip_date_range");
    }
}

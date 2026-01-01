package com.example.travelbuddy.ui.ItinerarySectionFragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;


import java.util.ArrayList;
import java.util.List;

public class TripFragment extends Fragment {

    private RecyclerView rvDays;

    public TripFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        rvDays = view.findViewById(R.id.rv_days);
        rvDays.setLayoutManager(new LinearLayoutManager(getContext()));

        List<DayItem> days = buildDummyDays();
        DaysAdapter adapter = new DaysAdapter(days);
        rvDays.setAdapter(adapter);

        return view;
    }

    // TODO: Replace Dummy Days with real dta from DB or API
    private List<DayItem> buildDummyDays() {
        List<DayItem> list = new ArrayList<>();
        list.add(new DayItem("Day 1 • Mon, Oct 20", "0 items"));
        list.add(new DayItem("Day 2 • Tue, Oct 21", "0 items"));
        list.add(new DayItem("Day 3 • Wed, Oct 22", "0 items"));
        list.add(new DayItem("Day 4 • Thu, Oct 23", "0 items"));
        list.add(new DayItem("Day 5 • Fri, Oct 24", "0 items"));
        list.add(new DayItem("Day 6 • Sat, Oct 25", "0 items"));
        list.add(new DayItem("Day 7 • Sun, Oct 26", "0 items"));
        return list;
    }
}
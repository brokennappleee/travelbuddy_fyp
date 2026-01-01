package com.example.travelbuddy.Itinerary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;


public class FlightEmptySheetFragment extends BottomSheetDialogFragment {

    private LinearLayout layoutAddFlight, layoutSearchList;
    private EditText etSearchBox, etSearchList;
    private RecyclerView rvAirlines;
    private AirlineAdapter airlineAdapter;

    @Override
    public int getTheme() {
        return R.style.Theme_TravelBuddy_BottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_empty_sheet, container, false);

        layoutAddFlight   = view.findViewById(R.id.layout_add_flight);
        layoutSearchList  = view.findViewById(R.id.layout_search_list);
        etSearchBox       = view.findViewById(R.id.et_search_airline);
        etSearchList      = view.findViewById(R.id.et_search_airline_list);
        rvAirlines        = view.findViewById(R.id.rv_airlines);

        // dummy data
        List<String> airlines = Arrays.asList(
                "CommuteAir (C5)",
                "Ceiba Intercontinental (C2)",
                "Cinnamon Air (C7)",
                "Cronos Airlines (C8)"
        );

        airlineAdapter = new AirlineAdapter(airlines);
        rvAirlines.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAirlines.setAdapter(airlineAdapter);

        // when user taps search box in first state, switch to list state
        etSearchBox.setOnFocusChangeListener((v1, hasFocus) -> {
            if (hasFocus) showSearchList();
        });
        etSearchBox.setOnClickListener(v1 -> showSearchList());

        // simple filter on typing
        etSearchList.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                airlineAdapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void showSearchList() {
        layoutAddFlight.setVisibility(View.GONE);
        layoutSearchList.setVisibility(View.VISIBLE);
        etSearchList.requestFocus();
    }

    // Airline Adapter

    static class AirlineAdapter extends RecyclerView.Adapter<AirlineAdapter.VH> {

        private final List<String> all;
        private final List<String> filtered;

        AirlineAdapter(List<String> airlines) {
            this.all = new ArrayList<>(airlines);
            this.filtered = new ArrayList<>(airlines);
        }

        void filter(String query) {
            filtered.clear();
            if (query == null || query.trim().isEmpty()) {
                filtered.addAll(all);
            } else {
                String q = query.toLowerCase();
                for (String a : all) {
                    if (a.toLowerCase().contains(q)) {
                        filtered.add(a);
                    }
                }
            }
            notifyDataSetChanged();
        }

        // ViewHolder holds the TextView directly
        static class VH extends RecyclerView.ViewHolder {
            TextView tvName;
            VH(@NonNull TextView itemView) {
                super(itemView);
                tvName = itemView;
            }
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(16, 12, 16, 12);
            tv.setTextSize(14);
            return new VH(tv);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.tvName.setText(filtered.get(position));
        }

        @Override
        public int getItemCount() {
            return filtered.size();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view == null) return;

        // parent is the bottom-sheet container created by the dialog
        View parent = (View) view.getParent();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);

        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // e.g. 70% of screen height – tweak 0.7f up/down until it looks right
        int targetHeight = (int) (screenHeight * 0.7f);

        ViewGroup.LayoutParams lp = parent.getLayoutParams();
        lp.height = targetHeight;
        parent.setLayoutParams(lp);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

}

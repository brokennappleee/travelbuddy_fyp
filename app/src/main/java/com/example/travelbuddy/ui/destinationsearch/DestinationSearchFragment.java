package com.example.travelbuddy.ui.destinationsearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.travelbuddy.R;
import com.example.travelbuddy.model.Destination;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DestinationSearchFragment extends Fragment {

    private DestinationAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_destination_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etSearch = view.findViewById(R.id.et_search);
        ImageButton btnClear = view.findViewById(R.id.btn_clear);
        ImageButton btnBack  = view.findViewById(R.id.btn_back);
        RecyclerView rv      = view.findViewById(R.id.rv_destinations);

        // Back button
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // Clear search text
        btnClear.setOnClickListener(v -> etSearch.setText(""));

        // RecyclerView setup
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Destination> mock = getMockDestinations();
        adapter = new DestinationAdapter(mock, dest -> {
            Bundle result = new Bundle();
            result.putString("selected_city", dest.city);
            result.putString("selected_country", dest.country);
            getParentFragmentManager()
                    .setFragmentResult("destination_result", result);
            getParentFragmentManager().popBackStack();
        });
        rv.setAdapter(adapter);

        // Search filtering
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }


    private List<Destination> getMockDestinations() {
        List<Destination> list = new ArrayList<>();
        list.add(new Destination("Japan", "Tokyo"));
        list.add(new Destination("Japan", "Kyoto"));
        list.add(new Destination("Japan", "Osaka"));
        // add more
        return list;
    }
}

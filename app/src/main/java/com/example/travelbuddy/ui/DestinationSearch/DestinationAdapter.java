package com.example.travelbuddy.ui.DestinationSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;
import com.example.travelbuddy.model.Destination;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.VH> {

    public interface OnDestinationClickListener {
        void onDestinationClick(Destination destination);
    }

    private final List<Destination> allItems;      // full list (mock data)
    private final List<Destination> visibleItems;  // filtered list
    private final OnDestinationClickListener listener;

    public DestinationAdapter(List<Destination> items,
                              OnDestinationClickListener listener) {
        this.allItems = new ArrayList<>(items);
        this.visibleItems = new ArrayList<>();   // start empty
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvCity, tvCountry;
        ImageView imgFlag;

        VH(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tv_city);
            tvCountry = itemView.findViewById(R.id.tv_country);
            imgFlag = itemView.findViewById(R.id.img_flag);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_destination, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Destination dest = visibleItems.get(position);

        holder.tvCity.setText(dest.city);
        holder.tvCountry.setText(dest.country);

        // TODO: set real flag later; for now leave placeholder

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDestinationClick(dest);
        });
    }

    @Override
    public int getItemCount() {
        return visibleItems.size();
    }

    // simple filter for search text
    public void filter(String query) {
        visibleItems.clear();

        if (query == null || query.trim().isEmpty()) {
            // user cleared input -> keep list empty
            notifyDataSetChanged();
            return;
        }

        String q = query.toLowerCase(Locale.getDefault());
        for (Destination d : allItems) {
            if (d.city.toLowerCase(Locale.getDefault()).startsWith(q)
                    || d.country.toLowerCase(Locale.getDefault()).startsWith(q)) {
                visibleItems.add(d);
            }
        }
        notifyDataSetChanged();
    }

}

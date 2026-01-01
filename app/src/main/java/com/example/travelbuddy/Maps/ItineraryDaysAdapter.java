package com.example.travelbuddy.Maps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;

import java.util.List;

public class ItineraryDaysAdapter extends RecyclerView.Adapter<ItineraryDaysAdapter.DayViewHolder> {

    private final List<ItineraryDayItem> items;

    public ItineraryDaysAdapter(List<ItineraryDayItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_itinerary_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        ItineraryDayItem item = items.get(position);
        holder.tvDayTitle.setText(item.title);
        holder.tvDaySubtitle.setText(item.subtitle);

        // divider removed → nothing else needed here
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayTitle;
        TextView tvDaySubtitle;

        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayTitle = itemView.findViewById(R.id.tv_day_title);
            tvDaySubtitle = itemView.findViewById(R.id.tv_day_subtitle);

        }
    }
}

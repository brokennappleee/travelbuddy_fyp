package com.example.travelbuddy.ui.ItinerarySectionFragments;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.travelbuddy.R;

import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayViewHolder> {

    private final List<DayItem> days;

    public DaysAdapter(List<DayItem> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayItem item = days.get(position);
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_day_title);
            subtitle = itemView.findViewById(R.id.tv_day_subtitle);
        }
    }
}
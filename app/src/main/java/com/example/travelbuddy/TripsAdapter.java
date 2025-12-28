package com.example.travelbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {

    // Fragment implements this to handle clicks
    public interface OnTripClickListener {
        void onTripClick(Trip trip);                  // card tap
        void onTripMoreClick(Trip trip, View anchor); // three dots tap
    }

    private final List<Trip> trips;
    private final OnTripClickListener listener;

    public TripsAdapter(List<Trip> trips, OnTripClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false); // item_trip uses view_trip_card.xml
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.bind(trip, listener);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;
        TextView tvTitle;
        TextView tvDate;
        ImageButton btnMore;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_trip_cover);
            tvTitle = itemView.findViewById(R.id.tv_trip_title);
            tvDate = itemView.findViewById(R.id.tv_trip_date);
            btnMore = itemView.findViewById(R.id.btn_more);
        }

        void bind(final Trip trip, final OnTripClickListener listener) {
            ivCover.setImageResource(trip.coverResId);
            tvTitle.setText(trip.title);
            tvDate.setText(trip.dateRange);

            // Whole card click
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTripClick(trip);
            });

            // Three-dots click
            btnMore.setOnClickListener(v -> {
                if (listener != null) listener.onTripMoreClick(trip, btnMore);
            });
        }
    }
}

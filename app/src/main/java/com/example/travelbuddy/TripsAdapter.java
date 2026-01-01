package com.example.travelbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripVH> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
        void onTripMoreClick(Trip trip, View anchor);
    }

    private final List<Trip> trips;
    private final OnTripClickListener listener;

    public TripsAdapter(List<Trip> trips, OnTripClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_trip_card, parent, false);
        return new TripVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripVH holder, int position) {
        Trip trip = trips.get(position);
        holder.title.setText(trip.title);       // or trip.getName() if you made a getter
        holder.dates.setText(trip.dateRange);   // use the field you already have

        holder.root.setOnClickListener(v -> listener.onTripClick(trip));
        holder.btnMore.setOnClickListener(v -> listener.onTripMoreClick(trip, holder.btnMore));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TripVH extends RecyclerView.ViewHolder {
        View root;
        TextView title, dates;
        ImageButton btnMore;

        TripVH(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.trip_card_root);
            title = itemView.findViewById(R.id.tv_trip_title);
            dates = itemView.findViewById(R.id.tv_trip_date);
            btnMore = itemView.findViewById(R.id.btn_more);
        }
    }
}

package com.example.travelbuddy;

public class Trip {
    public String id;
    public String title;
    public String location;
    public String dateRange;
    public int coverResId;

    public Trip(String id, String title, String location,
                String dateRange, int coverResId) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.dateRange = dateRange;
        this.coverResId = coverResId;
    }
}

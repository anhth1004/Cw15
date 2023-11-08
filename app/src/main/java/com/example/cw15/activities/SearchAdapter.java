package com.example.cw15.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw15.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private final Context context;
    Activity activity;
    ArrayList<String> hikeIds, hikeNames, hikeLocations, hikeDates, hikeParkingAvailable, hikeDescriptions;
    int position;

    public SearchAdapter(Context context,
                         SearchActivity activity,
                         ArrayList<String> hikeIds,
                         ArrayList<String> hikeNames,
                         ArrayList<String> hikeLocations,
                         ArrayList<String> hikeDates,
                         ArrayList<String> hikeParkingAvailable,
                         ArrayList<String> hikeDescriptions) {
        this.context = context;
        this.activity = activity;
        this.hikeIds = hikeIds;
        this.hikeNames = hikeNames;
        this.hikeLocations = hikeLocations;
        this.hikeDates = hikeDates;
        this.hikeParkingAvailable = hikeParkingAvailable;
        this.hikeDescriptions = hikeDescriptions;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.position = position;
        holder.hikeIdText.setText(String.valueOf(hikeIds.get(position)));
        holder.hikeNameText.setText(String.valueOf(hikeNames.get(position)));
        holder.hikeLocationText.setText(String.valueOf(hikeLocations.get(position)));
        holder.hikeDateText.setText(String.valueOf(hikeDates.get(position)));

        String parkingAvailable = "Parking Available: " + (String.valueOf(hikeParkingAvailable.get(position)).equals("Yes") ? "Yes" : "No");
        holder.hikeParkingAvailableText.setText(parkingAvailable);
    }

    @Override
    public int getItemCount() {
        return hikeIds.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hikeIdText, hikeNameText, hikeLocationText, hikeDateText, hikeParkingAvailableText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeIdText = itemView.findViewById(R.id.hike_id_text);
            hikeNameText = itemView.findViewById(R.id.hike_name_text);
            hikeLocationText = itemView.findViewById(R.id.hike_location_text);
            hikeDateText = itemView.findViewById(R.id.hike_date_text);
            hikeParkingAvailableText = itemView.findViewById(R.id.hike_parking_text);
        }
    }
}

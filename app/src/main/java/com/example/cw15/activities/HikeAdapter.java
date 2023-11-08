package com.example.cw15.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw15.R;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.MyViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<String> hikeId;
    private final ArrayList<String> hikeName;
    private final ArrayList<String> hikeLocation;
    private final ArrayList<String> hikeDate;
    private final ArrayList<String> hikeParkingAvailable;
    private final ArrayList<String> hikeLength;
    private final ArrayList<String> hikeDifficulty;
    private final ArrayList<String> hikeDesc;
    int position;

    public HikeAdapter(Context context, Activity activity, ArrayList<String> hikeId,
                       ArrayList<String> hikeName, ArrayList<String> hikeLocation,
                       ArrayList<String> hikeDate, ArrayList<String> hikeParkingAvailable,
                       ArrayList<String> hikeLength, ArrayList<String> hikeDifficulty,
                       ArrayList<String> hikeDesc) {
        this.context = context;
        this.activity = activity;
        this.hikeId = hikeId;
        this.hikeName = hikeName;
        this.hikeLocation = hikeLocation;
        this.hikeDate = hikeDate;
        this.hikeParkingAvailable = hikeParkingAvailable;
        this.hikeLength = hikeLength;
        this.hikeDifficulty = hikeDifficulty;
        this.hikeDesc = hikeDesc;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater .from(context);
        View view = inflater.inflate(R.layout.item_hike, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.position = position;
        holder.hikeIdText.setText(String.valueOf(hikeId.get(position)));
        holder.hikeNameText.setText(String.valueOf(hikeName.get(position)));
        holder.hikeLocationText.setText(String.valueOf(hikeLocation.get(position)));
        holder.hikeDateText.setText(String.valueOf(hikeDate.get(position)));
        holder.hikeParkingAvailableText.setText(String.valueOf(hikeParkingAvailable.get(position)));
        holder.hikeLengthText.setText(String.valueOf(hikeLength.get(position)));
        holder.hikeDifficultyText.setText(String.valueOf(hikeDifficulty.get(position)));

        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewHikeActivity.class);

            intent.putExtra("id", String.valueOf(hikeId.get(position)));
            intent.putExtra("name", String.valueOf(hikeName.get(position)));
            intent.putExtra("location", String.valueOf(hikeLocation.get(position)));
            intent.putExtra("date", String.valueOf(hikeDate.get(position)));
            intent.putExtra("parking_available", String.valueOf(hikeParkingAvailable.get(position)));
            intent.putExtra("length", Double.parseDouble(hikeLength.get(position)));
            intent.putExtra("difficulty", String.valueOf(hikeDifficulty.get(position)));
            intent.putExtra("desc", String.valueOf(hikeDesc.get(position)));

            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return hikeId.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hikeIdText, hikeNameText, hikeLocationText, hikeDateText, hikeParkingAvailableText, hikeLengthText, hikeDifficultyText;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeIdText = itemView.findViewById(R.id.hike_id_text);
            hikeNameText = itemView.findViewById(R.id.hike_name_text);
            hikeLocationText = itemView.findViewById(R.id.hike_location_text);
            hikeDateText = itemView.findViewById(R.id.hike_date_text);
            hikeParkingAvailableText = itemView.findViewById(R.id.park_available_text);
            hikeLengthText = itemView.findViewById(R.id.length_text);
            hikeDifficultyText = itemView.findViewById(R.id.difficult_level_text);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}

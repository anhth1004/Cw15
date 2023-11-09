package com.example.cw15.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cw15.R;

import java.util.ArrayList;

public class ObservationsAdapter extends RecyclerView.Adapter<ObservationsAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<String> observation_id;
    private final ArrayList<String> observation;
    private final ArrayList<String> date_of_observation;
    private final ArrayList<String> comment;
    private final OnItemClickListener listener;
    int EDIT_OBSERVATION_REQUEST = 5;

    public ObservationsAdapter(Context context, ArrayList<String> observation_id, ArrayList<String> observation, ArrayList<String> date_of_observation, ArrayList<String> comment, OnItemClickListener listener) {
        this.context = context;
        this.observation_id = observation_id;
        this.observation = observation;
        this.date_of_observation = date_of_observation;
        this.comment = comment;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_observation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(observation.get(position), date_of_observation.get(position), comment.get(position), position);
    }

    @Override
    public int getItemCount() {
        return observation.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView observation_text;
        private final TextView date_of_observation_text;
        private final TextView comments_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            observation_text = itemView.findViewById(R.id.textViewObservation);
            date_of_observation_text = itemView.findViewById(R.id.textViewDateOfObservation);
            comments_text = itemView.findViewById(R.id.textViewComments);

            // Xử lý sự kiện khi người dùng nhấn nút "Delete"
            itemView.findViewById(R.id.buttonDeleteObservation).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(position);
                }
            });

            // Xử lý sự kiện khi người dùng nhấn nút "Edit"
            itemView.findViewById(R.id.buttonEditObservation).setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String observationIdKey = observation_id.get(position);
                    String observationDataKey = observation.get(position);
                    String dateOfObservationKey = date_of_observation.get(position);
                    String observationCommentKey = comment.get(position);

                    Intent intent = new Intent(context, UpdateObservationActivity.class);
                    intent.putExtra("observationIdKey", observationIdKey);
                    intent.putExtra("observationDataKey", observationDataKey);
                    intent.putExtra("dateOfObservationKey", dateOfObservationKey);
                    intent.putExtra("observationCommentKey", observationCommentKey);

                    ((Activity) context).startActivityForResult(intent, EDIT_OBSERVATION_REQUEST);
                }
            });
        }

        public void bind(String observation, String date, String comment, int position) {
            observation_text.setText(observation);
            date_of_observation_text.setText(date);
            comments_text.setText(comment);
        }
    }

    // Interface để xử lý sự kiện khi người dùng nhấn nút "Delete", "Edit", hoặc "View"
    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onViewClick(int position);
    }
}

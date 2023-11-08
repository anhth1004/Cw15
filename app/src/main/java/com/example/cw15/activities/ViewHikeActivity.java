package com.example.cw15.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw15.R;
import com.example.cw15.database.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ViewHikeActivity extends AppCompatActivity {

    private TextView textViewHikeName, textViewLocation, textViewDate, textViewParkingAvailable,
            textViewHikeLength, textViewDifficulty, textViewDescription;
    private RecyclerView recyclerViewObservations;
    private Button buttonAddObservation;
    private MyDatabaseHelper databaseHelper;
    private List<String> observationList;
    private ObservationsAdapter observationsAdapter;
    private String hikeId, hikeName;
    ArrayList<String> observation_id, observation, date_of_observation, comment;
    private static final int UPDATE_OBSERVATION_REQUEST = 1;


    private List<String> hike_id, name, location, date, parkingAvailable, desc;
    AlertDialog.Builder builder;
    Spinner difficultyLevelSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hike_detail);

        // Initialize views
        textViewHikeName = findViewById(R.id.textViewHikeName);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewDate = findViewById(R.id.textViewDate);
        textViewParkingAvailable = findViewById(R.id.textViewParkingAvailable);
        textViewHikeLength = findViewById(R.id.textViewHikeLength);
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        textViewDescription = findViewById(R.id.textViewDescription);

        recyclerViewObservations = findViewById(R.id.recyclerViewObservations);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                // Trang chủ đã được chọn, không cần thực hiện gì cả
                Intent addHikeIntent = new Intent(ViewHikeActivity.this, HomeActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_add_hike) {
                // Chuyển đến màn hình thêm chuyến đi (AddActivity)
                Intent addHikeIntent = new Intent(ViewHikeActivity.this, AddActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_search_hike) {
                // Chuyển đến màn hình tìm kiếm chuyến đi (SearchActivity)
                Intent searchHikeIntent = new Intent(ViewHikeActivity.this, SearchActivity.class);
                startActivity(searchHikeIntent);
            }
            return true;
        });
        Button buttonEditHike = findViewById(R.id.buttonEditHike);
        buttonEditHike.setOnClickListener(view -> {
            Intent observationIntent = new Intent(ViewHikeActivity.this, UpdateActivity.class);
            observationIntent.putExtra("id", hikeId);

            String nameString = textViewHikeName.getText().toString();
            String[] nameParts = nameString.split(":");
            String name = nameParts[1].trim();
            observationIntent.putExtra("name", name);

            String locationString = textViewLocation.getText().toString();
            String[] locationParts = locationString.split(":");
            String location = locationParts[1].trim();
            observationIntent.putExtra("location", location);

            String dateString = textViewDate.getText().toString();
            String[] dateParts = dateString.split(":");
            String date = dateParts[1].trim();
            observationIntent.putExtra("date", date);

            String parkingAvailableString = textViewParkingAvailable.getText().toString();
            String[] parkingAvailableParts = parkingAvailableString.split(":");
            String parkingAvailable = parkingAvailableParts[1].trim();
            observationIntent.putExtra("parking_available", parkingAvailable);

            String lengthString = textViewHikeLength.getText().toString();
            String[] lengthParts = lengthString.split(":");
            double length = Double.parseDouble(lengthParts[1].trim());
            observationIntent.putExtra("length", length);

            String difficultyString = textViewDifficulty.getText().toString();
            String[] difficultyParts = difficultyString.split(":");
            String difficulty = difficultyParts[1].trim();
            observationIntent.putExtra("difficulty", difficulty);

            String descString = textViewDescription.getText().toString();
            String[] descParts = descString.split(":");
            String desc = descParts[1].trim();
            observationIntent.putExtra("desc", desc);

            startActivityForResult(observationIntent, 2);
        });
        buttonAddObservation = findViewById(R.id.buttonAddObservation);


        // Initialize database helper
        databaseHelper = new MyDatabaseHelper(this);

        // Get hike ID from intent
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            hikeId = intent.getStringExtra("id");
            loadHikeDetails(hikeId);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

        // Set up RecyclerView and Adapter for observations
        databaseHelper = new MyDatabaseHelper(ViewHikeActivity.this);
        observation_id = new ArrayList<>();
        hike_id = new ArrayList<>();
        observation = new ArrayList<>();
        date_of_observation = new ArrayList<>();
        comment = new ArrayList<>();


        storeDataInArrays(hikeId);

        observationsAdapter = new ObservationsAdapter(ViewHikeActivity.this,
                observation_id,
                observation,
                date_of_observation,
                comment,
                new ObservationsAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        // Xử lý sự kiện khi người dùng nhấn "Delete" ở vị trí 'position'
                        String observationId = observation_id.get(position);
                        deleteObservation(observationId);
                    }

                    @Override
                    public void onEditClick(int position) {
                        // Xử lý sự kiện khi người dùng nhấn "Edit" ở vị trí 'position'
                        String observationId = observation_id.get(position);
                        editObservation(observationId);
                    }
                    @Override
                    public void onViewClick(int position) {
                        // Xử lý sự kiện khi người dùng nhấn "View" ở vị trí 'position'
                    }
                });

        recyclerViewObservations.setAdapter(observationsAdapter);
        recyclerViewObservations.setLayoutManager(new LinearLayoutManager(ViewHikeActivity.this));

        // Set a click listener for the "Add Observation" button
        buttonAddObservation.setOnClickListener(view -> {
            Intent observationIntent = new Intent(ViewHikeActivity.this, AddObservationActivity.class);
            observationIntent.putExtra("hike_id", hikeId);
            startActivityForResult(observationIntent, 1);
        });

    }
    // Xử lý chỉnh sửa quan sát
    private void editObservation(String observationId) {
        // Chuyển đến màn hình chỉnh sửa quan sát (UpdateObservationActivity)
        Intent editObservationIntent = new Intent(ViewHikeActivity.this, UpdateObservationActivity.class);
        editObservationIntent.putExtra("observation_id", observationId);
        startActivityForResult(editObservationIntent, 3);
    }
    public void deleteObservation(String observationId) {
        // Convert observationId to an integer (if it's a valid integer)
        try {
            int id = Integer.parseInt(observationId);
            databaseHelper.deleteObservation(id);

            // Cập nhật lại danh sách quan sát
            int position = observation_id.indexOf(observationId);
            if (position != -1) {
                observation_id.remove(position);
                observation.remove(position);
                date_of_observation.remove(position);
                comment.remove(position);
                observationsAdapter.notifyDataSetChanged();
            }
        } catch (NumberFormatException e) {
            // Handle the case where observationId is not a valid integer
            // You can show an error message or handle it as needed
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onViewClick(int position);
    }


    // Xử lý xem quan sát
//    private void viewObservation(String observationId) {
//        // Chuyển đến màn hình xem chi tiết quan sát
//        Intent viewObservationIntent = new Intent(ViewHikeActivity.this, ViewObservationActivity.class);
//        viewObservationIntent.putExtra("observation_id", observationId);
//        startActivity(viewObservationIntent);
//    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_button) {
            deleteHike();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Load hike details from the database and update the UI
    private void loadHikeDetails(String hikeId) {
        Cursor cursor = databaseHelper.readAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(databaseHelper.getColumnId()));
                if (id.equals(hikeId)) {
                    String name = cursor.getString(1);
                    String location = cursor.getString(2);
                    String date = cursor.getString(3);
                    String parkingAvailable = cursor.getString(4);
                    String hikeLength = cursor.getString(6);
                    String difficulty = cursor.getString(7);; // Replace with the actual difficulty level
                    String description = cursor.getString(5);

                    textViewHikeName.setText("Hike Name: " + name);
                    textViewLocation.setText("Location: " + location);
                    textViewDate.setText("Date: " + date);
                    textViewParkingAvailable.setText("Parking Available: " + parkingAvailable);
                    textViewHikeLength.setText("Length of the Hike: " + hikeLength);
                    textViewDifficulty.setText("Difficulty Level: " + difficulty);
                    textViewDescription.setText("Description: " + description);

                    break;
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    // Load observations for the specified hike from the database and update the RecyclerView
    void storeDataInArrays(String hikeId) {
        Cursor cursor = databaseHelper.readAllObservations(hikeId);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                observation_id.add(cursor.getString(0));
                hike_id.add(cursor.getString(1));
                observation.add(cursor.getString(2));
                date_of_observation.add(cursor.getString(3));
                comment.add(cursor.getString(4));
            }
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteHike() {
        builder.setTitle("Delete " + hikeName + "?");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper databaseHelper = new MyDatabaseHelper(ViewHikeActivity.this);
            databaseHelper.deleteOneRow(hikeId);
            finish();
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });

        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Đã thêm quan sát thành công, cập nhật lại dữ liệu chuyến đi và danh sách quan sát
                loadHikeDetails(hikeId);
                updateRecyclerViewData();
            }
        }
    }

    // Cập nhật danh sách quan sát
    public void updateRecyclerViewData() {
        observation_id.clear();
        hike_id.clear();
        observation.clear();
        date_of_observation.clear();
        comment.clear();
        storeDataInArrays(hikeId);
        observationsAdapter.notifyDataSetChanged();
    }

}
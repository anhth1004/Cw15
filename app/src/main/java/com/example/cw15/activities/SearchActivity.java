package com.example.cw15.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw15.R;
import com.example.cw15.database.MyDatabaseHelper;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Button searchButton;
    MyDatabaseHelper db = new MyDatabaseHelper(SearchActivity.this);
    SearchAdapter searchHikeAdapter;
    RecyclerView searchRecyclerView;

    EditText searchInput;
    ArrayList<String> hikeIds, hikeNames, hikeLocations, hikeDates, hikeParkingAvailable, hikeDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);

        searchButton = findViewById(R.id.search_trip_button);
        searchButton.setOnClickListener(view -> {

            db = new MyDatabaseHelper(SearchActivity.this);
            hikeIds = new ArrayList<>();
            hikeNames = new ArrayList<>();
            hikeLocations = new ArrayList<>();
            hikeDates = new ArrayList<>();
            hikeParkingAvailable = new ArrayList<>();
            hikeDescriptions = new ArrayList<>();

            searchInput = findViewById(R.id.search_input);
            searchHikes(searchInput.getText().toString());

        });
    }

    void searchHikes(String hikeName) {
        Cursor cursor = db.searchHike(hikeName);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                hikeIds.add(cursor.getString(0));
                hikeNames.add(cursor.getString(1));
                hikeLocations.add(cursor.getString(2));
                hikeDates.add(cursor.getString(3));
                hikeParkingAvailable.add(cursor.getString(4));
                hikeDescriptions.add(cursor.getString(5));
            }

            searchHikeAdapter = new SearchAdapter(
                    SearchActivity.this,
                    this,
                    hikeIds,
                    hikeNames,
                    hikeLocations,
                    hikeDates,
                    hikeParkingAvailable,
                    hikeDescriptions);

            searchRecyclerView.setAdapter(searchHikeAdapter);
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
            recreate();
        }
    }
}

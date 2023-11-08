package com.example.cw15.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cw15.R;
import com.example.cw15.database.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    String hikeId;
    EditText observation_input;
    EditText comments_input;
    Button add_observation_button;
    TextView textViewSelectDateOfObservation;
    private Calendar selectedDate = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        observation_input = findViewById(R.id.editTextObservation);
        textViewSelectDateOfObservation = findViewById(R.id.textViewSelectDateOfObservation);
        comments_input = findViewById(R.id.editTextComments);
        add_observation_button = findViewById(R.id.buttonAddObservation);
        textViewSelectDateOfObservation.setOnClickListener(view -> showDatePicker());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                // Trang chủ đã được chọn, không cần thực hiện gì cả
                Intent addHikeIntent = new Intent(AddObservationActivity.this, HomeActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_add_hike) {
                // Chuyển đến màn hình thêm chuyến đi (AddActivity)
                Intent addHikeIntent = new Intent(AddObservationActivity.this, AddActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_search_hike) {
                // Chuyển đến màn hình tìm kiếm chuyến đi (SearchActivity)
                Intent searchHikeIntent = new Intent(AddObservationActivity.this, SearchActivity.class);
                startActivity(searchHikeIntent);
            }
            return true;
        });

        add_observation_button.setOnClickListener(view -> {
            if (observation_input.getText().toString().isEmpty() ||
                    textViewSelectDateOfObservation.getText().toString().isEmpty() ||
                    comments_input.getText().toString().isEmpty()) {
                Toast.makeText(AddObservationActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddObservationActivity.this, observation_input.getText().toString() +
                        textViewSelectDateOfObservation.getText().toString() +
                        comments_input.getText().toString(), Toast.LENGTH_SHORT).show();

                MyDatabaseHelper DB = new MyDatabaseHelper(AddObservationActivity.this);
                DB.addObservation(hikeId = getIntent().getStringExtra("hike_id"),
                        observation_input.getText().toString().trim(),
                        textViewSelectDateOfObservation.getText().toString().trim(),
                        comments_input.getText().toString().trim());
                Intent intent = new Intent(AddObservationActivity.this, ViewHikeActivity.class);
                intent.putExtra("hike_id", hikeId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(selectedDate.getTime());
            textViewSelectDateOfObservation.setText(formattedDate);
        };

        new DatePickerDialog(this, dateSetListener, selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void updateObservationDate(LocalDate date) {
        TextView dateText = findViewById(R.id.textViewSelectDateOfObservation);
        dateText.setText(date.toString());
    }
}

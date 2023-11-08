package com.example.cw15.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.cw15.R;
import com.example.cw15.database.MyDatabaseHelper;
import com.example.cw15.fragments.DatePickerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    EditText hikeNameInput, locationInput, descriptionInput, hikeLengthInput;
    Button addHikeButton;
    RadioGroup parkingRadioGroup;
    Spinner difficultySpinner;
    TextView textViewSelectDate;
    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        hikeNameInput = findViewById(R.id.editTextHikeName);
        locationInput = findViewById(R.id.editTextLocation);
        textViewSelectDate = findViewById(R.id.textViewSelectDate);
        descriptionInput = findViewById(R.id.editTextDescription);
        hikeLengthInput = findViewById(R.id.editTextHikeLength);
        addHikeButton = findViewById(R.id.buttonAddHike);
        parkingRadioGroup = findViewById(R.id.radio_group_parking);
        difficultySpinner = findViewById(R.id.spinnerDifficulty);
        textViewSelectDate.setOnClickListener(view -> showDatePicker());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                // Trang chủ đã được chọn, không cần thực hiện gì cả
                Intent addHikeIntent = new Intent(AddActivity.this, HomeActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_add_hike) {
                // Chuyển đến màn hình thêm chuyến đi (AddActivity)
                Intent addHikeIntent = new Intent(AddActivity.this, AddActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_search_hike) {
                // Chuyển đến màn hình tìm kiếm chuyến đi (SearchActivity)
                Intent searchHikeIntent = new Intent(AddActivity.this, SearchActivity.class);
                startActivity(searchHikeIntent);
            }
            return true;
        });

        addHikeButton.setOnClickListener(view -> {
            int radioId = parkingRadioGroup.getCheckedRadioButtonId();
            RadioButton rb = findViewById(radioId);
            if (hikeNameInput.getText().toString().trim().isEmpty() ||
                    locationInput.getText().toString().trim().isEmpty() ||
                    textViewSelectDate.getText().toString().trim().isEmpty() ||
                    rb.getText().toString().trim().isEmpty() ||
                    descriptionInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(AddActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                String difficultyLevel = difficultySpinner.getSelectedItem().toString();
                Toast.makeText(AddActivity.this, hikeNameInput.getText().toString() +
                        locationInput.getText().toString() +
                        textViewSelectDate.getText().toString().trim() +
                        rb.getText().toString() +
                        descriptionInput.getText().toString() +
                        hikeLengthInput.getText().toString() +
                        difficultyLevel, Toast.LENGTH_SHORT).show();
                MyDatabaseHelper DB = new MyDatabaseHelper(AddActivity.this);
                DB.addHike(hikeNameInput.getText().toString().trim(),
                        locationInput.getText().toString().trim(),
                        textViewSelectDate.getText().toString().trim(),
                        rb.getText().toString().trim(),
                        descriptionInput.getText().toString().trim(),
                        Double.parseDouble(hikeLengthInput.getText().toString().trim()),
                        difficultyLevel);
                // Cập nhật danh sách chuyến đi trên màn hình HomeActivity
//                Intent updateIntent = new Intent();
//                setResult(RESULT_OK, updateIntent);

                // Chuyển về màn hình trước
                finish();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date1");
    }
    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(selectedDate.getTime());
            textViewSelectDate.setText(formattedDate);
        };

        new DatePickerDialog(this, dateSetListener, selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void updateDate(LocalDate date) {
        TextView dateText = findViewById(R.id.textViewSelectDate);
        dateText.setText(date.toString());
    }
}

package com.example.cw15.activities;

import android.app.AlertDialog;
import android.widget.ArrayAdapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cw15.R;
import com.example.cw15.database.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    EditText name_input, location_input, description_input, parking_available_input, length_input;
    Button update_button, see_expense_button;
    RadioGroup radioGroup;
    RadioButton radioButtonYes, radioButtonNo;
    TextView textViewSelectDate;
    Spinner difficultyLevelSpinner;

    String id, name, location, date, parkingAvailable, difficultyLevel, desc;
    Double length;
    AlertDialog.Builder builder;
    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);

        name_input = findViewById(R.id.editTextHikeName);
        location_input = findViewById(R.id.editTextLocation);
        description_input = findViewById(R.id.editTextDescription);
        length_input = findViewById(R.id.editTextHikeLength);
        radioGroup = findViewById(R.id.radio_group_parking);
        radioButtonYes = findViewById(R.id.radio_parking_yes);
        radioButtonNo = findViewById(R.id.radio_parking_no);
        update_button = findViewById(R.id.buttonSaveChanges);
        textViewSelectDate = findViewById(R.id.textViewSelectDate);
        difficultyLevelSpinner = findViewById(R.id.spinnerDifficulty);
        textViewSelectDate.setOnClickListener(view -> showDatePicker());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                // Trang chủ đã được chọn, không cần thực hiện gì cả
                Intent addHikeIntent = new Intent(UpdateActivity.this, HomeActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_add_hike) {
                // Chuyển đến màn hình thêm chuyến đi (AddActivity)
                Intent addHikeIntent = new Intent(UpdateActivity.this, AddActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_search_hike) {
                // Chuyển đến màn hình tìm kiếm chuyến đi (SearchActivity)
                Intent searchHikeIntent = new Intent(UpdateActivity.this, SearchActivity.class);
                startActivity(searchHikeIntent);
            }
            return true;
        });

        getAndSetIntentData();

        update_button.setOnClickListener(view -> {
            MyDatabaseHelper db = new MyDatabaseHelper(UpdateActivity.this);
            name = name_input.getText().toString().trim();
            location = location_input.getText().toString().trim();
            date = textViewSelectDate.getText().toString().trim();
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId);
            parkingAvailable = selectedRadioButton.getText().toString().trim();
            length = Double.parseDouble(length_input.getText().toString());
            difficultyLevel = difficultyLevelSpinner.getSelectedItem().toString();
            desc = description_input.getText().toString().trim();

            db.updateHike(id, name, location, date, parkingAvailable, length, difficultyLevel, desc);

            // Set the result here to indicate that the update was successful
            setResult(RESULT_OK);

            finish();
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("location") && getIntent().hasExtra("date") && getIntent().hasExtra("parking_available") && getIntent().hasExtra("length") && getIntent().hasExtra("difficulty") && getIntent().hasExtra("desc")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            location = getIntent().getStringExtra("location");
            date = getIntent().getStringExtra("date");
            parkingAvailable = getIntent().getStringExtra("parking_available");
            length = getIntent().getDoubleExtra("length", 1.0);
            difficultyLevel = getIntent().getStringExtra("difficulty");
            desc = getIntent().getStringExtra("desc");

            //Setting Intent Data
            name_input.setText(name);
            location_input.setText(location);
            textViewSelectDate.setText(date);
            if (parkingAvailable.equals("Yes")) {
                radioButtonYes.setChecked(true);
            } else {
                radioButtonNo.setChecked(true);
            }
            length_input.setText(String.valueOf(length));
            description_input.setText(desc);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulty_levels, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            difficultyLevelSpinner.setAdapter(adapter);
            if (difficultyLevel != null) {
                int spinnerPosition = adapter.getPosition(difficultyLevel);
                difficultyLevelSpinner.setSelection(spinnerPosition);
            }
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
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

}

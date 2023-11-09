package com.example.cw15.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cw15.R;
import com.example.cw15.database.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateObservationActivity extends AppCompatActivity {

    String observationId;
    EditText observationInput;
    EditText commentsInput;
    Button saveObservationButton;
    TextView selectDateOfObservation;
    private Calendar selectedDate = Calendar.getInstance();
    private static final int UPDATE_OBSERVATION_REQUEST = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);

        observationInput = findViewById(R.id.editTextObservation);
        selectDateOfObservation = findViewById(R.id.textViewSelectDateOfObservation);
        commentsInput = findViewById(R.id.editTextComments);
        saveObservationButton = findViewById(R.id.buttonSaveObservation);

        selectDateOfObservation.setOnClickListener(view -> showDatePicker());

        Intent intent = getIntent();
        if (intent.hasExtra("observationIdKey")) {
            observationId = intent.getStringExtra("observationIdKey");
            String observation = intent.getStringExtra("observationDataKey");
            String dateOfObservation = intent.getStringExtra("dateOfObservationKey");
            String comments = intent.getStringExtra("observationCommentKey");

            observationInput.setText(observation);
            selectDateOfObservation.setText(dateOfObservation);
            commentsInput.setText(comments);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

        // In UpdateObservationActivity
        saveObservationButton.setOnClickListener(view -> {
            if (observationInput.getText().toString().isEmpty() ||
                    selectDateOfObservation.getText().toString().isEmpty() ||
                    commentsInput.getText().toString().isEmpty()) {
                Toast.makeText(UpdateObservationActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Cập nhật quan sát trong cơ sở dữ liệu ở đây
                updateObservationInDatabase();

                // Trả lại kết quả cho màn hình trước đó (ViewHikeActivity)
                Intent resultIntent = new Intent();
                resultIntent.putExtra("observationIdKey", observationId);
                resultIntent.putExtra("observationDataKey", observationInput.getText().toString());
                resultIntent.putExtra("dateOfObservationKey", selectDateOfObservation.getText().toString());
                resultIntent.putExtra("observationCommentKey", commentsInput.getText().toString());
                setResult(RESULT_OK, resultIntent);

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
            selectDateOfObservation.setText(formattedDate);
        };

        new DatePickerDialog(this, dateSetListener, selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Thực hiện cập nhật quan sát trong cơ sở dữ liệu
    private void updateObservationInDatabase() {
        String updatedObservation = observationInput.getText().toString();
        String updatedDateOfObservation = selectDateOfObservation.getText().toString();
        String updatedComments = commentsInput.getText().toString();

        // Gọi phương thức để cập nhật quan sát trong cơ sở dữ liệu
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        dbHelper.updateObservation(observationId, updatedObservation, updatedDateOfObservation, updatedComments);
    }
}

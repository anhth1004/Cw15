package com.example.cw15.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw15.R;
import com.example.cw15.database.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper db;
    ArrayList<String> hikeId, hikeName, hikeLocation, hikeDate, hikeParkingAvailable, hikeLength, hikeDifficulty, hikeDesc;
    HikeAdapter hikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerViewHikes);
//        add_button = findViewById(R.id.add_button);
//
//        add_button.setOnClickListener(view -> {
//            Intent intent = new Intent(HomeActivity.this, AddActivity.class);
//            startActivityForResult(intent, 1);
//        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                // Trang chủ đã được chọn, không cần thực hiện gì cả
            } else if (id == R.id.action_add_hike) {
                // Chuyển đến màn hình thêm chuyến đi (AddActivity)
                Intent addHikeIntent = new Intent(HomeActivity.this, AddActivity.class);
                startActivity(addHikeIntent);
            } else if (id == R.id.action_search_hike) {
                // Chuyển đến màn hình tìm kiếm chuyến đi (SearchActivity)
                Intent searchHikeIntent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(searchHikeIntent);
            }
            return true;
        });

        db = new MyDatabaseHelper(HomeActivity.this);
        hikeId = new ArrayList<>();
        hikeName = new ArrayList<>();
        hikeLocation = new ArrayList<>();
        hikeDate = new ArrayList<>();
        hikeParkingAvailable = new ArrayList<>();
        hikeLength = new ArrayList<>();
        hikeDifficulty = new ArrayList<>();
        hikeDesc = new ArrayList<>();

        storeDataInArrays();

        hikeAdapter = new HikeAdapter(HomeActivity.this, HomeActivity.this, hikeId, hikeName, hikeLocation, hikeDate, hikeParkingAvailable, hikeLength, hikeDifficulty, hikeDesc);

        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_button) {
            deleteAll();
        }
        if (item.getItemId() == R.id.search_button) {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            recreate();
//        }
//    }

    void storeDataInArrays() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                hikeId.add(cursor.getString(0));
                hikeName.add(cursor.getString(1));
                hikeLocation.add(cursor.getString(2));
                hikeDate.add(cursor.getString(3));
                hikeParkingAvailable.add(cursor.getString(4));
                hikeLength.add(cursor.getString(6));
                hikeDifficulty.add(cursor.getString(5));
                hikeDesc.add(cursor.getString(7));
            }
        } else {
            Toast.makeText(this, "Không có dữ liệu.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa tất cả?");
        builder.setMessage("Bạn có chắc chắn không?");

        builder.setPositiveButton("Có", (dialogInterface, i) -> {
            MyDatabaseHelper db = new MyDatabaseHelper(HomeActivity.this);
            db.deleteAllData();
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Không", (dialogInterface, i) -> {
        });

        builder.create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Có thể cập nhật lại danh sách chuyến đi ở đây, ví dụ, gọi phương thức updateRecyclerViewData():
                updateRecyclerViewData();
            }
        }
    }
    public void updateRecyclerViewData() {
        // Đối với Adapter của bạn, cập nhật danh sách dữ liệu (data) trong Adapter
        // Ví dụ: adapter.updateData(newData); // newData là danh sách mới
        // Sau đó gọi notifyDataSetChanged() để thông báo cập nhật giao diện
        hikeAdapter.notifyDataSetChanged();
    }
}

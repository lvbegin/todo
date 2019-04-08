package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class viewTask extends AppCompatActivity {
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Log.d("LOLO", "start view activity");
        Intent intent = getIntent();
        String title = intent.getStringExtra("task");
        titleView = findViewById(R.id.titleViewTask);
        titleView.setText(title);
    }
}

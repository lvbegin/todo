package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class viewTask extends AppCompatActivity {
    private TextView titleView;
    private TextView commentView;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Log.d("LOLO", "start view activity");
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String comment = intent.getStringExtra("comment");
        titleView = findViewById(R.id.titleViewTask);
        titleView.setText(title);
        commentView = findViewById(R.id.commentViewTask);
        commentView.setText(comment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_task_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_task_item:
                Log.d("LOLO", "Edit selected");
                Intent i = new Intent();
                i.putExtra("id", id);
                setResult(RESULT_OK, i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

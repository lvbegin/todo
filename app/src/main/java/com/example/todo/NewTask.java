package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewTask extends AppCompatActivity {
    private Button okButton;
    private Button cancelButton;
    private EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        title = findViewById(R.id.new_task_title);
        okButton = findViewById(R.id.done_new_task_button);
        cancelButton = findViewById(R.id.cancel_new_task_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = title.getText().toString();
                Intent i = new Intent();
                i.putExtra("title", t);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}

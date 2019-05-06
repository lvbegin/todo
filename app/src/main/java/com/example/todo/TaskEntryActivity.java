package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class TaskEntryActivity extends AppCompatActivity implements AddCommentListener {
    private Button okButton;
    private Button cancelButton;
    private EditText title;
    private long idTask;
    private Fragment commentFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        title = findViewById(R.id.new_task_title);
        okButton = findViewById(R.id.done_new_task_button);
        cancelButton = findViewById(R.id.cancel_new_task_button);

        Intent intent = getIntent();
        String intitialTitle = null;
        String initialComment = null;
        idTask = intent.getLongExtra("id", -1);
        intitialTitle = intent.getStringExtra("title");
        initialComment = intent.getStringExtra("comment");
        if (intitialTitle != null) {
            title.setText(intitialTitle);
        }

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
                if (t.length() == 0) {
                    TextView errorMessage = findViewById(R.id.no_title_error_message);
                    errorMessage.setVisibility(1);
                    Toast.makeText(TaskEntryActivity.this, "Some fields are mandatory", 3).show();
                    return ;
                }
                String comment;
                if (commentFragment != null)
                    comment = ((TextView)commentFragment.getView().findViewById(R.id.comment)).getText().toString();
                else
                    comment = "";
                Intent i = new Intent();
                i.putExtra("title", t);
                i.putExtra("comment", comment);
                i.putExtra("id", TaskEntryActivity.this.idTask);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        if (initialComment == null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment, AddCommentFragment.newInstance());
            transaction.commit();
        }
        else
            createCommandFragment(initialComment);
    }

    @Override
    public void OnAddCommentButtonClick()
    {
        if (commentFragment != null)
            return ;
        createCommandFragment("");
    }
    private void createCommandFragment(String comment) {
        commentFragment = commentBox.newInstance(comment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, commentFragment);
        transaction.commit();
    }
}

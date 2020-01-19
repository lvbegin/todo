package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class viewTask extends AppCompatActivity {
    private static final int VIEW_PICTURE_ACTIVITY = 1;

    private static final String TITLE_KEY = "title";
    private static final String COMMENT_KEY = "comment";
    private static final String ID_TASK_KEY = "id";
    private static final String CREATION_DATE_KEY = "creation_date";
    private static final String PICTURES_KEY = "pictures";
    private TextView titleView;
    private TextView commentView;
    private TextView creationDate;
    private long id;
    private String title;
    private String comment;
    private long date;
    private List<String> imagesUri;
    private RecyclerView recyclerView;
    private PersistentTaskList list;
    private PictureGalleryAdapter adapter;

    static public Intent prepareIntent(Context context, long taskId) {
        Intent intent = new Intent();
        intent.putExtra(ID_TASK_KEY, taskId);
        intent.setClass(context, TaskEntryActivity.class);
        return intent;
    }

    private void setUpViews() {
        titleView = findViewById(R.id.titleViewTask);
        commentView = findViewById(R.id.commentViewTask);
        creationDate = findViewById(R.id.CreationDateViewTask);
        recyclerView = findViewById(R.id.images_to_view);
    }

    private void getDataFromIntent(Intent intent) {
        id = intent.getLongExtra(ID_TASK_KEY, -1);
        TaskE task = list.getById(id);
        title = task.title;
        comment = task.comment;
        date = task.creationDate;
        imagesUri = list.getUriPictureArrayList(task);
    }

    private void setValuesInViews() {
        titleView.setText(title);
        commentView.setText(comment);
        creationDate.setText(ViewFormating.dateToString(date));
        creationDate.setText(ViewFormating.dateToString(date));


        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        adapter = new PictureGalleryAdapter(this, imagesUri);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Log.d("LOLO", "start view activity");

        list = new PersistentTaskList("tododb", getApplicationContext());

        Intent intent = getIntent();
        setUpViews();
        getDataFromIntent(intent);
        setValuesInViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_task_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent();
        switch(item.getItemId()) {
            case R.id.edit_task_item:
                Log.d("LOLO", "Edit selected");
                i.putExtra("id", id);
                setResult(1, i);
                finish();
                return true;
            case R.id.delete_task_item:
                Log.d("LOLO", "Delete selected");
                i.putExtra("id", id);
                setResult(2, i);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return ;
        if (PictureGalleryAdapter.REQUEST_CODE == requestCode) {
            int index = data.getExtras().getInt("picture_id");
            list.removePicture(id, index);
            adapter.remove(index);
            Log.d("TODO", "index to remove: " + index);
        }
    }
}

package com.example.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskEntryActivity extends AppCompatActivity {
    private static final int GALLERY_ACTIVITY = 1;
    private static final int CAMERA_ACTIVITY = 2;
    private static final String TITLE_KEY = "title";
    private static final String COMMENT_KEY = "comment";
    private static final String ID_TASK_KEY = "id";
    private static final String CREATION_DATE_KEY = "creation_date";
    private static final String PICTURES_KEY = "pictures";
    private static final String DONE_KEY = "done";
    private Button okButton;
    private Button cancelButton;
    private Button addPictureButton;
    private EditText titleView;
    private EditText commentView;
    private RecyclerView imagesView;
    private long idTask;
    private List<Bitmap> images;
    private ArrayList<String> imagesUri;
    private PictureGalleryAdapter imageAdapter;
    private PersistentTaskList db;
    private CheckBox checkBox;

    static public Intent prepareIntent(Context context, long taskId, String title, String comment, long creationDate, List<String> uris, boolean done) {
        Intent intent = new Intent();
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(COMMENT_KEY, comment);
        intent.putExtra(ID_TASK_KEY, taskId);
        intent.putExtra(CREATION_DATE_KEY, creationDate);
        intent.putExtra(DONE_KEY, done);
        intent.putStringArrayListExtra(PICTURES_KEY, new ArrayList<String>(uris));
        intent.setClass(context, TaskEntryActivity.class);
        return intent;
    }

    static public String titleResult(Intent intent) {
        return intent.getExtras().getString(TITLE_KEY);
    }

    static public String commentResult(Intent intent) {
        return intent.getExtras().getString(COMMENT_KEY);
    }

    static public long idTaskResult(Intent intent) {
        return intent.getExtras().getLong(ID_TASK_KEY);
    }

    static public List<String> listImageUri(Intent intent) {
        return intent.getExtras().getStringArrayList(PICTURES_KEY);
    }

    static public boolean doneResult(Intent intent) {
        return intent.getExtras().getBoolean(DONE_KEY);
    }

    private void setReferenceToViews() {
        titleView = findViewById(R.id.new_task_title);
        commentView = findViewById(R.id.comment);
        okButton = findViewById(R.id.done_new_task_button);
        cancelButton = findViewById(R.id.cancel_new_task_button);
        addPictureButton = findViewById(R.id.add_picture_button);
        imagesView = findViewById(R.id.images);
        checkBox = findViewById(R.id.CheckBoxEditTask);
    }

    private void setListeners() {
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
                String t = titleView.getText().toString();
                if (t.length() == 0) {
                    TextView errorMessage = findViewById(R.id.no_title_error_message);
                    errorMessage.setVisibility(1);
                    Toast.makeText(TaskEntryActivity.this, R.string.field_mandatory_message, 3).show();
                    return;
                }
                String comment = commentView.getText().toString();
                boolean done = checkBox.isChecked();
                Intent i = new Intent();
                i.putExtra(TITLE_KEY, t);
                i.putExtra(COMMENT_KEY, comment);
                i.putExtra(DONE_KEY, done);
                i.putExtra(ID_TASK_KEY, TaskEntryActivity.this.idTask);
                i.putStringArrayListExtra(PICTURES_KEY, imagesUri);
                Log.d("TODO", "title:" + t + ", comment: " + comment);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TODO", "onClick: ");
                new AlertDialog.Builder(TaskEntryActivity.this).setTitle(R.string.select_picture_title)
                        .setItems(R.array.picture_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("TODO", "onClick: " + which);
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, getResources().getText(R.string.choose_select_picture)), GALLERY_ACTIVITY);
                                        break;
                                    case 1:
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(Intent.createChooser(takePictureIntent, getResources().getText(R.string.choose_take_picture)), CAMERA_ACTIVITY);
                                        break;
                                    default:
                                        Log.d("TODO", "onClick: " + " unknown item selected");
                                }
                            }
                        })
                        .create().show();
            }
        });
    }

    private void setUpImageList() {
        if (null == imagesUri)
            imagesUri = new ArrayList<String>();

        imageAdapter = new PictureGalleryAdapter(this, imagesUri);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        imagesView.setLayoutManager(layoutManager);
        imagesView.setAdapter(imageAdapter);
    }

    private void initializeViewsFromIntent(Intent intent) {
        String intitialTitle = null;
        String initialComment = null;
        boolean initialDone = intent.getBooleanExtra(DONE_KEY, false);
        idTask = intent.getLongExtra(ID_TASK_KEY, -1);
        intitialTitle = intent.getStringExtra(TITLE_KEY);
        initialComment = intent.getStringExtra(COMMENT_KEY);
        if (intitialTitle != null) {
            titleView.setText(intitialTitle);
        }
        if (initialComment != null) {
            commentView.setText(initialComment);
        }
        checkBox.setChecked(initialDone);
        imagesUri = intent.getStringArrayListExtra(PICTURES_KEY);
        setUpImageList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        db = new PersistentTaskList(getResources().getString(R.string.db_name), getApplicationContext());
        setReferenceToViews();
        setListeners();
        initializeViewsFromIntent(getIntent());
    }

    private File createImageFile() throws IOException {
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode)
            return ;
        final Uri imageUri = getImageUriFromIntent(requestCode, data);
        if (null == imageUri) {
            displayErrorGettingImageMassage();
            return ;
        }

        Bitmap image = null;
        try {
            image =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            displayErrorGettingImageMassage();
            return ;
        }
        imageAdapter.add(imageUri.toString());
        if (-1 != idTask)
            db.addPicture(idTask, imageUri.toString());
    }

    private void displayErrorGettingImageMassage() {
        Toast.makeText(this, getString(R.string.problem_getting_image), Toast.LENGTH_SHORT).show();
    }

    private Uri getImageUriFromIntent(int requestCode, Intent data) {
        Uri imageUri = null;
        switch (requestCode) {
            case GALLERY_ACTIVITY:
                imageUri = data.getData();
                break;
            case CAMERA_ACTIVITY:
                File f = null;
                Bitmap image = (Bitmap) data.getExtras().get("data");
                try {
                    f  = createImageFile();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f));
                    imageUri = Uri.fromFile(f);
                } catch (IOException e) {
                    if (null != f)
                        f.delete();
                }
                break;
            default:
                Log.d("TODO", "unknonwn request code");
        }
        return imageUri;
    }
}
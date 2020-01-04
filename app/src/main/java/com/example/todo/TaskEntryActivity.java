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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.room.Room;

class CustomGalleryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Bitmap> images;

    public CustomGalleryAdapter(Context c, List<Bitmap> images) {
        context = c;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image, viewGroup, false);
                //(ImageView)viewGroup.findViewById(R.id.imageView);
        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(images.get(i), 240, 240, false));

        return new ViewHolder(layout) {
            @Override
            public String toString() {
                return "image";
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LinearLayout layout = (LinearLayout) viewHolder.itemView;
        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(images.get(i), 240, 240, false));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void add(Bitmap image)  {
        images.add(image);
        notifyDataSetChanged();
    }
}


public class TaskEntryActivity extends AppCompatActivity implements AddCommentListener {
    static final int GALLERY_ACTIVITY = 1;
    static final int CAMERA_ACTIVITY = 2;
    private Button okButton;
    private Button cancelButton;
    private Button addPictureButton;
    private EditText title;
    private RecyclerView imagesView;
    private long idTask;
    private Fragment commentFragment = null;
    private List<Bitmap> images;
    private ArrayList<String> imagesUri;
    private CustomGalleryAdapter imageAdapter;
    private TaskDB db;

    private void setReferenceToViews() {
        title = findViewById(R.id.new_task_title);
        okButton = findViewById(R.id.done_new_task_button);
        cancelButton = findViewById(R.id.cancel_new_task_button);
        addPictureButton = findViewById(R.id.add_picture_button);
        imagesView = findViewById(R.id.images);
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
                String t = title.getText().toString();
                if (t.length() == 0) {
                    TextView errorMessage = findViewById(R.id.no_title_error_message);
                    errorMessage.setVisibility(1);
                    Toast.makeText(TaskEntryActivity.this, "Some fields are mandatory", 3).show();
                    return;
                }
                String comment;
                if (commentFragment != null)
                    comment = ((TextView) commentFragment.getView().findViewById(R.id.comment)).getText().toString();
                else
                    comment = "";
                Intent i = new Intent();
                i.putExtra("title", t);
                i.putExtra("comment", comment);
                i.putExtra("id", TaskEntryActivity.this.idTask);
                i.putStringArrayListExtra("pictures", imagesUri);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TODO", "onClick: ");
                new AlertDialog.Builder(TaskEntryActivity.this).setTitle("Where the picture is from?")
                        .setItems(R.array.picture_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("TODO", "onClick: " + which);
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_ACTIVITY);
                                        break;
                                    case 1:
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, CAMERA_ACTIVITY);
                                        }

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
        images = new ArrayList<Bitmap>();
        if (null == imagesUri)
            imagesUri = new ArrayList<String>();

        List<Bitmap> images = new ArrayList<Bitmap>();
        for (String uri : imagesUri) {
            try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
                    images.add(bitmap);
                } catch (IOException e) {
                }
        }

        imageAdapter = new CustomGalleryAdapter(this, images);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        imagesView.setLayoutManager(layoutManager);
        imagesView.setAdapter(imageAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        setReferenceToViews();
        setListeners();

        db = Room.databaseBuilder(this, TaskDB.class, "tododb").allowMainThreadQueries().build();

        Intent intent = getIntent();
        String intitialTitle = null;
        String initialComment = null;
        idTask = intent.getLongExtra("id", -1);
        intitialTitle = intent.getStringExtra("title");
        initialComment = intent.getStringExtra("comment");
        if (intitialTitle != null) {
            title.setText(intitialTitle);
        }


        if (initialComment == null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment, AddCommentFragment.newInstance());
            transaction.commit();
        } else
            createCommandFragment(initialComment);

        imagesUri = intent.getStringArrayListExtra("pictures");
        setUpImageList();

    }

    @Override
    public void OnAddCommentButtonClick() {
        if (commentFragment != null)
            return;
        createCommandFragment("");
    }

    private void createCommandFragment(String comment) {
        commentFragment = commentBox.newInstance(comment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, commentFragment);
        transaction.commit();
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
        imageAdapter.add(image);
        imagesUri.add(imageUri.toString());
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
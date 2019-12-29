package com.example.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;


//class CustomGalleryAdapter extends BaseAdapter {
class CustomGalleryAdapter extends BaseAdapter {
    private Context context;
    private Bitmap[] images;

    public CustomGalleryAdapter(Context c, Bitmap[] images) {
        context = c;
        this.images = images;
    }

    // returns the number of images
    public int getCount() {
        return images.length;
    }

    // returns the ID of an item
    public Object getItem(int position) {
        return position;
    }

    // returns the ID of an item
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.image, parent, false);
        }
        ImageView view = convertView.findViewById(R.id.imageView);
        view.setImageBitmap(Bitmap.createScaledBitmap(images[position], 120, 120, false));
        return convertView;
    }
}



class CustomGalleryAdapter2 extends RecyclerView.Adapter {
    private Context context;
    private Bitmap[] images;

    public CustomGalleryAdapter2(Context c, Bitmap[] images) {
        context = c;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image, viewGroup, false);
                //(ImageView)viewGroup.findViewById(R.id.imageView);
        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(images[i], 240, 240, false));

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
        imageView.setImageBitmap(Bitmap.createScaledBitmap(images[i], 240, 240, false));
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}


public class TaskEntryActivity extends AppCompatActivity implements AddCommentListener {
    private Button okButton;
    private Button cancelButton;
    private Button addPictureButton;
    private EditText title;
    private RecyclerView images;
    private long idTask;
    private Fragment commentFragment = null;

    private void setReferenceToViews() {
        title = findViewById(R.id.new_task_title);
        okButton = findViewById(R.id.done_new_task_button);
        cancelButton = findViewById(R.id.cancel_new_task_button);
        addPictureButton = findViewById(R.id.add_picture_button);
        images = findViewById(R.id.images);
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
                setResult(RESULT_OK, i);
                finish();
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TODO", "onClick: ");
                String choices[] = new String[2];
                choices[0] = "Gallery";
                choices[1] = "Camera";
                new AlertDialog.Builder(TaskEntryActivity.this).setTitle("Where the picture is from?")
                        .setItems(choices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("TODO", "onClick: " + which);
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                                        break;
                                    case 1:
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, 2);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        setReferenceToViews();
        setListeners();

        Bitmap[] array = new Bitmap[2];
        array[0] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        array[1] = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        images.setLayoutManager(layoutManager);
        images.setAdapter(new CustomGalleryAdapter2(this, array));

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Log.d("TODO", "image selected");
        }
    }
}
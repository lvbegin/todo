package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//Duplicate
class CustomGalleryAdapter2 extends RecyclerView.Adapter {
private Context context;
private List<Bitmap> images;

public CustomGalleryAdapter2(Context c, List<Bitmap> images) {
        context = c;
        this.images = images;
        }

@NonNull
@Override
public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image, viewGroup, false);
        //(ImageView)viewGroup.findViewById(R.id.imageView);
        ImageView imageView = layout.findViewById(R.id.imageView);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(images.get(i), 240, 240, false));

        return new RecyclerView.ViewHolder(layout) {
@Override
public String toString() {
        return "image";
        }
        };
        }

@Override
public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
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


public class viewTask extends AppCompatActivity {
    private TextView titleView;
    private TextView commentView;
    private TextView creationDate;
    private long id;
    private String title;
    private String comment;
    private long date;
    private List<String> imagesUri;
    private RecyclerView recyclerView;

    private void setUpViews() {
        titleView = findViewById(R.id.titleViewTask);
        commentView = findViewById(R.id.commentViewTask);
        creationDate = findViewById(R.id.CreationDateViewTask);
        recyclerView = findViewById(R.id.images_to_view);
    }

    private void getDataFromIntent(Intent intent) {
        id = intent.getLongExtra("id", -1);
        title = intent.getStringExtra("title");
        comment = intent.getStringExtra("comment");
        date = intent.getLongExtra("creation date", -1);
        imagesUri = intent.getStringArrayListExtra("pictures");

        List<Bitmap> images = new ArrayList<Bitmap>();
        for (String uri : imagesUri) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
                images.add(bitmap);
            } catch (IOException e) { }
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(new CustomGalleryAdapter2(this, images));
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setValuesInViews() {
        titleView.setText(title);
        commentView.setText(comment);
        creationDate.setText(ViewFormating.dateToString(date));

        List<Bitmap> images = new ArrayList<Bitmap>();
        for (String uri : imagesUri) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uri));
                images.add(bitmap);
            } catch (IOException e) { }
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(new CustomGalleryAdapter2(this, images));
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Log.d("LOLO", "start view activity");
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
}

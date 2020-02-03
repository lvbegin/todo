package com.example.todo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureGalleryAdapter extends RecyclerView.Adapter {
    public static final int REQUEST_CODE = 0xAA;
    private Activity activity;
    private List<String> imagesUri;

    public PictureGalleryAdapter(Activity a, List<String> images) {
        activity = a;
        this.imagesUri = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image, viewGroup, false);
        ImageView imageView = layout.findViewById(R.id.imageView);
        Bitmap bitmap = TaskBitmap.getBitmapOrDefault(Uri.parse(imagesUri.get(i)), activity);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 240, 240, false));

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
        Bitmap bitmap = TaskBitmap.getBitmapOrDefault(Uri.parse(imagesUri.get(i)), activity);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 240, 240, false));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TODO", "onClick on a picture" + i);
                Intent intent = ViewPicture.prepareIntent(PictureGalleryAdapter.this.activity, PictureGalleryAdapter.this.imagesUri, i);
                PictureGalleryAdapter.this.activity.startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesUri.size();
    }

    public void add(String uri)  {
        imagesUri.add(uri);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        imagesUri.remove(index);
        notifyDataSetChanged();
    }
}

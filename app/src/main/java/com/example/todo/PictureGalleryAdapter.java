package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureGalleryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Bitmap> images;
    private List<String> imagesUri;

    public PictureGalleryAdapter(Context c, List<String> images) {
        context = c;
        this.imagesUri = images;
        this.images = new ArrayList();
        for (String uri : images) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(c.getContentResolver(), Uri.parse(uri));
                this.images.add(bitmap);
            } catch (IOException e) { }
        }
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TODO", "onClick on a picture" + i);
                Intent intent = ViewPicture.prepareIntent(PictureGalleryAdapter.this.context, PictureGalleryAdapter.this.imagesUri, i);
                PictureGalleryAdapter.this.context.startActivity(intent);
            }
        });
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

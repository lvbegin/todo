package com.example.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

public class ViewPicture extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private RecyclerView listPictures;
    private ImageSwitcher imageView;
    private int currentPictureIndex;
    private GestureDetector gestureDetector;
    private List<String> uris;
    private Animation left_to_right_in;
    private Animation left_to_right_out;
    private Animation right_to_left_in;
    private Animation right_to_left_out;

    static public Intent prepareIntent(Context context, List<String> uris, int position) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("pictures", new ArrayList(uris));
        intent.putExtra("position", position);
        intent.setClass(context, ViewPicture.class);
        return intent;
    }

    private void retreiveDataFromIntent() {
        currentPictureIndex = getIntent().getExtras().getInt("position");
        uris = getIntent().getStringArrayListExtra("pictures");
    }

    private void initViews() {
        imageView = (ImageSwitcher)findViewById(R.id.image_fullscreen);
    }

    private void setUpPictureView() {
        gestureDetector = new GestureDetector(this);

        imageView.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });
        setNewImage(null, null);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return ViewPicture.this.gestureDetector.onTouchEvent(event);
            }
        });
    }

    private void initAnimationData() {
        left_to_right_in = AnimationUtils.loadAnimation(this, R.anim.letf_to_right_in_anim);
        left_to_right_out = AnimationUtils.loadAnimation(this, R.anim.left_to_right_out_anim);
        right_to_left_in = AnimationUtils.loadAnimation(this, R.anim.right_to_left_in_anim);
        right_to_left_out = AnimationUtils.loadAnimation(this, R.anim.right_to_left_out_anim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_picture);

        retreiveDataFromIntent();
        initViews();
        setUpPictureView();
        initAnimationData();
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d("TODO","onDown: " + event.toString());
        return true;
    }

    private void setNewImage(Animation in, Animation out) {
        Bitmap bitmap = TaskBitmap.getBitmapOrDefault(Uri.parse(uris.get(currentPictureIndex)), this);
        imageView.setInAnimation(in);
        imageView.setOutAnimation(out);
        imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        if ((event2.getRawX() - event1.getRawX() > 100) && (Math.abs(event2.getRawY() - event1.getRawY()) < 50)) {
            currentPictureIndex = (currentPictureIndex - 1);
            if (currentPictureIndex < 0)
                currentPictureIndex = uris.size() - 1;
        setNewImage(left_to_right_in, left_to_right_out);
        }
        else  if ((event1.getRawX() - event2.getRawX() > 100) && (Math.abs(event1.getRawY() - event2.getRawY()) < 50)) {
            currentPictureIndex = (currentPictureIndex  + 1) % uris.size();
        setNewImage(right_to_left_in, right_to_left_out);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d("TODO", "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d("TODO", "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.d("TODO", "onScroll: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d("TODO", "onLongPress: " + event.toString());
        new AlertDialog.Builder(this)
                .setMessage("Delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TODO", "yes pressed!");
                        Intent intent = new Intent();
                        intent.putExtra("picture_id", currentPictureIndex);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TODO", "No pressed!");
                    }
                }).create().show();
    }
}

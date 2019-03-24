package com.example.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


class MyViewHolder extends RecyclerView.ViewHolder {
    public MyViewHolder(View v) {
        super(v);
    }

    public View getParentView()  {
        return super.itemView;
    }
}


interface OnClickItem {
    void OnClickItem(int itemIndex);
    void OnLongClickItem(int itemIndex);
}

class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private String TAG = "MyAdapter";
    private ArrayList<Task> tasks;
    private OnClickItem onClickItem;

    public MyAdapter(ArrayList<Task> tasks, OnClickItem onClickItem) {
        super();
        this.tasks = tasks;
        this.onClickItem = onClickItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        TextView textView = viewHolder.itemView.findViewById(R.id.itemText);
        textView.setText(tasks.get(i).getTitle());
        Date date = new Date(tasks.get(i).getDate());
        TextView dateView = viewHolder.itemView.findViewById(R.id.itemDate);
        dateView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(date));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAdapter.this.onClickItem.OnClickItem(i);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("TOTO", "onLongClick called");
                MyAdapter.this.onClickItem.OnLongClickItem(i);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.item;
    }
}


public class MainActivity extends AppCompatActivity implements OnClickItem {

    private RecyclerView listToDo;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private Button newButton;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new MyAdapter(tasks, this);
        listToDo = (RecyclerView)findViewById(R.id.listtodo);
        listToDo.setHasFixedSize(true);
        listToDo.setLayoutManager(new LinearLayoutManager(this));
        listToDo.setAdapter( adapter);
        listToDo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        newButton = findViewById(R.id.new_task_button);
        ((View) newButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOLO", "button pressed");
                Toast.makeText(MainActivity.this, "new button pressed", 3).show();
                Intent intent = new Intent(MainActivity.this, NewTask.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void OnClickItem(int itemIndex) {
        Task t = MainActivity.this.tasks.get(itemIndex);
        Intent i = new Intent(this, viewTask.class);
        i.putExtra("task", t.getTitle());
        startActivity(i);
    }

    public void OnLongClickItem(final int itemIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("LOLO", "yes pressed");
                tasks.remove(itemIndex);
                adapter.notifyDataSetChanged();
            }
        }). setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("LOLO", "No pressed");
            }
        })
                .setMessage("Delete Task?")
                .create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_CANCELED)
            return ;
        String title = data.getExtras().getString("title");
        long date = new Date().getTime();
        Log.d("LOLO", DateFormat.getDateInstance(DateFormat.SHORT).format(date));
        Toast.makeText(MainActivity.this, "task added", 3).show();
        tasks.add(new Task(title, date));
        adapter.notifyDataSetChanged();

    }
}

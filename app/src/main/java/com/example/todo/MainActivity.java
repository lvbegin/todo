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


interface OnClickItem {
    void OnClickItem(int itemIndex);
    void OnLongClickItem(int itemIndex);
}

class MyViewHolder extends RecyclerView.ViewHolder {
    public MyViewHolder(View v) {
        super(v);
    }

    public void setDate(long date) {
        TextView dateView = this.itemView.findViewById(R.id.itemDate);
        dateView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(date)));
    }

    public void setTitle(String title) {
        TextView textView = this.itemView.findViewById(R.id.itemText);
        textView.setText(title);
    }

    public void setListeners(final OnClickItem onClick, final int itemIndex)
    {
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.OnClickItem(itemIndex);
            }
        });

        this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClick.OnLongClickItem(itemIndex);
                return false;
            }
        });

    }

}

class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
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
        viewHolder.setTitle(tasks.get(i).getTitle());
        viewHolder.setDate(tasks.get(i).getDate());
        viewHolder.setListeners(MyAdapter.this.onClickItem, i);
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
        new AlertDialog.Builder(this)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(itemIndex);
            }
        }). setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
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
        addTask(title);
    }

    private void addTask(String title) {
        long date = new Date().getTime();
        Toast.makeText(MainActivity.this, "task added", 3).show();
        tasks.add(new Task(title, date));
        adapter.notifyDataSetChanged();
    }

    private void deleteTask(int itemIndex) {
        tasks.remove(itemIndex);
        adapter.notifyDataSetChanged();
    }
}

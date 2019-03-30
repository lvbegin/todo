package com.example.todo;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;


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
        dateView.setText(ViewFormating.dateToString(date));
    }

    public void setTitle(String title) {
        TextView textView = this.itemView.findViewById(R.id.itemText);
        textView.setText(ViewFormating.TitleToDisplayTruncated(title));
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
    private List<TaskE> tasks;
    private OnClickItem onClickItem;

    public MyAdapter(List<TaskE> tasks, OnClickItem onClickItem) {
        super();
        this.tasks = tasks;
        this.onClickItem = onClickItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int i) {
        viewHolder.setTitle(tasks.get(i).title);
        viewHolder.setDate(tasks.get(i).creationDate);
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
    List<TaskE> tasks = null;
    private Button newButton;
    private MyAdapter adapter;
    private TaskDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), TaskDB.class, "tododb").allowMainThreadQueries().build();
        tasks = db.TaskDAO().getAll();

        adapter = new MyAdapter(tasks, this);
        listToDo = (RecyclerView)findViewById(R.id.listtodo);
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
        TaskE t = MainActivity.this.tasks.get(itemIndex);
        Intent i = new Intent(this, viewTask.class);
        i.putExtra("task", t.title);
        startActivity(i);
    }

    public void OnLongClickItem(final int itemIndex) {
        new AlertDialog.Builder(this)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(itemIndex);
            }
        }). setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        })
        .setMessage(R.string.delete_task_confirm)
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
        tasks.add(new TaskE(title, date));
        db.TaskDAO().insert(new TaskE(title, date));
        adapter.notifyDataSetChanged();
    }

    private void deleteTask(int itemIndex) {
        db.TaskDAO().delete(tasks.get(itemIndex));
        tasks.remove(itemIndex);
        adapter.notifyDataSetChanged();
    }
}

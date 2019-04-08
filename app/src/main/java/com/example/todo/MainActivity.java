package com.example.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
    private Button newButton;
    private MyAdapter adapter;
    private PersistentTaskList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new PersistentTaskList("tododb", getApplicationContext());

        adapter = new MyAdapter(list.getList(), this);
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

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                Log.d("LOLO", "getMovementFlags");
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                Log.d("LOLO", "onMove callback called");
                list.swap(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.d("LOLO", "onSwiped");
                int position = viewHolder.getAdapterPosition();
                list.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        helper.attachToRecyclerView(listToDo);
    }

    @Override
    public void OnClickItem(int itemIndex) {
        TaskE t = MainActivity.this.list.getList().get(itemIndex);
        Intent i = new Intent(this, viewTask.class);
        i.putExtra("task", t.title);
        startActivity(i);
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
        list.add(title, date);
        adapter.notifyDataSetChanged();
    }
}

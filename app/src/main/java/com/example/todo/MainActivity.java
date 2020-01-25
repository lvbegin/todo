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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;


interface OnClickItem {
    void OnClickItem(int itemIndex);
    void OnDoneChecked(int itemIndex, boolean checked);
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

    public void setChecked(boolean checked) {
        CheckBox checkBox = this.itemView.findViewById(R.id.doneBox);
        checkBox.setChecked(checked);
    }

    public void setListeners(final OnClickItem onClick, int itemIndex)
    {
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClick.OnClickItem(MyViewHolder.this.getAdapterPosition());
            }
        });
        CheckBox box = this.itemView.findViewById(R.id.doneBox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onClick.OnDoneChecked(MyViewHolder.this.getAdapterPosition(), isChecked);
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
        viewHolder.setChecked(tasks.get(i).done);
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
    private Button newButton;
    private MyAdapter adapter;
    private PersistentTaskList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new PersistentTaskList("tododb", getApplicationContext());

        adapter = new MyAdapter(list.getList(), this);
        RecyclerView listToDo = (RecyclerView)findViewById(R.id.listtodo);
        listToDo.setLayoutManager(new LinearLayoutManager(this));
        listToDo.setAdapter( adapter);
        listToDo.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        newButton = findViewById(R.id.new_task_button);
        ((View) newButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "new button pressed", 3).show();
                Intent intent = new Intent(MainActivity.this, TaskEntryActivity.class);
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
        displayTask(t);
    }

    @Override
    public void OnDoneChecked(int itemIndex, boolean checked) {
        TaskE t = MainActivity.this.list.getList().get(itemIndex);
        updateTask(t.tid, t.title, t.comment, checked);
        Log.d("TODO", "OnCheckedChange state changed on item " + new Integer(itemIndex).toString() + " " + (checked ? " selected" : "unselected"));
    }

    private void displayTask(TaskE t) {
        Intent intent = TaskEToIntent(t);
        intent.setClass(this, viewTask.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        /* should dispatch depending on requestCode */
        if (resultCode == RESULT_CANCELED)
            return ;

        switch (requestCode) {
            case 1:
                reactToNewTaskTermination(data);
                break;
            case 2:
                reactToViewTaskTermination(resultCode, data);
                break;
            case 3:
                reactToUpdateTaskTermination(data);
                break;
            default:
                Log.e("TODO", "unexpected request code");
        }
    }

    private void reactToViewTaskTermination(int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                TaskE t = list.getById(data.getLongExtra("id", -1));
                List<String> uris  = list.getUriPictureArrayList(t);
                Intent intent = TaskEntryActivity.prepareIntent(this, t.tid, t.title, t.comment, t.creationDate, uris);
                startActivityForResult(intent, 3);
                break;
            case 2:
                int position = list.idToIndex(data.getLongExtra("id", -1));
                list.remove(position);
                adapter.notifyItemRemoved(position);
                break;
            default:
                Log.e("TODO", "unexpected result code");
        }
    }

    private void reactToUpdateTaskTermination(Intent data) {
        long id = TaskEntryActivity.idTaskResult(data);
        updateTask(id, TaskEntryActivity.titleResult(data), TaskEntryActivity.commentResult(data));
        displayTask(MainActivity.this.list.getById(id));
    }

    private void reactToNewTaskTermination(Intent data) {
        String title = TaskEntryActivity.titleResult(data);
        String comment = TaskEntryActivity.commentResult(data);
        List<String> imagesUri = TaskEntryActivity.listImageUri(data);
        for (String image : imagesUri) {
            Log.d("TODO", "uri retrieved:" + image);
        }
        addTask(title, comment, imagesUri);
    }

    private Intent TaskEToIntent(TaskE t) {
        return viewTask.prepareIntent(this, t.tid);
    }

    private void updateTask(long id, String title, String comment, boolean checked) {
        list.update(id, title, comment, checked);
    }

    private void updateTask(long id, String title, String comment) {
        list.update(id, title, comment);
        adapter.notifyItemChanged(list.idToIndex(id));
    }

    private void addTask(String title, String comment, List<String> imagesUri) {
        long date = new Date().getTime();
        Toast.makeText(MainActivity.this, "task added", 3).show();
        list.add(title, comment, date, imagesUri);
        adapter.notifyDataSetChanged();
    }
}

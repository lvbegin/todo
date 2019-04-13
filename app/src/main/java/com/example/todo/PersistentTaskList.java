package com.example.todo;


import android.content.Context;

import java.util.Collections;
import java.util.List;

import androidx.room.Room;

public class PersistentTaskList {
    private TaskDB db;
    List<TaskE> tasks = null;

    public PersistentTaskList(String dbName, Context context) {
        db = Room.databaseBuilder(context, TaskDB.class, "tododb").allowMainThreadQueries().build();
        tasks = db.TaskDAO().getAll();
    }

    public List<TaskE> getList() {
        return  tasks;
    }

    void remove(int index) {
        db.TaskDAO().decrementPositionsFrom(index);
        db.TaskDAO().delete(tasks.get(index));
        tasks.remove(index);
        for (TaskE t: tasks) {
            if (t.position > index)
                t.position --;
        }
    }

    void swap(int position, int position1) {
        TaskE task = tasks.get(position);
        TaskE task1 = tasks.get(position1);
        task.position = position1;
        task1.position = position;
        db.TaskDAO().update(task, task1);
        Collections.swap(tasks, position, position1);
    }

    void add(String title, String comment, long date) {
        TaskE newTask = new TaskE(title, comment, date, tasks.size());
        tasks.add(newTask);
        db.TaskDAO().insert(newTask);

    }
}

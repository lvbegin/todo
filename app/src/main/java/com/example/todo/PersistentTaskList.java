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

    public void remove(int index) {
        db.TaskDAO().decrementPositionsFrom(index);
        db.TaskDAO().delete(tasks.get(index));
        tasks.remove(index);
        for (TaskE t: tasks) {
            if (t.position > index)
                t.position --;
        }
    }

    public void swap(int position, int position1) {
        TaskE task = tasks.get(position);
        TaskE task1 = tasks.get(position1);
        task.position = position1;
        task1.position = position;
        db.TaskDAO().update(task, task1);
        Collections.swap(tasks, position, position1);
    }

    public void add(String title, String comment, long date) {
        TaskE newTask = new TaskE(title, comment, date, tasks.size());
        tasks.add(newTask);
        db.TaskDAO().insert(newTask);

    }

    public int idToIndex(int id) {
        for(int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).tid == id)
                return i;
        }
        return -1;
    }

    public TaskE getById(int id) {
        int index = idToIndex((id));
        return -1 == index ? null : tasks.get(index);
    }

    public void update(int id, String title, String comment) {
        TaskE t = getById(id);
        t.title = title;
        t.comment = comment;
        db.TaskDAO().update(t);
    }
}

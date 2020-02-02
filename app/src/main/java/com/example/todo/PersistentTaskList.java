package com.example.todo;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.room.Room;

public class PersistentTaskList {
    private TaskDB db;
    List<TaskE> tasks = null;

    public PersistentTaskList(String dbName, Context context) {
        db = Room.databaseBuilder(context, TaskDB.class, dbName).allowMainThreadQueries().build();
        tasks = db.TaskDAO().getAll();
    }

    public List<TaskE> getList() {
        return  tasks;
    }

    public List<TaskPicture> getTaskPictureList(TaskE task) {
        return getTaskPictureListFromTaskId(task.tid);
    }

    public List<TaskPicture> getTaskPictureListFromTaskId(long taskId) {
        return db.TaskPictureDAO().getAllAssociatedToTask(taskId);
    }

    public ArrayList<String> getUriPictureArrayList(TaskE task) {
        List<TaskPicture> l  = db.TaskPictureDAO().getAllAssociatedToTask(task.tid);
        ArrayList<String> result = new ArrayList();
        for (TaskPicture taskPicture : l) {
            result.add(taskPicture.uri);
        }
        return result;
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

    public void addPicture(long taskId, String imageUri) {
        db.TaskPictureDAO().insert(new TaskPicture(taskId, imageUri));
    }

    public void removePicture(long taskId, int pictureIndex) {
        List<TaskPicture> l = getTaskPictureListFromTaskId(taskId);
        db.TaskPictureDAO().delete(l.get(pictureIndex));
    }

    public void swap(int position, int position1) {
        TaskE task = tasks.get(position);
        TaskE task1 = tasks.get(position1);
        task.position = position1;
        task1.position = position;
        db.TaskDAO().update(task, task1);
        Collections.swap(tasks, position, position1);
    }

    public void add(String title, String comment, long date, List<String> imagesUri, boolean done) {
        TaskE newTask = new TaskE(title, comment, date, tasks.size(), done);
        tasks.add(newTask);
        newTask.tid = db.TaskDAO().insert(newTask);
        for (String uri: imagesUri) {
            Log.d("TOTO", "inserting uri in db: " + uri);
            db.TaskPictureDAO().insert(new TaskPicture(newTask.tid, uri));
        }
    }

    public int idToIndex(long id) {
        for(int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).tid == id)
                return i;
        }
        return -1;
    }

    public TaskE getById(long id) {
        int index = idToIndex((id));
        return -1 == index ? null : tasks.get(index);
    }

    public void update(long id, String title, String comment) {
        TaskE t = getById(id);
        t.title = title;
        t.comment = comment;
        db.TaskDAO().update(t);
    }

    public void update(long id, String title, String comment, boolean done) {
        TaskE t = getById(id);
        t.title = title;
        t.comment = comment;
        t.done = done;
        db.TaskDAO().update(t);
    }

}

package com.example.todo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TaskE {

    public TaskE(String title, String comment, long creationDate, int position) {
//        this.tid = 0;
        this.title = title;
        this.comment = comment;
        this.creationDate = creationDate;
        this.position = position;
        this.done = false;
    }
    @PrimaryKey(autoGenerate = true)
    public long tid;

    @ColumnInfo(name = "task_title")
    public String title;

    @ColumnInfo(name = "comment")
    public String comment;

    @ColumnInfo(name = "creation_date")
    public long creationDate;

    @ColumnInfo(name = "done")
    public boolean done;

    @ColumnInfo(name = "position")
    public int position;

}

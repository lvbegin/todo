package com.example.todo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TaskE {

    public TaskE(String title, long creationDate) {
        this.tid = 0;
        this.title = title;
        this.creationDate = creationDate;
    }
    @PrimaryKey(autoGenerate = true)
    public int tid;

    @ColumnInfo(name = "task_title")
    public String title;

    @ColumnInfo(name = "creation_date")
    public long creationDate;
}

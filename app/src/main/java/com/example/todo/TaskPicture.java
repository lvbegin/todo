package com.example.todo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_picture",
        foreignKeys = @ForeignKey(entity = TaskE.class,
        parentColumns = "tid",
        childColumns = "task_id",
        onDelete = ForeignKey.CASCADE))
public class TaskPicture {

    public TaskPicture(long task_id, String uri) {
        this.task_id = task_id;
        this.uri = uri;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "task_id")
    public long task_id;

    @ColumnInfo(name = "picture_uri")
    public String uri;

}

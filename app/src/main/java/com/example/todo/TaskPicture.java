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

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "task_id")
    public int task_id;

    @ColumnInfo(name = "picture_uri")
    public String uri;

}

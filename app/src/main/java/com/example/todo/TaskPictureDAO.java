package com.example.todo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TaskPictureDAO {

    @Insert
    long insert(TaskPicture t);

    @Delete
    void delete(TaskPicture t);

    @Query("SELECT * FROM task_picture WHERE task_id = :task_id")
    List<TaskPicture> getAllAssociatedToTask(int task_id);
}

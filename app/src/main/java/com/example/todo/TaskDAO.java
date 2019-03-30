package com.example.todo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM TaskE")
    List<TaskE> getAll();

    @Insert
    void insert(TaskE t);

    @Delete
    void delete(TaskE t);
}

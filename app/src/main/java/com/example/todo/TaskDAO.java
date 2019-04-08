package com.example.todo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM TaskE order by position")
    List<TaskE> getAll();

    @Insert
    void insert(TaskE t);

    @Delete
    void delete(TaskE t);

    @Update
    void update(TaskE ...t);

    @Query("UPDATE TaskE SET " +
            "position = position - 1 " +
            "WHERE position > :position")
    void decrementPositionsFrom(int position);
}

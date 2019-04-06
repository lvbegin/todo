package com.example.todo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

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

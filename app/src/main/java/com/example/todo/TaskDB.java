package com.example.todo;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TaskE.class}, version = 1)
public abstract class TaskDB extends RoomDatabase {
    public abstract TaskDAO TaskDAO();
}

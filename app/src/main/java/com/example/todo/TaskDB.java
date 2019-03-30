package com.example.todo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {TaskE.class}, version = 1)
public abstract class TaskDB extends RoomDatabase {
    public abstract TaskDAO TaskDAO();
}

package com.example.todo;

import java.util.Date;

public class Task {
    private String title;
    private long date;

    public Task(String title, long date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

}

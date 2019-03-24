package com.example.todo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void can_retreive_task_title() {
        String title = new String("this is a title");
        long date = 99009900;
        Task t = new Task(title, date);
        assertEquals(title, t.getTitle());
        assertEquals(date, t.getDate());
    }
}
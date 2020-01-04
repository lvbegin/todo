package com.example.todo;

//import android.support.test.runner.AndroidJUnit4;

//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.Espresso;
//import android.support.test.espresso.action.ViewActions;
//import android.support.test.espresso.matcher.ViewMatchers;
//mport android.support.test.runner.AndroidJUnit4;
//import org.junit.Before;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PersistantTaskListTest {
    private PersistentTaskList list = null;
    private List<TaskE> l;

    @Before
    public void setup() {
        list = new PersistentTaskList("testdb", ApplicationProvider.getApplicationContext());
        l = list.getList();
        while (!l.isEmpty()) {
            list.remove(0);
        }
    }

    @Test
    public void addTaskSwapAndRemove() {
        long date1 = 11111111;
        String title1 = "test title 1";
        String comment1 = "test comment 1";
        ArrayList<String> dummyListUri1 = new ArrayList();
        long date2 = 22222222;
        String title2 = "test title 2";
        String comment2 = "test comment 2";
        ArrayList<String> dummyListUri2 = new ArrayList();
        dummyListUri2.add("dummy uri 1");
        long date3 = 33333333;
        String title3 = "test title 3";
        String comment3 = "test comment 3";
        ArrayList<String> dummyListUri3 = new ArrayList();
        dummyListUri3.add("dummy uri 2");
        dummyListUri3.add("dummy uri 3");

        assert (l.isEmpty());
        list.add(title1, comment1, date1, dummyListUri1);
        list.add(title2, comment2, date2, dummyListUri2);
        list.add(title3, comment3, date3, dummyListUri3);
        assertEquals(3, l.size());
        assertEquals(title1, l.get(0).title);
        assertEquals(date1, l.get(0).creationDate);
        assertEquals(0, l.get(0).position);
        assertEquals(title2, l.get(1).title);
        assertEquals(date2, l.get(1).creationDate);
        assertEquals(1, l.get(1).position);
        assertEquals(title3, l.get(2).title);
        assertEquals(date3, l.get(2).creationDate);
        assertEquals(2, l.get(2).position);

        list = new PersistentTaskList("testdb", ApplicationProvider.getApplicationContext());
        l = list.getList();

        assertEquals(3, l.size());
        assertEquals(title1, l.get(0).title);
        assertEquals(date1, l.get(0).creationDate);
        assertEquals(0, l.get(0).position);
        assertEquals(title2, l.get(1).title);
        assertEquals(date2, l.get(1).creationDate);
        assertEquals(1, l.get(1).position);
        assertEquals(title3, l.get(2).title);
        assertEquals(date3, l.get(2).creationDate);
        assertEquals(2, l.get(2).position);

        list.swap(0, 1);

        list = new PersistentTaskList("testdb", ApplicationProvider.getApplicationContext());
        l = list.getList();

        assertEquals(3, l.size());
        assertEquals(title2, l.get(0).title);
        assertEquals(date2, l.get(0).creationDate);
        assertEquals(0, l.get(0).position);
        assertEquals(title1, l.get(1).title);
        assertEquals(date1, l.get(1).creationDate);
        assertEquals(1, l.get(1).position);
        assertEquals(title3, l.get(2).title);
        assertEquals(date3, l.get(2).creationDate);
        assertEquals(2, l.get(2).position);

        list.remove(1);

        list = new PersistentTaskList("testdb", ApplicationProvider.getApplicationContext());
        l = list.getList();

        assertEquals(2, l.size());
        assertEquals(title2, l.get(0).title);
        assertEquals(date2, l.get(0).creationDate);
        assertEquals(0, l.get(0).position);
        assertEquals(title3, l.get(1).title);
        assertEquals(date3, l.get(1).creationDate);
        assertEquals(1, l.get(1).position);

        list.remove(0);

        assertEquals(1, l.size());
        assertEquals(title3, l.get(0).title);
        assertEquals(date3, l.get(0).creationDate);
        assertEquals(0, l.get(0).position);

        list.remove(0);
        assertEquals(0, l.size());

    }

    @Test
    public void addTaskWithPictures() {
        long date = 11111111;
        String title = "test title";
        String comment = "test comment";
        String dummyUri1 = "dummy uri 1";
        String dummyUri2 = "dummy uri 2";
        ArrayList<String> dummyListUri = new ArrayList();
        dummyListUri.add(dummyUri1);
        dummyListUri.add(dummyUri2);

        assert (l.isEmpty());
        list.add(title, comment, date, dummyListUri);
        l = list.getList();
        assertEquals(1, l.size());
        List<TaskPicture> taskPictures = list.getTaskPictureList(l.get(0));
        assertEquals(2, taskPictures.size());
        assertEquals(dummyUri1, taskPictures.get(0).uri);
        assertEquals(dummyUri2, taskPictures.get(1).uri);
    }

    @Test
    public void addTaskWithPicturesThenDelete() {
        long date = 11111111;
        String title = "test title";
        String comment = "test comment";
        String dummyUri1 = "dummy uri 1";
        String dummyUri2 = "dummy uri 2";
        ArrayList<String> dummyListUri = new ArrayList();
        dummyListUri.add(dummyUri1);
        dummyListUri.add(dummyUri2);

        assert (l.isEmpty());
        list.add(title, comment, date, dummyListUri);
        l = list.getList();
        assertEquals(1, l.size());
        TaskE t = l.get(0);
        list.remove(0);
        assertEquals(0, l.size());
        List<TaskPicture> taskPictures = list.getTaskPictureList(t);
        assertEquals(0, taskPictures.size());

        list = new PersistentTaskList("testdb", ApplicationProvider.getApplicationContext());
        l = list.getList();
        assertEquals(0, l.size());
        taskPictures = list.getTaskPictureList(t);
        assertEquals(0, taskPictures.size());


    }
}
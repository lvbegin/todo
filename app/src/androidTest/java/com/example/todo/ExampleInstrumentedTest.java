package com.example.todo;

//import android.support.test.runner.AndroidJUnit4;

//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.Espresso;
//import android.support.test.espresso.action.ViewActions;
//import android.support.test.espresso.matcher.ViewMatchers;
//mport android.support.test.runner.AndroidJUnit4;
//import org.junit.Before;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.regex.Matcher;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Before
    public void setup() {
        PersistentTaskList list = new PersistentTaskList("testdb", ApplicationProvider.getApplicationContext());
        List<TaskE> l = list.getList();

        while (!l.isEmpty()) {
            list.remove(0);
        }
    }

    @Test
    public void createTaskWithoutCommentAndView() {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.isCompletelyDisplayed()));
        onView(withId(R.id.listtodo))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
    }

    @Test
    public void createTaskWithCommentAndView() {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.add_comment_button)).perform(click());
        onView(withId(R.id.comment)).perform(typeText("new comment"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.isCompletelyDisplayed()));
        onView(withId(R.id.listtodo))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
    }

    @Test
    public void deleteTask() throws InterruptedException {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.isCompletelyDisplayed()));

        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.hasChildCount(1)));

        onView(withId(R.id.listtodo))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        Thread.sleep(1000);
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.hasChildCount(0)));
    }

    @Test
    public void cannotCreateTaskWithEmptyTitle() {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.no_title_error_message)).check(matches(ViewMatchers.isCompletelyDisplayed()));
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.isCompletelyDisplayed()));
        onView(withId(R.id.listtodo))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
    }

}

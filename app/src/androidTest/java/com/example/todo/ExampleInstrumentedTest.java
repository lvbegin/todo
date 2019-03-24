package com.example.todo;

//import android.support.test.runner.AndroidJUnit4;

//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.Espresso;
//import android.support.test.espresso.action.ViewActions;
//import android.support.test.espresso.matcher.ViewMatchers;
//mport android.support.test.runner.AndroidJUnit4;
//import org.junit.Before;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
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


    @Test
    public void createTaskAndView() {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.isCompletelyDisplayed()));

        onView(withId(R.id.itemLayout)).perform(click());
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
    }

    @Test
    public void deleteTask() {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.isCompletelyDisplayed()));

        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.hasChildCount(1)));

        onView(withId(R.id.itemLayout)).perform(longClick());
        onView(withText("Yes")).perform(click());

        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.hasChildCount(0)));
    }

}

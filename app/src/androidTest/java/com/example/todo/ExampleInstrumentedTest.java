package com.example.todo;

//import android.support.test.runner.AndroidJUnit4;

//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.Espresso;
//import android.support.test.espresso.action.ViewActions;
//import android.support.test.espresso.matcher.ViewMatchers;
//mport android.support.test.runner.AndroidJUnit4;
//import org.junit.Before;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.regex.Matcher;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
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
        PersistentTaskList list = new PersistentTaskList("tododb", ApplicationProvider.getApplicationContext());
        List<TaskE> l = list.getList();

        while (!l.isEmpty()) {
            list.remove(0);
        }
    }

    @Rule
    public IntentsTestRule<com.example.todo.MainActivity> intentsRule = new IntentsTestRule<>(com.example.todo.MainActivity.class);

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
    public void cannotCreateTaskWithEmptyTitle() throws InterruptedException {

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

    @Test
    public void createTaskAndThenEdit() {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.add_comment_button)).perform(click());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        onView(withText(R.string.edit_item)).perform(click());

        onView(withId(R.id.comment)).perform(typeText("new comment"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
        onView(withId(R.id.commentViewTask)).check(matches(withText("new comment")));

    }

    @Test
    public void createTaskAndDeleteOnView() throws InterruptedException {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.add_comment_button)).perform(click());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        onView(withText(R.string.dekete_item)).perform(click());

        Thread.sleep(1000);
        onView(withId(R.id.listtodo)).check(matches(ViewMatchers.hasChildCount(0)));

    }

    @Test
    public void createTaskAndThenCheckDone() {

        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        onView(withId(R.id.new_task_button)).perform(click());
        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.new_task_title)).perform(closeSoftKeyboard());
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.doneBox))
                .perform(click());
        onView(withId(R.id.doneBox)).check(matches(isChecked()));
    }

    private void checkImageDisplayed(int recyclerViewId, int nbImages) {
        Activity currentActivity = intentsRule.getActivity();
        RecyclerView view = (RecyclerView) currentActivity.findViewById(R.id.images);
        assert(nbImages == view.getAdapter().getItemCount());
    }

    private void testAddingPicture(String how, Intent resultData) {
        ActivityScenario<com.example.todo.MainActivity> scenario = ActivityScenario.launch(com.example.todo.MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));

        onView(withId(R.id.new_task_button)).perform(click());
        checkImageDisplayed(R.id.images, 0);
        onView(withId(R.id.add_picture_button)).perform(click());
        onView(withText(how)).perform(click());
        checkImageDisplayed(R.id.images, 1);

        onView(withId(R.id.new_task_title)).perform(typeText("new task"));
        onView(withId(R.id.done_new_task_button)).perform(click());
        onView(withId(R.id.listtodo))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.titleViewTask)).check(matches(withText("new task")));
        checkImageDisplayed(R.id.images_to_view, 1);
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getContext());
        onView(withText(R.string.edit_item)).perform(click());
        checkImageDisplayed(R.id.images, 1);
    }

    private Bitmap getABitmap() {
        return  BitmapFactory.decodeResource(intentsRule.getActivity().getResources(), R.mipmap.ic_launcher);

    }

    @Test
    public void userCameraToTakeAPhoto() {
        Intent resultData = new Intent();
        resultData.putExtra("data", getABitmap());

        testAddingPicture(intentsRule.getActivity().getString(R.string.camera), resultData);
    }

    @Test
    public void getPictureFromGallery() {
        Resources resources = intentsRule.getActivity().getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.mipmap.ic_launcher))
                .appendPath(resources.getResourceTypeName(R.mipmap.ic_launcher))
                .appendPath(resources.getResourceEntryName(R.mipmap.ic_launcher))
                .build();
        Intent resultData = new Intent();
        resultData.setData(uri);

        testAddingPicture(intentsRule.getActivity().getString(R.string.gallery), resultData);
    }

}

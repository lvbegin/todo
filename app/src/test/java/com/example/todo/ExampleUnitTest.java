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
    public void date_formatted_correctly() {
        long date = 891119878;
        assertEquals("11/01/70", ViewFormating.dateToString(date));
    }

    @Test
    public void title_too_long_is_truncated() {
        final String title = "This is a too long title";
        final String formattedtitle = "This is a too long t...";
        assertEquals(formattedtitle, ViewFormating.TitleToDisplayTruncated(title));
    }
}
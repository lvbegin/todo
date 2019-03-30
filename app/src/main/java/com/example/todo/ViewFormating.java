package com.example.todo;

import java.text.DateFormat;
import java.util.Date;

public class ViewFormating {
    static String dateToString(long date) {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(date));
    }

    static String TitleToDisplayTruncated(String title) {
        if (title.length() <= 23) {
            return title;
        }
        else
            return title.substring(0, 20) + "...";
    }
}

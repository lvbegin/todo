package com.example.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewFormating {
    static String dateToString(long date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(date));
    }

    static String TitleToDisplayTruncated(String title) {
        if (title.length() <= 23) {
            return title;
        }
        else
            return title.substring(0, 20) + "...";
    }
}

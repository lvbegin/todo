package com.example.todo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewFormating {
    static String dateToString(long date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(date));
    }

    static String TitleToDisplayTruncated(String title) {
        if (title == null)
            return "";
        else if (title.length() <= 53) {
            return title;
        }
        else
            return title.substring(0, 50) + "...";
    }
}

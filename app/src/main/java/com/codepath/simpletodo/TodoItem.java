package com.codepath.simpletodo;

/**
 * Created by culgnol on 8/27/17.
 */

public class TodoItem {
    public String textBody;
    public int days;

    public TodoItem(String textBody, int remindDays) {
        this.textBody = textBody;
        this.days = remindDays;
    }
}

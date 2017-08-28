package com.codepath.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by culgnol on 8/27/17.
 */

public class TodoItemsAdapter extends ArrayAdapter<TodoItem> {
    public TodoItemsAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context, R.layout.todo_item, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Lookup view for data population
        TextView tvItemBody = (TextView) convertView.findViewById(R.id.tvItemBody);
        TextView tvRemindDays = (TextView) convertView.findViewById(R.id.tvRemindDays);
        // Populate the data into the template view using the data object
        tvItemBody.setText(todoItem.textBody);
        tvRemindDays.setText(String.format("( %1$d )", todoItem.days));
        // Return the completed view to render on screen
        return convertView;

    }
}

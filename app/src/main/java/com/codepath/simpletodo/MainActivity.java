package com.codepath.simpletodo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.R.attr.order;
import static android.R.attr.text;
import static com.raizlabs.android.dbflow.sql.language.property.PropertyFactory.from;

public class MainActivity extends AppCompatActivity implements EditItemDialogFrament.EditNameDialogListener {
    ArrayList<TodoItem> items;
    TodoItemsAdapter itemsAdapter;
    ListView lvItems;
    TodoModel todoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create list of items and Display in ListView
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<TodoItem>();
        //Create model
        todoModel = new TodoModel();
        readItems();
        itemsAdapter = new TodoItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();

    }

    public void launchEditItemView(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        TodoItem item = items.get(pos);
        int days = (item.days > 0) ? item.days : 1; // if past due
        EditItemDialogFrament editItemDialogFrament = EditItemDialogFrament.newInstance(item.textBody, pos, days);
        editItemDialogFrament.show(fm, "fragment_edit_item");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        updateDb(items.get(pos).toString(), pos, 0, 2);
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        launchEditItemView(pos);
                    }
                }
        );
    }

    @Override
    public void onFinishEditDialog(String todoText, int listPosition, int remindDays) {
        updateItem(todoText, listPosition, remindDays);
    }

    private void updateItem(String item, int pos, int days) {
        TodoItem tItem = new TodoItem(item, days);
        items.set(pos, tItem);
        itemsAdapter.notifyDataSetChanged();
        updateDb(item, pos, days, 1);
    }

    public void onAddItem(View v)
    {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        int daysDefault = 1;

        TodoItem item = new TodoItem(itemText, daysDefault);

        itemsAdapter.add(item);
        etNewItem.setText("");

        int indexOfLastItem = items.size()-1;
        writeDb(item.textBody, indexOfLastItem, item.days);
    }

    private void readItems() {
        try {
            List<TodoModel> todoList = SQLite.select()
                    .from(TodoModel.class)
                    .where()
                    .orderBy(TodoModel_Table.createdOn, true).queryList();

            for (TodoModel item: todoList) {
                Date modifiedDate = new Date(item.modifiedOn * 1000L);
                Date currentDate = Calendar.getInstance().getTime();

                long diff = currentDate.getTime() - modifiedDate.getTime();
                int daysLeft = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                items.add(new TodoItem(item.textBody, (item.days - daysLeft) ));
            }
        } catch (Exception e) {
            items = new ArrayList<TodoItem>();
        }
    }

    private void writeDb(String textBody, int listPos, int days) {
        long timestamp;
        try {
            timestamp = System.currentTimeMillis() / 1000L;
            SQLite.insert(TodoModel.class)
                    .columns(TodoModel_Table.textBody, TodoModel_Table.listPos, TodoModel_Table.days, TodoModel_Table.createdOn, TodoModel_Table.modifiedOn)
                    .values(textBody, listPos, days, timestamp, timestamp)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDb(String textBody, int listPos, int days, int status) {
        long timeStamp;
        try {
            TodoModel todo = queryDb(listPos);

            switch (status)
            {
                case 2:
                    todo.delete();
                    break;
                case 1:
                default:
                    timeStamp = System.currentTimeMillis() / 1000;
                    todo.setTextBody(textBody);
                    todo.setDays(days);
                    todo.setModifiedOn(timeStamp);
                    todo.save();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TodoModel queryDb(int listPos) {
        TodoModel todo = null;
        try {
            todo = SQLite.select()
                .from(TodoModel.class)
                .where(TodoModel_Table.listPos.eq(listPos))
                .querySingle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return todo;
    }
}

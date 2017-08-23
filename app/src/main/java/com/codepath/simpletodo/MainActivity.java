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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.R.attr.order;
import static android.R.attr.text;
import static com.raizlabs.android.dbflow.sql.language.property.PropertyFactory.from;

public class MainActivity extends AppCompatActivity implements EditItemDialogFrament.EditNameDialogListener {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    TodoModel todoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create list of items and Display in ListView
        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        //Create model
        todoModel = new TodoModel();

        setupListViewListener();

    }

    public void launchEditItemView(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialogFrament editItemDialogFrament = EditItemDialogFrament.newInstance(items.get(pos).toString(), pos);
        editItemDialogFrament.show(fm, "fragment_edit_item");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        updateDb(items.get(pos).toString(), pos, 2);
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
    public void onFinishEditDialog(String inputText, int position) {
        updateItem(inputText, position);
    }

    private void updateItem(String item, int pos) {
        items.set(pos, item);
        itemsAdapter.notifyDataSetChanged();
        updateDb(item, pos, 1);
    }

    public void onAddItem(View v)
    {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");

        int indexOfLastItem = items.size()-1;
        writeDb(items.get(indexOfLastItem).toString(), indexOfLastItem);
    }

    private void readItems() {
        try {
            List<TodoModel> todoList = SQLite.select()
                    .from(TodoModel.class)
                    .where()
                    .orderBy(TodoModel_Table.createdOn, true).queryList();

            for (TodoModel item: todoList) {
                items.add(item.textBody);
            }
        } catch (Exception e) {
            items = new ArrayList<String>();
        }
    }

    private void writeDb(String textBody, int listPos) {
        long timestamp;
        try {
            timestamp = System.currentTimeMillis() / 1000;
            SQLite.insert(TodoModel.class)
                    .columns(TodoModel_Table.textBody, TodoModel_Table.listPos, TodoModel_Table.createdOn)
                    .values(textBody, listPos, timestamp)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDb(String textBody, int listPos, int status) {
        try {
            TodoModel todo = SQLite.select()
                    .from(TodoModel.class)
                    .where(TodoModel_Table.listPos.eq(listPos))
                    .querySingle();

            switch (status)
            {
                case 2:
                    todo.delete();
                    break;
                case 1:
                default:
                    todo.setTextBody(textBody);
                    todo.save();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

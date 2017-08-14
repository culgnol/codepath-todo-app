package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class EditItemActivity extends AppCompatActivity {
    String item_body;
    int item_pos;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item_body = getIntent().getStringExtra("item_body");
        item_pos = getIntent().getIntExtra("item_pos", -1);
        editText = (EditText) findViewById(R.id.etEditItem);
        editText.setText(item_body);
        editText.setSelection(item_body.length()); //set cursor position at end of string
    }

    public void onSubmit(View v) {
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("item_pos", item_pos);
        data.putExtra("item_body", editText.getText().toString());
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        this.finish();
    }
}

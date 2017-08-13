package com.codepath.simpletodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class EditItemActivity extends AppCompatActivity {
    String item_body;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item_body = getIntent().getStringExtra("item_body");
        editText = (EditText) findViewById(R.id.etEditItem);
        editText.setText(item_body);
        editText.setSelection(item_body.length()); //set cursor position at end of string
    }

    public void onSubmit(View v) {
        this.finish();
    }
}

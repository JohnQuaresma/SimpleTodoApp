package com.codepath.starter.simpletodoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore.Files;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {
    private final int EDIT_REQUEST_CODE = 14;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long rowId) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch other view
                Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
                i.putExtra("item_value", items.get(position).toString());
                i.putExtra("item_position", position);
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    private void saveItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile,  items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateResult(int resultCode, Intent data) {
        return resultCode == RESULT_OK && data != null;
    }

    private void updateItem(Integer position, String value) {
        items.remove(position.intValue());
        items.add(position, value);
        itemsAdapter.notifyDataSetChanged();
        saveItems();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView) findViewById(R.id.lvItems);
		items = new ArrayList<String>();
		readItems();
		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		setupListViewListener();
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!validateResult(resultCode, data)) {
            //handle error
            System.err.println(String.format("Got error result code %d for request %d.  Data was: %s", resultCode, requestCode, data));
            return;
        }
        switch(requestCode) {
            case EDIT_REQUEST_CODE:
                Integer itemPosition = data.getExtras().getInt("item_position");
                String itemValue = data.getExtras().getString("item_value");
                updateItem(itemPosition, itemValue);
            break;
            default:
                System.err.println("Unknown request code: " + requestCode);
        }
    }

    public void addToDoItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        saveItems();
    }
}

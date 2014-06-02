package com.codepath.starter.simpletodoapp;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class EditItemActivity extends Activity {

    EditText etItemValue;
    Integer itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemValue = getIntent().getStringExtra("item_value");
        itemPosition = getIntent().getIntExtra("item_position", -1);
        etItemValue = (EditText) findViewById(R.id.etItemValue);
        etItemValue.setText(itemValue);
        /*Getting length, which is also the position where we'd like to place the cursor.*/
        int cursorPosition = etItemValue.length();
        Editable itemValueEditable = etItemValue.getText();
        /*Set the cursor position*/
        Selection.setSelection(itemValueEditable, cursorPosition);
    }

    public void updateToDoItem(View v) {
        String newText = etItemValue.getText().toString();
        Intent data = new Intent();
        data.putExtra("item_value", newText);
        data.putExtra("item_position", itemPosition);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.dialogbox;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Contacts;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactPicker extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.contactpicker);


        final Uri data = Uri.parse("content://contacts/" + "people/");
        final Cursor c = managedQuery(data, null, null, null, null);

        String[] from = new String[] {Contacts.People.NAME};
        int[]  to = new int[] { R.id.itemTextView };

        // The SimpleCursorArrayAdapter lets you assign Cursor data,
        // used by Content Providers, to Views.
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.listitemlayout,c,from,to);
        ListView lv = (ListView)findViewById(R.id.contactListView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Add an onItemClickListener to the List View. Selecting
            // a contact from the list should return a path to
            // the item to the calling Activity.
            public void onItemClick(AdapterView<?> parent, View view,
                                    int pos, long id) {
                // Move the cursor to the selected item
                c.moveToPosition(pos);
                // Extract the row id.
                String name = c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME));
                // Construct the result URI.
                Uri outURI = Uri.parse(name);
                Intent outData = new Intent();
                outData.setData(outURI);
                setResult(Activity.RESULT_OK, outData);
                finish();
            }
        });
    }
}
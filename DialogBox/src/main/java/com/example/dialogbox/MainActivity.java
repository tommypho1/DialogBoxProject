package com.example.dialogbox;

import android.annotation.TargetApi;
import android.app.AlertDialog;

import android.app.Dialog;
import android.app.DialogFragment;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;

import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private SharedPreferences prefs;
    private String prefName = "MyPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveBtn = (Button) findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View _view) {
                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                EditText e = (EditText) findViewById(R.id.name);
                editor.putString("name", e.getText().toString());
                editor.commit();
                showDialog(0);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        String name = prefs.getString("name", "Default Value");

        switch (id) {
            case 0:
                return new AlertDialog.Builder(this)
                                        .setMessage(name)
                                        .setTitle("You Entered: ")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int whichButton)
                                                {
                                                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                                                    int rawContactInsertIndex = ops.size();

                                                    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null )
                                                            .build());
                                                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                                                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, prefs.getString("name", "Default Value"))
                                                            .build());

                                                    try {
                                                        ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                                    } catch (RemoteException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                    } catch (OperationApplicationException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                    }

                                                    startActivity(new Intent ("com.dn.pick"));
                                                 }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton)
                                                {

                                                }
                                        })

                                        .create();
        }
        return null;
    }
    
}

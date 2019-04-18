/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bl4k3.keno;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;


/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mTtlclsEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mclsatndEditText;
    Uri currentSubUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.subname);
        mclsatndEditText = findViewById(R.id.clsatnd);
        mTtlclsEditText = findViewById(R.id.ttlcls);

        // intent from main activity
        Intent intent = getIntent();
        currentSubUri = intent.getData();
        Log.i("uri", String.valueOf(currentSubUri));
        if(currentSubUri==null)
            setTitle("Add New Subject");
        else
        {
            setTitle("Edit Subject");
            Cursor cursor = getContentResolver().query(currentSubUri,null,null,null,null);
            cursor.moveToFirst();
            mNameEditText.setText(cursor.getString(1));
            mTtlclsEditText.setText(cursor.getString(2));
            mclsatndEditText.setText(cursor.getString(3));
            cursor.close();
        }




    }

       /* private void insertSubject() {

            int flag =0,clsatnd=0,ttlcls=0;

            String nameString = mNameEditText.getText().toString().trim();
            String clasattended  = mclsatndEditText.getText().toString().trim();
            String totalclasses = mTtlclsEditText.getText().toString().trim();

            if(clasattended.length()!=0)
                clsatnd = Integer.valueOf(clasattended);

            if(totalclasses.length()!=0)
                ttlcls = Integer.valueOf(totalclasses);

            if(clsatnd>ttlcls )
            {
                mTtlclsEditText.setError("Invalid");
                mclsatndEditText.setError("Invalid");
                flag=1;
            }
            if(nameString.length()<1)
            {
                Log.i("INvlaid","NAme text");
                mNameEditText.setError("Invalid Name");
                flag=1;
            }
            Log.i("flag", String.valueOf(flag));
           if(flag==0){
               // Create a ContentValues object where column names are the keys,
               // and pet attributes from the editor are the values.
               ContentValues values = new ContentValues();
               values.put(SubjectContract.SubjectEntry.COLUMN_SUB_NAME, nameString);
               values.put(SubjectContract.SubjectEntry.COLUMN_TTLCLS, ttlcls);
               values.put(SubjectContract.SubjectEntry.COLUMN_CLSATND, clsatnd);


               // Insert a new pet into the provider, returning the content URI for the new pet.

               Uri newUri=null;
               Integer rowsupdated=0;
               if(getTitle().equals("Add New Subject"))
               {
                   newUri = getContentResolver().insert(SubjectContract.SubjectEntry.CONTENT_URI, values);
                   // Show a toast message depending on whether or not the insertion was successful
                   if (newUri == null) {
                       // If the new content URI is null, then there was an error with insertion.
                       Toast.makeText(this, "Invalid Data",
                               Toast.LENGTH_SHORT).show();
                   } else {
                       // Otherwise, the insertion was successful and we can display a toast.
                       Toast.makeText(this, "Subject Inserted Successfully",
                               Toast.LENGTH_SHORT).show();
                       finish();
                   }
               }
               else
               {
                   rowsupdated = getContentResolver().update(currentSubUri,values,null,null);
                   if (rowsupdated == 0) {
                       // If the new content URI is null, then there was an error with insertion.
                       Toast.makeText(this, "Invalid Data",
                               Toast.LENGTH_SHORT).show();
                   } else {
                       // Otherwise, the insertion was successful and we can display a toast.
                       Toast.makeText(this, "Subject Updated !!",
                               Toast.LENGTH_SHORT).show();
                       finish();
                   }
               }
           }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_save:
                insertSubject();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
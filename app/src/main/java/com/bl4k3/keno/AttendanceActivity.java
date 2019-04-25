package com.bl4k3.keno;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class AttendanceActivity extends AppCompatActivity {
    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.SUBJECTNAME, DatabaseHelper.TOTALCONDUCTED, DatabaseHelper.TOTALATTENDED};

    final int[] to = new int[] { R.id.id, R.id.SubjectName, R.id.TotalConducted, R.id.TotalAttended};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        DBManager dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        // OnCLickListener For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView name = (TextView) view.findViewById(R.id.SubjectName);
                TextView email = (TextView) view.findViewById(R.id.TotalConducted);
                TextView password = (TextView) view.findViewById(R.id.TotalAttended);

                String id = idTextView.getText().toString();
                String SubjectN = name.getText().toString();
                String TotalC = email.getText().toString();
                String TotalA = password.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifySubject.class);
                modify_intent.putExtra("SubjectName", SubjectN);
                modify_intent.putExtra("TotalC", TotalC);
                modify_intent.putExtra("TotalA", TotalA);
                modify_intent.putExtra("id", id);

                startActivity(modify_intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_subject) {

            Intent add_mem = new Intent(this, AddSubject.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }
}

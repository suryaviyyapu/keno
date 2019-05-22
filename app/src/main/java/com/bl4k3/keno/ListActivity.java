package com.bl4k3.keno;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.bl4k3.keno.NoteAdapter;
import com.bl4k3.keno.Note;
import com.bl4k3.keno.R;

public class ListActivity extends AppCompatActivity {

    public static final String TableNote = "Notes";

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private ArrayList<Note> mDataList;
    private DatabaseReference mDbRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDbRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUI();
        observeDatabase();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signOut) {
            mAuth.signOut();
            startActivity(new Intent(this, Welcome.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void initUI() {

        mDataList = new ArrayList<>();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, PostNoteActivity.class));
                Snackbar.make(view,"kek", Snackbar.LENGTH_SHORT);
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        mAdapter = new NoteAdapter(this, mDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void observeDatabase() {
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDataList.clear();
                DataSnapshot tableNote = dataSnapshot.child(TableNote);

                for (DataSnapshot child: tableNote.getChildren()) {
                    Note tmpNote = child.getValue(Note.class);
                    mDataList.add(tmpNote);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

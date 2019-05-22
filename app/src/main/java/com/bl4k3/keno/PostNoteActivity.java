package com.bl4k3.keno;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.bl4k3.keno.ListActivity.TableNote;

public class PostNoteActivity extends AppCompatActivity {

    public static final String REQUIRED = "Required";

    private EditText mTitleField, mMessageField;
    private RadioGroup mNoteGradeRadioGroup;
    private Button mPostButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_note);

        mAuth = FirebaseAuth.getInstance();

        initUI();
    }

    private void initUI() {

        mTitleField = (EditText) findViewById(R.id.titlePost);
        mMessageField = (EditText) findViewById(R.id.messagePost);
        mNoteGradeRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mPostButton = (Button) findViewById(R.id.buttonPost);

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mTitleField.getText())) {
                    mTitleField.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(mMessageField.getText())) {
                    mMessageField.setError(REQUIRED);
                    return;
                }

                int radioChecked = mNoteGradeRadioGroup.getCheckedRadioButtonId();
                NoteGrade noteGrade;
                if (radioChecked == R.id.radioImportant) {
                    noteGrade = NoteGrade.Important;
                } else if (radioChecked == R.id.radioMedium) {
                    noteGrade = NoteGrade.Medium;
                } else {
                    noteGrade = NoteGrade.Light;
                }

                View view = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view != null ? view.getWindowToken() : null, 0);

                setEditingEnabled(false);
                postNote(new Note(mTitleField.getText().toString(), mMessageField.getText().toString(), noteGrade, mAuth.getCurrentUser().getUid()));
                Toast.makeText(PostNoteActivity.this,"Posting...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postNote(Note note) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        String key = mDbRef.child(TableNote).push().getKey();

        mDbRef.child(TableNote).child(key).setValue(note, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                mPostButton.setEnabled(true);

                if (databaseError == null) {
                    Toast.makeText(PostNoteActivity.this, "Successfully posted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostNoteActivity.this, ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(new Intent(PostNoteActivity.this, com.bl4k3.keno.ListActivity.class));
                    finish();
                } else {
                    Toast.makeText(PostNoteActivity.this, "Note was not posted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setEditingEnabled(boolean state) {
        mTitleField.setEnabled(state);
        mMessageField.setEnabled(state);
        mNoteGradeRadioGroup.setEnabled(state);
        mPostButton.setEnabled(state);
    }

}

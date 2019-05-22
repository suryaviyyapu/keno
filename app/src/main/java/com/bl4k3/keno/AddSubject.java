package com.bl4k3.keno;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubject extends Activity implements OnClickListener {

    private EditText subject_name_EditText, total_conducted_EditText, total_attended_EditText;

    String Tag = "AddSubjectActivity";
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Subject");

        setContentView(R.layout.activity_add_record);

        subject_name_EditText = findViewById(R.id.Name_Text);
        total_conducted_EditText = findViewById(R.id.TotalConducted);
        total_attended_EditText =  findViewById(R.id.TotalAttended);
        Button addSub = findViewById(R.id.add_subject);

        dbManager = new DBManager(this);
        dbManager.open();
        addSub.setOnClickListener(this);
        //show password

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_subject) {
            final String subject_name = subject_name_EditText.getText().toString();
            final String total_conducted = total_conducted_EditText.getText().toString();
            final String total_attended = total_attended_EditText.getText().toString();
            String percent = "0";
            if (subject_name.isEmpty()) {
                subject_name_EditText.setError("Subject name is required");
                subject_name_EditText.requestFocus();
                return;
            }
            if (total_conducted.isEmpty()) {
                total_conducted_EditText.setError("Total hours is required");
                total_conducted_EditText.requestFocus();
                return;
            }
            if (total_attended.isEmpty()) {
                total_attended_EditText.setError("Total attended hours required");
                total_attended_EditText.requestFocus();
                return;
            }
            double tc = Integer.parseInt(total_conducted);
            double ta = Integer.parseInt(total_attended);
            Toast.makeText(getApplicationContext(),"tc is "+tc+"ta is "+ ta,Toast.LENGTH_SHORT).show();
            percent = String.valueOf((ta/tc)*100);
            dbManager.insert(subject_name, total_conducted, total_attended, percent);
            Log.i(Tag,"Inserted into DB");
            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
                Intent main = new Intent(AddSubject.this, AttendanceActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
        }
    }

}
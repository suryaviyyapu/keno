package com.bl4k3.keno;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ModifySubject extends Activity implements OnClickListener {

    private TextView Subject_Name, totalConducted, totalAttended;
    String Tag = "ModifySubjectActivity";
    public String subjectName, totalconducted, totalattended;
    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Subject");

        setContentView(R.layout.activity_modify_record);

        dbManager = new DBManager(this);
        dbManager.open();
        totalConducted = findViewById(R.id.conductedNumber);
        Subject_Name =  findViewById(R.id.subName);
        totalAttended = findViewById(R.id.attendedNumber);
        Button presentBtn = (Button) findViewById(R.id.btn_present);
        Button deleteBtn = (Button) findViewById(R.id.btn_delete);
        Button absentBtn = findViewById(R.id.btn_absent);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        subjectName = intent.getStringExtra("SubjectName");
        totalconducted = intent.getStringExtra("TotalC");
        totalattended = intent.getStringExtra("TotalA");

        _id = Long.parseLong(id);

        Subject_Name.setText(subjectName);
        totalConducted.setText(totalconducted);
        totalAttended.setText(totalattended);

        presentBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_present:
                int conduct = Integer.parseInt(totalconducted);
                int attended = Integer.parseInt(totalattended);
                String present = String.valueOf(attended+1);
                String total = String.valueOf(conduct+1);
                /*String name = NameText.getText().toString();
                //String email = Subject_Name.getText().toString();
                String pass = PasswordText.getText().toString();
                if(name.isEmpty()){
                    NameText.setError("Provider name required");
                    NameText.requestFocus();
                    break;
                }
                /*if (email.isEmpty()){
                    Subject_Name.setError("Email is required");
                    Subject_Name.requestFocus();
                    break;
                }
                if (pass.isEmpty()){
                    PasswordText.setError("Password required");
                    PasswordText.requestFocus();
                    break;
                }
               // dbManager.insert(name, email, pass);*/

                dbManager.update(_id, subjectName, total, present);
                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), AttendanceActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}

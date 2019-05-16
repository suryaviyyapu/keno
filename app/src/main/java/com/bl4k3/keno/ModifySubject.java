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
    int conduct, attended;

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
        absentBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_present:
                conduct = Integer.parseInt(totalconducted);
                attended = Integer.parseInt(totalattended);
                String present = String.valueOf(attended+1);
                String total = String.valueOf(conduct+1);
                dbManager.update(_id, subjectName, total, present);
                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
            case R.id.btn_absent:
                conduct = Integer.parseInt(totalconducted);
                attended = Integer.parseInt(totalattended);
                total = String.valueOf(conduct+1);
                present = String.valueOf(attended);
                dbManager.update(_id, subjectName,total,present);
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

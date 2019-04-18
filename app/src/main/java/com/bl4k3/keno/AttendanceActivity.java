package com.bl4k3.keno;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AttendanceActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            finish();
            startActivity(new Intent(this,Welcome.class));
            Toast.makeText(getApplicationContext(),"You Must Login First",Toast.LENGTH_LONG).show();
        }

    }
}

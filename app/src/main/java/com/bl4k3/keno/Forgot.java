package com.bl4k3.keno;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot extends AppCompatActivity {
    private static final String TAG = "Forgot";
    Button forgotButton;
    EditText editText;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        editText = findViewById(R.id.forgotemail);
        forgotButton = findViewById(R.id.forgot);
        mAuth = FirebaseAuth.getInstance();
    }

    public void forgotPassword() {
        String emailAddress = editText.getText().toString().trim();
        if (emailAddress.isEmpty()) {
            editText.setError("Enter a valid Email");
            editText.requestFocus();
        } else {

            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                startActivity(new Intent(Forgot.this,Welcome.class));
                                Toast.makeText(getApplicationContext(),"Reset Link is sent to the email i\'d you provided",Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    public void forgot(View view){
        forgotPassword();
    }
}

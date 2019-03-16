package com.bl4k3.keno;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends Activity {

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    EditText editTextEmail, editTextPassword;
    Button buttonSignup, buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();

        //textView = findViewById(R.id.logo);
        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editText_Password);
        buttonLogin = findViewById(R.id.login);
        buttonSignup = findViewById(R.id.signup);

        TextView tx = (TextView)findViewById(R.id.logo);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/HandStrike.ttf");
        tx.setTypeface(custom_font);
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            Toast.makeText(this, " Connection Established ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " NO INTERNET ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email Address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Enter your Password");
            editTextPassword.requestFocus();
            return;
        }
        boolean state = checkInternetConnection();
        if (!state){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null)
                    if(!user.isEmailVerified()){
                        startActivity(new Intent(Welcome.this,Confirmation.class));

                    } else if(user.isEmailVerified()) {
                        finish();
                        Intent intent = new Intent(Welcome.this, Keno.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //notification();
                        startActivity(intent);
                    }
                } else {
                    editTextPassword.setText("");
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void login(View view){
        userLogin();
    }
    public void signupPage(View view){
        startActivity(new Intent(Welcome.this,Signup.class));
    }
    public void forgotPage(View view){startActivity(new Intent(Welcome.this,Forgot.class));}

    @Override
    protected void onStart() {
        super.onStart();
        //checkInternetConenction();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if(!user.isEmailVerified()) {
              startActivity(new Intent(Welcome.this, Confirmation.class));
            } else if(user.isEmailVerified()) {
                finish();
                Intent intent = new Intent(Welcome.this, Keno.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //startActivity(new Intent(this,Keno.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(a);

    }
}

package com.bl4k3.keno;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Signup extends AppCompatActivity {
    ProgressBar progressBar;
    private final static String TAG = "signup";
    Button signup,login;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText editTextEmail, editTextUsername, editTextPhone, editTextCPassword, editTextPassword;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername = findViewById(R.id.Username);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        checkBox = findViewById(R.id.checkBox);
        editTextEmail = findViewById(R.id.Email);
        editTextPhone = findViewById(R.id.mobile);
        editTextPassword = findViewById(R.id.Password);
        editTextCPassword = findViewById(R.id.CPassword);
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

    }


    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String mobile = editTextPhone.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,15}$";
        String cpassword = editTextCPassword.getText().toString().trim();

        if(!password.matches(regexp)){
            editTextPassword.setError("Weak Password must contain Numbers, Upper and Lower case letters");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(cpassword)) {
            editTextCPassword.setError("Password Doesn't match");
            editTextCPassword.requestFocus();
            return;
        }
        if (!validateInputs(username, email, mobile, password)) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser fbUser = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();
                                Log.d(TAG,"Done");
                                fbUser.updateProfile(profileChangeRequest);
                                CollectionReference dbusers = db.collection("Users");
                                final User user= new User(
                                        username,
                                        email,
                                        password,
                                        mobile
                                );
                                dbusers.add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                getinfo();
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Signup.this, "Signup Successful", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(new Intent(Signup.this,Confirmation.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    private void getinfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = editTextUsername.getText().toString().trim();
        //Name and other info setting to account
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
        if(user!=null)
            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Successful getUser",Toast.LENGTH_LONG).show();
                    }
                }
            });
    }


    private boolean validateInputs(String username, String email, String mobile, String password) {

        if (username.isEmpty()) {
            editTextUsername.setError("A Username is required");
            editTextUsername.requestFocus();
            return true;
        }

        if (username.length() < 5) {
            editTextUsername.setError("Length should be minimum of 5");
            editTextUsername.requestFocus();
            return true;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return true;
        }

        if (mobile.isEmpty()) {
            editTextPhone.setError("A Valid Mobile number is required");
            editTextPhone.requestFocus();
            return true;
        }
        if (mobile.length() != 10) {
            editTextPhone.setError("Provide a valid mobile number");
            editTextPhone.requestFocus();
            return true;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return true;
        }

        if (password.length() < 8) {
            editTextPassword.setError("Minimum length of password should be 8");
            editTextPassword.requestFocus();
            return true;
        }
        if(!checkBox.isChecked()){
            checkBox.setError("Agree Terms and Conditions");
            checkBox.requestFocus();
        }
        return false;
    }


    public void signup(View view) {
        registerUser();
    }

    public void loginPage(View view){
        startActivity(new Intent(Signup.this,Welcome.class));
    }
}


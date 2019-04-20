package com.bl4k3.keno;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {
    private static final String TAG = "Settings";
    FirebaseAuth mAuth;
    Button deleteAcc, savePass, verifyAcc;
    EditText newPass, newPass1;
    Switch aSwitch;
    TextView username,verifyTextVar;
    public static final String PREFS = "examplePrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        verifyAcc = findViewById(R.id.verifyButton);
        username = findViewById(R.id.profileName);
        verifyTextVar = findViewById(R.id.verifyText);
        deleteAcc = findViewById(R.id.deleteAccount);
        savePass = findViewById(R.id.savePassword);
        newPass = findViewById(R.id.passChange1);
        newPass1 = findViewById(R.id.passChange2);
        aSwitch = findViewById(R.id.notificationSwitch);
        mAuth = FirebaseAuth.getInstance();
                FirebaseUser
                user = mAuth.getCurrentUser();
                if (user!= null){
                    Log.d(TAG,"USER");
                    username.setText(user.getDisplayName());
                    if (user.isEmailVerified()){
                        verifyTextVar.setText("Verified Account");
                        verifyAcc.setEnabled(false);
                        verifyAcc.setVisibility(View.GONE);
                        //verifyAcc.getBackground().setColorFilter(0x00FF0000, PorterDuff.Mode.MULTIPLY);
                    }
                }
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
        savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });
        verifyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

    }

    private void changePass() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String pass = newPass.getText().toString().trim();
        String pass1 = newPass1.getText().toString().trim();
        String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,15}$";

        if(pass.isEmpty()){
            newPass.setError("Field cannot be empty");
            newPass.requestFocus();
            return;
        }
        if (pass1.isEmpty()){
            newPass1.setError("Field cannot be empty");
            newPass1.requestFocus();
            return;
        }
        if (pass.length() < 8 || pass.length() > 15){
            newPass.setError("Password must be at least 8 characters");
            newPass.requestFocus();
            return;
        }

        if (pass1.length() < 8 || pass1.length() > 15) {
            newPass1.setError("Password must be at least 8 characters");
            newPass1.requestFocus();
            return;
        }
        if(!pass.matches(regexp)){
            newPass.setError("Password is not strong");
            newPass.requestFocus();
            return;
        }
        if(!pass1.equals(pass)){
            Toast.makeText(getApplicationContext(),"Password Doesn\'t match",Toast.LENGTH_LONG).show();
            return;
        }
        if(user != null)
        user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Password Updated Successfully", Toast.LENGTH_LONG).show();
                    newPass.setText(null);
                    newPass1.setText(null);
                }
            }
        });
    }

    public void deleteAccount() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
// Alert for deletion
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Are you sure, You want to Delete?");
        alertDialogBuilder.setCancelable(false);


        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //finish();
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Log.d(TAG, "User account deleted.");
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),Welcome.class));
                                        Toast.makeText(getApplicationContext(),"Successfully Deleted your Account",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        //end of alert box


    }
    public void verify() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),"Email sent",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Settings.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            finish();
            startActivity(new Intent(this,Welcome.class));
        }
    }
}


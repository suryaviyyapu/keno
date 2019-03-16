package com.bl4k3.keno;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class Settings extends AppCompatActivity {
    private static final String TAG = "Settings";
    Switch aSwitch;
    FirebaseAuth mAuth;
    Button deleteAcc, savePass;
    EditText newPass, newPass1;
    TextView username;
    public static final String PREFS = "examplePrefs";
    PendingIntent notifyIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        username = findViewById(R.id.profileName);
        deleteAcc = findViewById(R.id.deleteAccount);
        savePass = findViewById(R.id.savePassword);
        newPass = findViewById(R.id.passChange1);
        newPass1 = findViewById(R.id.passChange2);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            username.setText(name);
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
        aSwitch = findViewById(R.id.switch1);

        //saving switch state
        SharedPreferences sharedPrefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        aSwitch.setChecked(sharedPrefs.getBoolean("Notifications", true));

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notification();
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                    editor.putBoolean("Notifications", true);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"ON",Toast.LENGTH_LONG).show();
                } else if (!isChecked){
                    //cancel();
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                    editor.putBoolean("Notifications", false);
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"For Now you cannot stop Notifications",Toast.LENGTH_LONG).show();
                }
                //end
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
        }

        if (pass1.length() < 8 || pass.length() > 15) {
            newPass1.setError("Password must be at least 8 characters");
            newPass1.requestFocus();
        }
        if(!pass.matches(regexp)){
            newPass.setError("Password is not strong");
            newPass.requestFocus();
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

    public void notification(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext()
                , 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
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
    public void cancel(){
        Intent notificationIntent = new Intent(Settings.this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext()
                , 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(notifyIntent);
        Log.v(TAG,"Cancel Notification");
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


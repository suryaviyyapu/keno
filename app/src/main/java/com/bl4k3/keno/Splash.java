package com.bl4k3.keno;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends Activity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.imageView);
        int SPLASH_TIME_OUT = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    finish();
                    Intent intent = new Intent(Splash.this, Keno.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    finish();
                    startActivity(new Intent(Splash.this,Welcome.class));
                }
                //startActivity(new Intent(Splash.this, Welcome.class));
                //finish();
            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
}


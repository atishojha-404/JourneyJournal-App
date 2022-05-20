package com.atish.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.atish.journeyjournal.Auth.RegisterActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemClock.sleep(1000);
        Intent signupIntent = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(signupIntent);
        finish();
    }
}
package com.ap.classattendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ap.classattendanceapp.ui.activities.AdminActivity;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
package com.ap.classattendanceapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.ui.fragments.CalendarFragment;
import com.ap.classattendanceapp.ui.fragments.ClassesFragment;
import com.ap.classattendanceapp.ui.fragments.CoursesFragment;
import com.ap.classattendanceapp.ui.fragments.HomeFragment;
import com.ap.classattendanceapp.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    HomeFragment homeFragment = new HomeFragment();
    CoursesFragment coursesFragment = new CoursesFragment();
    ClassesFragment classesFragment = new ClassesFragment();
    CalendarFragment calendarFragment = new CalendarFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        bottomNav = findViewById(R.id.studentBottomNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
                return true;
            }else if(itemId == R.id.courses){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, coursesFragment).commit();
                return true;
            } else if(itemId == R.id.classes){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, classesFragment).commit();
                return true;
            } else if(itemId == R.id.calendar){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, calendarFragment).commit();
                return true;
            } else if(itemId == R.id.profile){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, profileFragment).commit();
                return true;
            }

            return false;
        });
    }
}
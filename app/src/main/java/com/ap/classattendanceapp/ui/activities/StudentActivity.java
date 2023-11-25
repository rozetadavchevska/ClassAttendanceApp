package com.ap.classattendanceapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.ui.fragments.CalendarFragment;
import com.ap.classattendanceapp.ui.fragments.StudentClassesFragment;
import com.ap.classattendanceapp.ui.fragments.StudentCoursesFragment;
import com.ap.classattendanceapp.ui.fragments.StudentHomeFragment;
import com.ap.classattendanceapp.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    StudentHomeFragment homeFragment = new StudentHomeFragment();
    StudentCoursesFragment coursesFragment = new StudentCoursesFragment();
    StudentClassesFragment classesFragment = new StudentClassesFragment();
    CalendarFragment calendarFragment = new CalendarFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        bottomNav = findViewById(R.id.studentBottomNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, homeFragment).commit();

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, homeFragment).commit();
                return true;
            }else if(itemId == R.id.courses){
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, coursesFragment).commit();
                return true;
            } else if(itemId == R.id.classes){
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, classesFragment).commit();
                return true;
            } else if(itemId == R.id.calendar){
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, calendarFragment).commit();
                return true;
            } else if(itemId == R.id.profile){
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, profileFragment).commit();
                return true;
            }

            return false;
        });
    }
}
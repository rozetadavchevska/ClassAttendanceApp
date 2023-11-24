package com.ap.classattendanceapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.ui.fragments.AttendanceFragment;
import com.ap.classattendanceapp.ui.fragments.ClassesFragment;
import com.ap.classattendanceapp.ui.fragments.CoursesFragment;
import com.ap.classattendanceapp.ui.fragments.HomeFragment;
import com.ap.classattendanceapp.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    HomeFragment homeFragment = new HomeFragment();
    CoursesFragment coursesFragment = new CoursesFragment();
    ClassesFragment classesFragment = new ClassesFragment();
    AttendanceFragment attendanceFragment = new AttendanceFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        bottomNav = findViewById(R.id.teacherBottomNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.teacherFrameLayout, homeFragment).commit();

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.teacherFrameLayout, homeFragment).commit();
                return true;
            }else if(itemId == R.id.courses){
                getSupportFragmentManager().beginTransaction().replace(R.id.teacherFrameLayout, coursesFragment).commit();
                return true;
            } else if(itemId == R.id.classes){
                getSupportFragmentManager().beginTransaction().replace(R.id.teacherFrameLayout, classesFragment).commit();
                return true;
            } else if(itemId == R.id.attendance){
                getSupportFragmentManager().beginTransaction().replace(R.id.teacherFrameLayout, attendanceFragment).commit();
                return true;
            } else if(itemId == R.id.profile){
                getSupportFragmentManager().beginTransaction().replace(R.id.teacherFrameLayout, profileFragment).commit();
                return true;
            }

            return false;
        });
    }
}
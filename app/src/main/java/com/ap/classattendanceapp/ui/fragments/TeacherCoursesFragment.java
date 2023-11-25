package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ap.classattendanceapp.R;

public class TeacherCoursesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_courses, container, false);

        Button addCourse = view.findViewById(R.id.buttonAddCourse);
        Button viewCourses = view.findViewById(R.id.buttonViewCourses);


        addCourse.setOnClickListener(v -> {
            AddNewCourseFragment addNewCourseFragment = new AddNewCourseFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.teacherFrameLayout, addNewCourseFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        viewCourses.setOnClickListener(v -> {
            ViewCoursesFragment viewCoursesFragment = new ViewCoursesFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.teacherFrameLayout, viewCoursesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}
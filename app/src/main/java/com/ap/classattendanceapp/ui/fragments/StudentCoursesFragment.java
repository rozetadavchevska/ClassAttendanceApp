package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ap.classattendanceapp.R;

public class StudentCoursesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_courses, container, false);

        ImageButton allCourses = view.findViewById(R.id.allCourses);

        allCourses.setOnClickListener(v -> {
            ViewCoursesFragment viewCoursesFragment = new ViewCoursesFragment();
            FragmentManager fragment = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragment.beginTransaction();
            fragmentTransaction.replace(R.id.studentFrameLayout, viewCoursesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}
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

public class TeacherHomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachers_home, container, false);

        Button addCourse = view.findViewById(R.id.dashAddCourse);
        Button addClass = view.findViewById(R.id.dashAddClass);
        Button addSurvey = view.findViewById(R.id.dashAddSurvey);
        Button checkReviews = view.findViewById(R.id.dashCheckReviews);
        Button analysis = view.findViewById(R.id.dashAnalysis);

        addCourse.setOnClickListener(v -> {
            AddNewCourseFragment addNewCourse = new AddNewCourseFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.teacherFrameLayout, addNewCourse);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        addClass.setOnClickListener(v -> {
            AddClassFragment addNewClass = new AddClassFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.teacherFrameLayout, addNewClass);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        addSurvey.setOnClickListener(v -> {

        });

        checkReviews.setOnClickListener(v -> {

        });

        analysis.setOnClickListener(v -> {

        });

        return view;
    }
}
package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ap.classattendanceapp.R;

public class ViewReviewsFragment extends Fragment {
    String classId;
    String className;
    String courseName;

    public static ViewReviewsFragment newInstance(String currentClassId, String currentClassName, String currentCourseName) {
        ViewReviewsFragment fragment = new ViewReviewsFragment();
        Bundle args = new Bundle();
        args.putString("classId", currentClassId);
        args.putString("className", currentClassName);
        args.putString("courseName", currentCourseName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_reviews, container, false);

        Bundle args = getArguments();
        if (args != null) {
            classId = args.getString("classId");
            className = args.getString("className");
            courseName = args.getString("courseName");
        }


        return view;
    }
}
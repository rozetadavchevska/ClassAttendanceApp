package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ap.classattendanceapp.R;

public class AddNewCourseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_course, container, false);

        ImageButton backBtn = view.findViewById(R.id.addCourseBack);

        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });


        return view;
    }
}
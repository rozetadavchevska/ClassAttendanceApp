package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.adapters.StudentsAttendingAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfirmStudentAttendedFragment extends Fragment {
    String classId;
    String className;
    String courseName;
    Map<String, Boolean> studentsIds;
    private List<String> studentsList;
    private StudentsAttendingAdapter adapter;

    public static ConfirmStudentAttendedFragment newInstance(String currentClassId, String currentClassName, String currentCourseName, Map<String, Boolean> studentId) {
        ConfirmStudentAttendedFragment fragment = new ConfirmStudentAttendedFragment();
        Bundle args = new Bundle();
        args.putString("classId", currentClassId);
        args.putString("className", currentClassName);
        args.putString("courseName", currentCourseName);
        args.putSerializable("studentsId", (Serializable) studentId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_student_attended, container, false);

        ImageButton backBtn = view.findViewById(R.id.attendingBack);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        Bundle args = getArguments();
        if (args != null) {
            classId = args.getString("classId");
            className = args.getString("className");
            courseName = args.getString("courseName");
            Serializable mapStudents = args.getSerializable("studentsId");
            if (mapStudents instanceof Map) {
                studentsIds = (Map<String, Boolean>) mapStudents;
                studentsList = new ArrayList<>();

                for (Map.Entry<String, Boolean> entry : studentsIds.entrySet()) {
                    studentsList.add(entry.getKey());
                }
            }
        }

        RecyclerView studentsAttending = view.findViewById(R.id.studentAttending);
        adapter = new StudentsAttendingAdapter(studentsList, classId);
        studentsAttending.setLayoutManager(new LinearLayoutManager(requireContext()));
        studentsAttending.setAdapter(adapter);

        return view;
    }
}
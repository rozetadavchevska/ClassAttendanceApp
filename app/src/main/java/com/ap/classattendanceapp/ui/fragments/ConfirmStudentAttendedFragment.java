package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ap.classattendanceapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfirmStudentAttendedFragment extends Fragment {
    String classId;
    String className;
    String courseName;
    Map<String, Boolean> studentsIds;
    String studentId;
    Boolean isPresent;

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

        Bundle args = getArguments();
        if (args != null) {
            classId = args.getString("classId");
            className = args.getString("className");
            courseName = args.getString("courseName");
            Serializable mapStudents = args.getSerializable("studentsId");
            if (mapStudents instanceof Map) {
                studentsIds = (Map<String, Boolean>) mapStudents;

                List<String> studentIdsList = new ArrayList<>();
                List<Boolean> attendanceList = new ArrayList<>();

                for (Map.Entry<String, Boolean> entry : studentsIds.entrySet()) {
                    studentIdsList.add(entry.getKey());
                    attendanceList.add(entry.getValue());
                }
            }
        }

        return view;
    }
}
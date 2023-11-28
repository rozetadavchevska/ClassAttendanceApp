package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewCourseFragment extends Fragment {
    private EditText name;
    private EditText description;
    private Spinner courseSpinner;
    private List<DataSnapshot> teachersList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_course, container, false);

        ImageButton backBtn = view.findViewById(R.id.addCourseBack);
        Button addCourse = view.findViewById(R.id.addCourseBtn);
        name = view.findViewById(R.id.courseName);
        description = view.findViewById(R.id.courseDescription);
        courseSpinner = view.findViewById(R.id.spinnerCourse);

        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        getTeachers();

        addCourse.setOnClickListener(v -> addCourse());

        return view;
    }

    private void getTeachers(){

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("users");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teachersList = new ArrayList<>();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String userType = dataSnapshot.child("userType").getValue(String.class);
                    if("Teacher".equals(userType)){
                        teachersList.add(dataSnapshot);
                    }
                }
                if(courseSpinner != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getTeachersNames());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addCourse(){
        String courseName = name.getText().toString();
        String courseDescription = description.getText().toString();
        String teacherSpinner = (String) courseSpinner.getSelectedItem();

        if(courseName.isEmpty() || courseDescription.isEmpty() || teacherSpinner.isEmpty()){
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("courses");
        String courseId = dataRef.push().getKey();

        if(courseId != null){
            DataSnapshot selectedTeacherSnapshot = teachersList.get(courseSpinner.getSelectedItemPosition());
            String teacherId = selectedTeacherSnapshot.getKey();


            Course newCourse = new Course(courseId, courseName, courseDescription, new HashMap<>(), new HashMap<>(), new HashMap<>());
            dataRef.child(courseId).setValue(newCourse)
                    .addOnSuccessListener(v -> {
                        enrollTeacherToCourse(teacherId, courseId);
                        addTeacherToCourse(teacherId, courseId);
                        Toast.makeText(getActivity(), "Successfully added course", Toast.LENGTH_SHORT).show();

                        clearInputFields();
                    })
                    .addOnFailureListener(v -> Toast.makeText(getActivity(), "Error: " + v.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private List<String> getTeachersNames() {
        List<String> teacherNames = new ArrayList<>();
        for (DataSnapshot teacherSnapshot : teachersList) {
            String teacherFirstName = teacherSnapshot.child("userFirstName").getValue(String.class);
            String teacherLastName = teacherSnapshot.child("lastName").getValue(String.class);
            String teacherFullName = teacherFirstName + " " + teacherLastName;
            teacherNames.add(teacherFullName);
        }
        return teacherNames;
    }

    private void enrollTeacherToCourse(String teacherId, String courseId){
        DatabaseReference dataRefUsers = FirebaseDatabase.getInstance().getReference("users");
        dataRefUsers.child(teacherId).child("coursesId").child(courseId).setValue(true);
    }

    private void addTeacherToCourse(String teacherId, String courseId){
        DatabaseReference dataRefCourses = FirebaseDatabase.getInstance().getReference("courses");
        dataRefCourses.child(courseId).child("teacherId").child(teacherId).setValue(true);
    }

    private void clearInputFields(){
        name.getText().clear();
        description.getText().clear();
        courseSpinner.setSelection(0);
    }
}
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
import android.widget.TextView;
import android.widget.Toast;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddClassFragment extends Fragment {
    private EditText className;
    private EditText classDescription;
    private EditText classDate;
    private EditText classTime;
    private Spinner coursesSpinner;
    private TextView currentTeacher;
    private String currentTeacherId;
    private List<DataSnapshot> coursesList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_class, container, false);

        ImageButton backBtn = view.findViewById(R.id.addClassBack);
        className = view.findViewById(R.id.className);
        classDescription = view.findViewById(R.id.classDescription);
        classDate = view.findViewById(R.id.classDate);
        classTime = view.findViewById(R.id.classTime);
        coursesSpinner = view.findViewById(R.id.coursesSpinner);
        currentTeacher = view.findViewById(R.id.teacherText);

        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        currentTeacher();
        getCourses();


        Button addNewClass = view.findViewById(R.id.addClassBtn);

        addNewClass.setOnClickListener(v -> addClass());

        return view;
    }

    private void getCourses(){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("courses");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursesList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Only add courses that the current teacher teaches
                    List<String> teachers = dataSnapshot.child("teacherId").getValue(new GenericTypeIndicator<List<String>>() {});
                    if (teachers != null && teachers.contains(currentTeacherId)) {
                        coursesList.add(dataSnapshot);
                    }
                }

                if(coursesSpinner != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, getCoursesNames());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    coursesSpinner.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<String> getCoursesNames(){
        List<String> coursesNames = new ArrayList<>();
        for (DataSnapshot dataSnapshot : coursesList) {
            String courseName = dataSnapshot.child("name").getValue(String.class);
            coursesNames.add(courseName);
        }
        return coursesNames;
    }

    private void currentTeacher(){
        currentTeacherId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentTeacherRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentTeacherId);
        currentTeacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.child("userType").getValue(String.class).equals("Teacher")) {
                    String teacherFirstName = dataSnapshot.child("userFirstName").getValue(String.class);
                    String teacherLastName = dataSnapshot.child("lastName").getValue(String.class);
                    String teacherFullName = teacherFirstName + " " + teacherLastName;

                    currentTeacher.setText(teacherFullName);
                    getCourses();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void addClass() {
        String name = className.getText().toString();
        String description = classDescription.getText().toString();
        String date = classDate.getText().toString();
        String time = classTime.getText().toString();
        String selectedCourseName = (String) coursesSpinner.getSelectedItem();

        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classes");
        String classId = classRef.push().getKey();

        if (name.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty() || selectedCourseName.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        DataSnapshot selectedCourseSnapshot = coursesList.get(coursesSpinner.getSelectedItemPosition());
        String courseId = selectedCourseSnapshot.getKey();

        Class newClass = new Class(classId, name, description, date, time, currentTeacherId, courseId, new HashMap<>());
        if (classId != null) {
            classRef.child(classId).setValue(newClass)
                    .addOnSuccessListener(aVoid -> {
                        updateCourseWithClass(courseId, classId);
                        updateTeacherWithClass(currentTeacherId, classId);

                        Toast.makeText(getActivity(), "Class added successfully", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getActivity(), "Failed adding class" , Toast.LENGTH_SHORT).show();
        }
    }


    private void updateCourseWithClass(String courseId, String classId) {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses");
        courseRef.child(courseId).child("classesIds").child(classId).setValue(true);
    }

    private void updateTeacherWithClass(String currentTeacherId, String classId) {
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("users");
        teacherRef.child(currentTeacherId).child("classesIds").child(classId).setValue(true);
    }

    private void clearInputFields() {
        className.getText().clear();
        classDescription.getText().clear();
        classDate.getText().clear();
        classTime.getText().clear();
    }
}
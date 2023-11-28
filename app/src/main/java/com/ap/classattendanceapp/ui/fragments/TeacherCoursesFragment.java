package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.adapters.MyCoursesAdapter;
import com.ap.classattendanceapp.data.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherCoursesFragment extends Fragment {
    private List<Course> coursesList;
    private MyCoursesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_courses, container, false);

        ImageButton addCourse = view.findViewById(R.id.buttonAddCourse);
        ImageButton viewCourses = view.findViewById(R.id.buttonViewCourses);


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

        RecyclerView recyclerView = view.findViewById(R.id.teacherCoursesRecycler);
        coursesList = new ArrayList<>();
        adapter = new MyCoursesAdapter(coursesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        getCoursesFromDatabase();

        return view;
    }

    private void getCoursesFromDatabase(){
        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("courses");
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursesList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Course course = dataSnapshot.getValue(Course.class);
                    if(course != null){
                        coursesList.add(course);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
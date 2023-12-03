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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentCoursesFragment extends Fragment {
    private List<Course> coursesList;
    private List<Course> enrolledCourses;
    private MyCoursesAdapter adapter;
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

        RecyclerView recyclerView = view.findViewById(R.id.studentCourses);
        coursesList = new ArrayList<>();

        getCoursesFromDatabase();

        adapter = new MyCoursesAdapter(enrolledCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getCoursesFromDatabase(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();



        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("courses");
        enrolledCourses = new ArrayList<>();
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursesList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Course course = dataSnapshot.getValue(Course.class);

                        if(course != null){
                            coursesList.add(course);
                            checkUserEnrolled(coursesList, currentUserId);
                        }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserEnrolled(List<Course> fullCoursesList, String userId){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("coursesId");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enrolledCourses.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String enrolledCourseId = dataSnapshot.getKey();
                    for (Course course : fullCoursesList) {
                        if (course.getCourseId().equals(enrolledCourseId)) {
                            enrolledCourses.add(course);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
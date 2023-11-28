package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.adapters.CoursesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ap.classattendanceapp.data.models.Course;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCoursesFragment extends Fragment {
    private List<Course> coursesList;
    private CoursesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_courses, container, false);

        ImageButton backBtn = view.findViewById(R.id.viewCoursesBack);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        RecyclerView recyclerView = view.findViewById(R.id.coursesListView);

        coursesList = new ArrayList<>();
        adapter = new CoursesAdapter(coursesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        retrieveCoursesFromDatabase();

        return view;
    }

    private void retrieveCoursesFromDatabase() {
        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("courses");
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coursesList.clear();

                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course != null) {
                        coursesList.add(course);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
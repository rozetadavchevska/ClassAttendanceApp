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
import android.widget.Button;
import android.widget.TextView;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.adapters.TeacherHomeAdapter;
import com.ap.classattendanceapp.data.models.Class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeacherHomeFragment extends Fragment {
    private List<Class> currentClassesList;
    private FragmentManager fragmentManager;
    private TeacherHomeAdapter adapter;
    TextView currentClassMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachers_home, container, false);

        Button addCourse = view.findViewById(R.id.dashAddCourse);
        Button addClass = view.findViewById(R.id.dashAddClass);

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

        fragmentManager = getParentFragmentManager();
        currentClassesList = new ArrayList<>();
        RecyclerView currentClass = view.findViewById(R.id.currentClassTeacher);
        adapter = new TeacherHomeAdapter(currentClassesList, fragmentManager);

        currentClass.setLayoutManager(new LinearLayoutManager(requireContext()));
        currentClass.setAdapter(adapter);

        currentClassMessage = view.findViewById(R.id.currentClassMessage);

        getClassesFromDatabase();

        return view;
    }

    private void getClassesFromDatabase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("coursesId");
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> courseIds = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String courseId = dataSnapshot.getKey();
                    courseIds.add(courseId);
                }

                for (String courseId : courseIds) {
                    DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId).child("classesIds");
                    classRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                                String classId = classSnapshot.getKey();
                                DatabaseReference classDetailsRef = FirebaseDatabase.getInstance().getReference("classes").child(classId);
                                classDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot classDetailsSnapshot) {
                                        Class classData = classDetailsSnapshot.getValue(Class.class);
                                        if (classData != null) {
                                            separateClassesByTime(classData);
                                            adapter.notifyDataSetChanged();

                                            if (adapter.getItemCount() == 0) {
                                                currentClassMessage.setVisibility(View.VISIBLE);
                                            } else {
                                                currentClassMessage.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {}
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void separateClassesByTime(Class classData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String classDateTimeString = classData.getDate() + ", " + classData.getTime();
        LocalDateTime classDateTime = LocalDateTime.parse(classDateTimeString, formatter);
        LocalDateTime classEndTime = classDateTime.plusMinutes(45);

        if (currentDateTime.isAfter(classDateTime) && currentDateTime.isBefore(classEndTime)) {
            currentClassesList.add(classData);
        }

        Collections.sort(currentClassesList, (c1, c2) -> {
            LocalDateTime time1 = parseDateTime(c1.getDate(), c1.getTime(), formatter);
            LocalDateTime time2 = parseDateTime(c2.getDate(), c2.getTime(), formatter);
            return time1.compareTo(time2);
        });
    }

    private LocalDateTime parseDateTime(String date, String time, DateTimeFormatter formatter) {
        String dateTimeString = date + ", " + time;
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
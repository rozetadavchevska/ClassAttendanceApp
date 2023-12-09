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

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.adapters.ClassAttendanceAdapter;
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

public class AttendanceFragment extends Fragment {
    List<Class> classesList;
    private ClassAttendanceAdapter adapter;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        fragmentManager = getParentFragmentManager();
        classesList = new ArrayList<>();
        adapter = new ClassAttendanceAdapter(classesList, fragmentManager);
        RecyclerView attendedClasses = view.findViewById(R.id.attendanceList);
        attendedClasses.setLayoutManager(new LinearLayoutManager(requireContext()));
        attendedClasses.setAdapter(adapter);

        getClasses();

        return view;
    }

    private void getClasses(){
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
                                            orderClassesByTime(classData);
                                            adapter.notifyDataSetChanged();
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

    private void orderClassesByTime(Class classData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String classDateTimeString = classData.getDate() + ", " + classData.getTime();
        LocalDateTime classDateTime = LocalDateTime.parse(classDateTimeString, formatter);

        if (classDateTime.isBefore(currentDateTime)) {
            classesList.add(classData);
        }

        Collections.sort(classesList, (c1, c2) -> {
            LocalDateTime time1 = parseDateTime(c1.getDate(), c1.getTime(), formatter);
            LocalDateTime time2 = parseDateTime(c2.getDate(), c2.getTime(), formatter);
            return time2.compareTo(time1);
        });
    }

    private LocalDateTime parseDateTime(String date, String time, DateTimeFormatter formatter) {
        String dateTimeString = date + ", " + time;
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
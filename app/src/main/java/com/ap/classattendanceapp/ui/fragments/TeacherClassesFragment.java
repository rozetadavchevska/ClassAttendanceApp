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
import com.ap.classattendanceapp.data.adapters.TeacherClassesAdapter;
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

public class TeacherClassesFragment extends Fragment {
    private List<Class> classesList;
    private List<Class> upcomingClassesList;
    private List<Class> pastClassesList;
    private TeacherClassesAdapter upcomingAdapter;
    private TeacherClassesAdapter pastAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_classes, container, false);

        ImageButton addClass = view.findViewById(R.id.addNewClass);

        addClass.setOnClickListener(v -> {
            AddClassFragment addClassFragment = new AddClassFragment();
            FragmentManager fragment = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragment.beginTransaction();
            fragmentTransaction.replace(R.id.teacherFrameLayout, addClassFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        RecyclerView upcomingClasses = view.findViewById(R.id.upcomingClassView);
        RecyclerView pastClasses = view.findViewById(R.id.pastClassView);

        classesList = new ArrayList<>();
        upcomingClassesList = new ArrayList<>();
        pastClassesList = new ArrayList<>();

        upcomingAdapter = new TeacherClassesAdapter(upcomingClassesList);
        pastAdapter = new TeacherClassesAdapter(pastClassesList);

        upcomingClasses.setLayoutManager(new LinearLayoutManager(requireContext()));
        upcomingClasses.setAdapter(upcomingAdapter);

        pastClasses.setLayoutManager(new LinearLayoutManager(requireContext()));
        pastClasses.setAdapter(pastAdapter);

        getClassesFromDatabase();

        return view;
    }

    private void getClassesFromDatabase() {
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classes");
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classesList.clear();
                upcomingClassesList.clear();
                pastClassesList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Class classData = dataSnapshot.getValue(Class.class);
                    if (classData != null) {
                        // Check if the class belongs to the current teacher's course
                        if (isTeacherEnrolledInCourse(classData)) {
                            classesList.add(classData);
                            separateClassesByTime(classData);
                        }
                    }
                }

                upcomingAdapter.notifyDataSetChanged();
                pastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private boolean isTeacherEnrolledInCourse(Class classData) {
        String teacherId = classData.getTeacherId();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentTeacherId = currentUser.getUid();

        return currentTeacherId.equals(teacherId);
    }

    private void separateClassesByTime(Class classData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String classDateTimeString = classData.getDate() + ", " + classData.getTime();
        LocalDateTime classDateTime = LocalDateTime.parse(classDateTimeString, formatter);

        if (classDateTime.isBefore(currentDateTime)) {
            pastClassesList.add(classData);
        } else {
            upcomingClassesList.add(classData);
        }

        // Sort upcoming classes by time
        Collections.sort(upcomingClassesList, (c1, c2) -> {
            LocalDateTime time1 = parseDateTime(c1.getDate(), c1.getTime(), formatter);
            LocalDateTime time2 = parseDateTime(c2.getDate(), c2.getTime(), formatter);
            return time1.compareTo(time2);
        });

        // Sort past classes in descending order (from closest to farthest)
        Collections.sort(pastClassesList, (c1, c2) -> {
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
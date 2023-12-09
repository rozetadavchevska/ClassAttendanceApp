package com.ap.classattendanceapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Class;
import com.ap.classattendanceapp.ui.fragments.StudentClassesFragment;
import com.ap.classattendanceapp.ui.fragments.StudentCoursesFragment;
import com.ap.classattendanceapp.ui.fragments.StudentHomeFragment;
import com.ap.classattendanceapp.ui.fragments.ProfileFragment;
import com.ap.classattendanceapp.ui.receivers.NotificationReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private String currentUserId;
    private List<Class> upcomingClassesList;

    BottomNavigationView bottomNav;
    StudentHomeFragment homeFragment = new StudentHomeFragment();
    StudentCoursesFragment coursesFragment = new StudentCoursesFragment();
    StudentClassesFragment classesFragment = new StudentClassesFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, homeFragment).commit();

        upcomingClassesList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        currentUserId = currentUser.getUid();

        bottomNav = findViewById(R.id.studentBottomNav);

        getClassesFromDatabase();

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, homeFragment).commit();
                return true;
            } else if (itemId == R.id.courses) {
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, coursesFragment).commit();
                return true;
            } else if (itemId == R.id.classes) {
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, classesFragment).commit();
                return true;
            } else if (itemId == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, profileFragment).commit();
                return true;
            }

            return false;
        });
    }

    private void getClassesFromDatabase() {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("coursesId");
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> courseIds = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String courseId = dataSnapshot.getKey();
                    courseIds.add(courseId);
                }

                upcomingClassesList.clear();

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
                                        }
                                        setUpNotifications();
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

        LocalDateTime classDateTime = parseDateTime(classData.getDate(), classData.getTime(), formatter);

        if(classDateTime.isAfter(currentDateTime)){
            upcomingClassesList.add(classData);
        }

        Collections.sort(upcomingClassesList, (c1, c2) -> {
            LocalDateTime time1 = parseDateTime(c1.getDate(), c1.getTime(), formatter);
            LocalDateTime time2 = parseDateTime(c2.getDate(), c2.getTime(), formatter);
            return time1.compareTo(time2);
        });

    }

    private LocalDateTime parseDateTime(String date, String time, DateTimeFormatter formatter) {
        String dateTimeString = date + ", " + time;
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private void setUpNotifications() {
         for (Class classModel: upcomingClassesList) {
             scheduleNotificationAlarm(classModel);
         }
    }

    @SuppressLint({"ScheduleAlarm", "ScheduleExactAlarm"})
    private void scheduleNotificationAlarm(Class classModel) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("className", classModel.getName());

        int notificationId = generateUniqueId(classModel);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
        LocalDateTime classDateTime = parseDateTime(classModel.getDate(), classModel.getTime(), formatter);
        LocalDateTime notificationTime = classDateTime.minusHours(2);
        long triggerAtMillis = notificationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if (notificationTime.isAfter(LocalDateTime.now())) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    private static int generateUniqueId(Class classModel) {
        String uniqueId = classModel.getName() + classModel.getDate() + classModel.getTime();
        return uniqueId.hashCode();
    }
}
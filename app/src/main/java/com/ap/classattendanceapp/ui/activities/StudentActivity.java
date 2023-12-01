package com.ap.classattendanceapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Class;
import com.ap.classattendanceapp.ui.fragments.StudentClassesFragment;
import com.ap.classattendanceapp.ui.fragments.StudentCoursesFragment;
import com.ap.classattendanceapp.ui.fragments.StudentHomeFragment;
import com.ap.classattendanceapp.ui.fragments.ProfileFragment;
//import com.ap.classattendanceapp.ui.receivers.NotificationChannelHelper;
//import com.ap.classattendanceapp.ui.receivers.NotificationReceiver;
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
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentActivity extends AppCompatActivity {
//    private FirebaseAuth auth;
//    private FirebaseUser currentUser;
//    private String currentUserId;
//    private List<Class> upcomingClassesList;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//    private LocationManager locationManager;

    BottomNavigationView bottomNav;
    StudentHomeFragment homeFragment = new StudentHomeFragment();
    StudentCoursesFragment coursesFragment = new StudentCoursesFragment();
    StudentClassesFragment classesFragment = new StudentClassesFragment();
    //    CalendarFragment calendarFragment = new CalendarFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, homeFragment).commit();
//        upcomingClassesList = new ArrayList<>();

//        auth = FirebaseAuth.getInstance();
//        currentUser = auth.getCurrentUser();
//        currentUserId = currentUser.getUid();

        bottomNav = findViewById(R.id.studentBottomNav);
//        requestLocationPermissions();
//
//        getClassesFromDatabase();


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
//            } else if(itemId == R.id.calendar){
//                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, calendarFragment).commit();
//                return true;
            } else if (itemId == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.studentFrameLayout, profileFragment).commit();
                return true;
            }

            return false;
        });

    }

//    private void requestLocationPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            getLocation();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLocation();
//            } else {
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void getLocation() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        } else {
//        }
//    }
//
//    private void getClassesFromDatabase() {
//        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("coursesId");
//        courseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<String> courseIds = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String courseId = dataSnapshot.getKey();
//                    courseIds.add(courseId);
//                }
//
//                for (String courseId : courseIds) {
//                    DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId).child("classesIds");
//                    classRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot classSnapshot : snapshot.getChildren()) {
//                                String classId = classSnapshot.getKey();
//                                DatabaseReference classDetailsRef = FirebaseDatabase.getInstance().getReference("classes").child(classId);
//                                classDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot classDetailsSnapshot) {
//                                        Class classData = classDetailsSnapshot.getValue(Class.class);
//                                        if (classData != null) {
//                                            separateClassesByTime(classData);
//                                        }
//
//
//
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {}
//                                });
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {}
//                    });
//
//                }
//                setUpNotifications();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });
//    }
//
//    private void separateClassesByTime(Class classData) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        String classDateTimeString = classData.getDate() + ", " + classData.getTime();
//        LocalDateTime classDateTime = LocalDateTime.parse(classDateTimeString, formatter);
//
//        if(classDateTime.isAfter(currentDateTime)){
//            upcomingClassesList.add(classData);
//        }
//
//        Collections.sort(upcomingClassesList, (c1, c2) -> {
//            LocalDateTime time1 = parseDateTime(c1.getDate(), c1.getTime(), formatter);
//            LocalDateTime time2 = parseDateTime(c2.getDate(), c2.getTime(), formatter);
//            return time1.compareTo(time2);
//        });
//
//    }
//
//    private LocalDateTime parseDateTime(String date, String time, DateTimeFormatter formatter) {
//        String dateTimeString = date + ", " + time;
//        return LocalDateTime.parse(dateTimeString, formatter);
//    }
//
//    private void setUpNotifications() {
//         for (Class classModel: upcomingClassesList) {
//             setNotification(classModel);
//         }
//    }
//
//    private void setNotification(Class classModel) {
//        if (classModel != null) {
//            Intent intent = new Intent(this, NotificationReceiver.class);
//            intent.putExtra("className", classModel.getName());
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                    this,
//                    generateUniqueId(),
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//            );
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
//            LocalDateTime classDateTime = parseDateTime(classModel.getDate(), classModel.getTime(), formatter);
//            LocalDateTime currentDateTime = LocalDateTime.now();
//            LocalDateTime notificationTime = classDateTime.minusHours(2);
//
//            long hoursDifference = ChronoUnit.HOURS.between(currentDateTime, classDateTime);
//            long desiredNotificationTime = 2;
//
//            if (hoursDifference <= desiredNotificationTime) {
//                Notification notification = new NotificationCompat.Builder(this, NotificationChannelHelper.getChannelId())
//                        .setContentTitle("Class Reminder")
//                        .setContentText("Your class " + classModel.getName() + " starts in 2 hours!")
//                        .setSmallIcon(R.drawable.ic_notification)
//                        .setContentIntent(pendingIntent)
//                        .setWhen(notificationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
//                        .build();
//
//                notificationManager.notify(generateUniqueId(), notification);
//            }
//        }
//    }
//
//    private static int generateUniqueId() {
//        return (int) System.currentTimeMillis();
//    }
//    @Override
//    protected void onDestroy() {
//        if (locationManager != null) {
//            locationManager = null;
//        }
//        super.onDestroy();
//    }
}
package com.ap.classattendanceapp.ui.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Attendance;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ConfirmAttendanceFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final double maxDistanceMeters = 250.0;
    private double universityLatitude = 42.00496;
    private double universityLongitude =  21.40832;
    Button confirmAttendance;
    TextView className;
    TextView courseName;
    TextView teacherName;
    String classNameText;
    String courseNameText;
    String teacherNameText;
    String currentUserId;
    String studentName;
    String classId;

    public static ConfirmAttendanceFragment newInstance(String currentClassId, String className, String coursesName, String teachersName){
        ConfirmAttendanceFragment fragment = new ConfirmAttendanceFragment();
        Bundle args = new Bundle();
        args.putString("classId", currentClassId);
        args.putString("className", className);
        args.putString("courseName", coursesName);
        args.putString("teacherName", teachersName);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_attendance, container, false);

        ImageButton backBtn = view.findViewById(R.id.attendBack);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        className = view.findViewById(R.id.classAttend);
        courseName = view.findViewById(R.id.courseAttend);
        teacherName = view.findViewById(R.id.teacherClass);
        confirmAttendance = view.findViewById(R.id.confirmAttendBtn);

        requestLocation();

        Bundle args = getArguments();
        if (args != null) {
            classId = args.getString("classId", "Class Id");
            classNameText = args.getString("className", "Unknown Course");
            courseNameText = args.getString("courseName", "Unknown Course");
            teacherNameText = args.getString("teacherName", "Unknown Teacher");

            if (classNameText != null) {
                updateInformation(classNameText, courseNameText, teacherNameText);
            }
        }

        getCurrentUserName();

        return view;
    }

    private void updateInformation(String classNameText, String courseNameText, String teacherNameText) {
        className.setText(classNameText);
        courseName.setText(courseNameText);
        teacherName.setText(teacherNameText);
    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            retrieveLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrieveLocation();
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void retrieveLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            onLocationReceived(location.getLatitude(), location.getLongitude());
                        } else {
                            Toast.makeText(requireContext(), "Location is null", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Error getting location", Toast.LENGTH_SHORT).show();
                    });
        } catch (SecurityException e) {
            requestLocation();
        }
    }

    private void onLocationReceived(double userLatitude, double userLongitude) {
        double distance = calculateDistance(userLatitude, userLongitude, universityLatitude, universityLongitude);

        if (distance <= maxDistanceMeters) {
            confirmAttendance.setOnClickListener(v -> attendanceConfirmation());
        } else {
            Toast.makeText(requireContext(), "You are not at the university.", Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula for calculating distance between two points on the Earth
        double R = 6371; // Earth radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void getCurrentUserName(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        currentUserId = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String studentFirstName = snapshot.child("userFirstName").getValue(String.class);
                    String studentLastName = snapshot.child("lastName").getValue(String.class);
                    if(studentFirstName != null && studentLastName != null){
                        studentName = studentFirstName + " " + studentLastName;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void attendanceConfirmation() {
        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance");
        String attendanceId = attendanceRef.push().getKey();

        Attendance attendance = new Attendance(attendanceId, teacherNameText,studentName,classNameText,courseNameText, true);
        if(attendanceId != null){
            attendanceRef.child(attendanceId).setValue(attendance)
                    .addOnSuccessListener(v -> {
                        updateStudentAttendedClasses(classId, currentUserId);
                        updateClassWithStudentsAttended(currentUserId, classId);
                        Toast.makeText(getActivity(), "Attendance confirmed", Toast.LENGTH_SHORT).show();
                        confirmAttendance.setText("Confirmed");
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getActivity(), "Failed confirming attendance" , Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStudentAttendedClasses(String attendedClassId, String studentId){
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("users");
        studentRef.child(studentId).child("attendedClasses").child(attendedClassId).setValue(true);
    }

    private void updateClassWithStudentsAttended(String studentId, String attendedClassId){
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("classes");
        studentRef.child(attendedClassId).child("studentIds").child(studentId).setValue(true);
    }
}
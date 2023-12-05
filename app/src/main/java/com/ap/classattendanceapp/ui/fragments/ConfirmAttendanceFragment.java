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
import android.widget.Toast;

import com.ap.classattendanceapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class ConfirmAttendanceFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final double maxDistanceMeters = 250.0;
    private double universityLatitude = 42.00496;
    private double universityLongitude =  21.40832;
    Button confirmAttendance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_attendance, container, false);

        ImageButton backBtn = view.findViewById(R.id.attendBack);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        confirmAttendance = view.findViewById(R.id.confirmAttendBtn);

        requestLocation();

        return view;
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
            confirmAttendance.setOnClickListener(v -> {

            });
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
}
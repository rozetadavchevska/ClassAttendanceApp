package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddReviewFragment extends Fragment {
    String classNameText;
    String courseNameText;
    String teacherId;
    String teacherNameText;
    String currentUserId;
    String studentName;
    String classId;
    String selectedTime;
    String selectedOrganization;
    String selectedImportance;
    EditText commentText;
    Boolean isClassAttended;
    Button leaveReview;

    public static AddReviewFragment newInstance(String currentClassId, String className, String coursesName, String teachersId, String teachersName){
        AddReviewFragment fragment = new AddReviewFragment();
        Bundle args = new Bundle();
        args.putString("classId", currentClassId);
        args.putString("className", className);
        args.putString("courseName", coursesName);
        args.putString("teacherId", teachersId);
        args.putString("teacherName", teachersName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);

        ImageButton backBtn = view.findViewById(R.id.leaveReviewBack);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        RadioGroup timeAndPace = view.findViewById(R.id.timeAndPace);
        RadioGroup orgAndPresentation = view.findViewById(R.id.orgAndPresentation);
        RadioGroup importantMaterial = view.findViewById(R.id.importantMaterial);

        Bundle args = getArguments();
        if(args != null){
            classId = args.getString("classId", "Class Id");
            classNameText = args.getString("className", "Unknown Course");
            courseNameText = args.getString("courseName", "Unknown Course");
            teacherId = args.getString("teacherId", "Teacher Id");
            teacherNameText = args.getString("teacherName", "Unknown Teacher");
        }

        getCurrentUser();

        leaveReview = view.findViewById(R.id.leaveReviewBtn);
        leaveReview.setOnClickListener(v -> addReview(timeAndPace, orgAndPresentation, importantMaterial, view));

        return view;
    }

    private void getCurrentUser(){
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
                        ifStudentAttended(snapshot.child("attendedClasses"));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void ifStudentAttended(DataSnapshot attendedClasses){
        for(DataSnapshot classSnapshot : attendedClasses.getChildren()){
            String attendedClassId = classSnapshot.getKey();
            if (attendedClassId != null && attendedClassId.equals(classId)) {
                isClassAttended = true;
            }
        }
    }

    private void addReview (RadioGroup timeAndPace, RadioGroup orgAndPresentation, RadioGroup importance, View view){
        int selectedTimeBtnId = timeAndPace.getCheckedRadioButtonId();
        RadioButton selectedTimeBtn = view.findViewById(selectedTimeBtnId);
        selectedTime = selectedTimeBtn.getText().toString();

        int orgBtnId = orgAndPresentation.getCheckedRadioButtonId();
        RadioButton selectedOrgBtn = view.findViewById(orgBtnId);
        selectedOrganization = selectedOrgBtn.getText().toString();

        int importantBtnId = importance.getCheckedRadioButtonId();
        RadioButton selectedImportanceBtn = view.findViewById(importantBtnId);
        selectedImportance = selectedImportanceBtn.getText().toString();

        commentText = view.findViewById(R.id.commentText);
        String comment = commentText.getText().toString();

        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
        String reviewId = reviewsRef.push().getKey();

        Review review = new Review(reviewId, selectedTime, selectedOrganization, selectedImportance, comment, classId, classNameText, teacherId, teacherNameText, courseNameText);
        if(reviewId != null) {
            reviewsRef.child(reviewId).setValue(review)
                    .addOnSuccessListener(v -> {
                        updateTeacherWithReview(reviewId, teacherId);
                        Toast.makeText(getActivity(), "Review sent", Toast.LENGTH_SHORT).show();
                        leaveReview.setText("Review sent");
                        leaveReview.setEnabled(false);
                    })
                    .addOnFailureListener(v -> Toast.makeText(getActivity(), "Review not sent", Toast.LENGTH_SHORT).show());
        }
    }

    private void updateTeacherWithReview(String reviewId, String teacherClassId) {
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("users");
        teacherRef.child(teacherClassId).child("reviewIds").child(reviewId).setValue(true);
    }
}
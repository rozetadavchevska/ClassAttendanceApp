package com.ap.classattendanceapp.data.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
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
import java.util.Map;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
    private List<Course> coursesList;

    public CoursesAdapter(List<Course> coursesList) {
        this.coursesList = coursesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = coursesList.get(position);
        Map<String,Boolean> teacherId = course.getTeacherId();
        Course selectedCourse = coursesList.get(holder.getAdapterPosition());
        retrieveTeacherName(selectedCourse.getCourseId(), teacherId, holder.teacherName);
        holder.courseName.setText(course.getName());
        holder.courseDescription.setText(course.getDescription());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        isUserEnrolled(currentUserId, course.getCourseId(), holder);

        holder.enrollButton.setOnClickListener(v -> {
            String buttonText = holder.enrollButton.getText().toString();

            if("Enroll".equals(buttonText)){
                enrollToCourse(currentUserId, course.getCourseId());
            } else if("Unenroll".equals(buttonText)){
                unenrollFromCourse(currentUserId, course.getCourseId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView courseDescription;
        TextView teacherName;
        Button enrollButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.courseNameCard);
            courseDescription = itemView.findViewById(R.id.CourseDesCard);
            teacherName = itemView.findViewById(R.id.teacherNameCard);
            enrollButton = itemView.findViewById(R.id.enrollBtn);
        }
    }

    private void retrieveTeacherName(String courseId, Map<String, Boolean> teacherIds, TextView teacherName) {
        DatabaseReference teachersReference = FirebaseDatabase.getInstance().getReference("users");
        teacherName.setText("");
        List<String> enrolledTeacherNames = new ArrayList<>();

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId).child("teacherId");

        for (Map.Entry<String, Boolean> entry : teacherIds.entrySet()) {
            String teacherId = entry.getKey();
            courseRef.child(teacherId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        teachersReference.child(teacherId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String firstName = snapshot.child("userFirstName").getValue(String.class);
                                    String lastName = snapshot.child("lastName").getValue(String.class);

                                    if (firstName != null && lastName != null) {
                                        String teacherFullName = firstName + " " + lastName;
                                        enrolledTeacherNames.add(teacherFullName);
                                        updateTeacherNames(teacherName, enrolledTeacherNames);
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

    private void updateTeacherNames(TextView teacherName, List<String> enrolledTeacherNames) {
        String namesString = TextUtils.join(", ", enrolledTeacherNames);
        teacherName.setText(namesString);
    }

    private boolean isUserEnrolled(String currentUserId, String courseId, ViewHolder holder){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isEnrolled;
                if (snapshot.exists()) {
                    DataSnapshot coursesIdsSnapshot = snapshot.child("coursesId");
                    isEnrolled = coursesIdsSnapshot.hasChild(courseId);
                } else {
                    isEnrolled = false;
                }

                holder.itemView.post(() -> {
                    if (isEnrolled) {
                        holder.enrollButton.setText("Unenroll");
                    } else {
                        holder.enrollButton.setText("Enroll");
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.itemView.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        });
        return false;
    }

    private void enrollToCourse(String currentUserId, String courseId){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userType = snapshot.child("userType").getValue(String.class);

                    if("Student".equals(userType)){
                        userRef.child("coursesId").child(courseId).setValue(true);
                        courseRef.child("studentsIds").child(currentUserId).setValue(true);
                        notifyDataSetChanged();
                    } else if ("Teacher".equals(userType)) {
                        userRef.child("coursesId").child(courseId).setValue(true);
                        courseRef.child("teacherId").child(currentUserId).setValue(true);
                        notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void unenrollFromCourse(String userId, String courseId){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);

        userRef.child("coursesId").child(courseId).removeValue();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userType = snapshot.child("userType").getValue(String.class);

                    if("Student".equals(userType)){
                        courseRef.child("studentsIds").child(userId).removeValue();
                        notifyDataSetChanged();
                    } else if ("Teacher".equals(userType)) {
                        courseRef.child("teacherId").child(userId).removeValue();
                        notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
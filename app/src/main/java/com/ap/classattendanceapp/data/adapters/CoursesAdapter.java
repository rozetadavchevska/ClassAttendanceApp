package com.ap.classattendanceapp.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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
        String teacherId = course.getTeacherId().get(0);
        retrieveTeacherName(teacherId, holder.teacherName);

        holder.courseName.setText(course.getName());
        holder.courseDescription.setText(course.getDescription());


        holder.enrollButton.setOnClickListener(v -> {
            // Handle enrollment button click if needed
            // You can access the selected course using coursesList.get(position)
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

    private void retrieveTeacherName(String teacherId, TextView teacherName) {
        // Create a reference to the Teachers node in Firebase
        DatabaseReference teachersReference = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve teacher's information using the teacherId
        teachersReference.child(teacherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Assuming your Teacher model has fields for first and last name
                    String firstName = snapshot.child("userFirstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);

                    // Display the teacher's name in the TextView
                    String teacherFullName = firstName + " " + lastName;
                    teacherName.setText(teacherFullName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
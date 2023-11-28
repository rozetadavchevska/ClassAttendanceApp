package com.ap.classattendanceapp.data.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.ViewHolder> {
    private List<Course> courseList;
    public MyCoursesAdapter(List<Course> coursesList){
        this.courseList = coursesList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_courses,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        Map<String,Boolean> teacherId = course.getTeacherId();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        userIsEnrolled(currentUserId,course.getCourseId(), holder, course,teacherId);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView courseDescription;
        TextView teacherName;
        boolean isEnrolled;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.myCourseName);
            courseDescription = itemView.findViewById(R.id.myCourseDes);
            teacherName = itemView.findViewById(R.id.myTeacherNameCard);
        }
    }

    private void userIsEnrolled(String currentUserId, String courseId, ViewHolder holder, Course course, Map<String,Boolean> teacherId){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isEnrolled;
                if(snapshot.exists()){
                    DataSnapshot coursesIdSnapshot = snapshot.child("coursesId");
                    isEnrolled = coursesIdSnapshot.hasChild(courseId);
                } else {
                    isEnrolled = false;
                }

                if (isEnrolled){
                    holder.isEnrolled = true;
                    holder.courseName.setText(course.getName());
                    holder.courseDescription.setText(course.getDescription());
                    getTeacherName(course.getCourseId(), teacherId, holder.teacherName);
                } else {
                    holder.isEnrolled = false;
                    holder.itemView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void getTeacherName(String courseId, Map<String, Boolean> teacherIds, TextView teacherName){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId).child("teacherId");
        teacherName.setText("");
        List<String> courseTeacher = new ArrayList<>();

        for(Map.Entry<String, Boolean> entry : teacherIds.entrySet()){
            String teacherId = entry.getKey();
            courseRef.child(teacherId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        userRef.child(teacherId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String firstName = snapshot.child("userFirstName").getValue(String.class);
                                    String lastName = snapshot.child("lastName").getValue(String.class);
                                    if(firstName != null && lastName != null){
                                        String teacherFullName = firstName + " " + lastName;
                                        courseTeacher.add(teacherFullName);
                                        String namesString = TextUtils.join(", ", courseTeacher);
                                        teacherName.setText(namesString);
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
}

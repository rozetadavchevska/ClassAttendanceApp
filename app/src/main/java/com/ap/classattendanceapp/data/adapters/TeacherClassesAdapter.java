package com.ap.classattendanceapp.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TeacherClassesAdapter extends RecyclerView.Adapter<TeacherClassesAdapter.ViewHolder> {

    private List<Class> classesList;
    public TeacherClassesAdapter(List<Class> classesList){
        this.classesList = classesList;
    }
    @NonNull
    @Override
    public TeacherClassesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_class,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherClassesAdapter.ViewHolder holder, int position) {
        Class classData = classesList.get(position);
        String teacherId = classData.getTeacherId();
        String courseId = classData.getCourseId();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentTeacherId = currentUser.getUid();

        if (currentTeacherId.equals(teacherId)) {
            holder.className.setText(classData.getName());
            holder.classDescription.setText(classData.getDescription());

            fetchCourseName(courseId, holder.classCourse);
            fetchTeacherName(teacherId, holder.classTeacher);

            if (classData.getDate() != null && !classData.getDate().isEmpty()) {
                holder.classDate.setText(classData.getDate());
            } else {
                holder.classDate.setText("No Date");
            }

            if (classData.getTime() != null && !classData.getTime().isEmpty()) {
                holder.classTime.setText(classData.getTime());
            } else {
                holder.classTime.setText("No Time");
            }
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
    }

    private void fetchCourseName(String courseId, TextView textView) {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String courseName = snapshot.child("name").getValue(String.class);
                    if (courseName != null) {
                        textView.setText(courseName);
                    } else {
                        textView.setText("Unknown Course");
                    }
                } else {
                    textView.setText("Unknown Course");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textView.setText("Unknown Course");
            }
        });
    }

    private void fetchTeacherName(String teacherId, TextView textView) {
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("users").child(teacherId);
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String teacherFirstName = snapshot.child("userFirstName").getValue(String.class);
                    String teacherLastName = snapshot.child("lastName").getValue(String.class);
                    if (teacherFirstName != null && teacherLastName != null) {
                        String teacherFullName = teacherFirstName + " " + teacherLastName;
                        textView.setText(teacherFullName);
                    } else {
                        textView.setText("Unknown Teacher");
                    }
                } else {
                    textView.setText("Unknown Teacher");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView className;
        TextView classDescription;
        TextView classCourse;
        TextView classTeacher;
        TextView classDate;
        TextView classTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.classNameCard);
            classDescription = itemView.findViewById(R.id.classDesCard);
            classCourse = itemView.findViewById(R.id.classCourse);
            classTeacher = itemView.findViewById(R.id.teacherNameCard);
            classDate = itemView.findViewById(R.id.classDate);
            classTime = itemView.findViewById(R.id.classTime);
        }
    }
}

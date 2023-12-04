package com.ap.classattendanceapp.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Class;
import com.ap.classattendanceapp.ui.fragments.AddReviewFragment;
import com.ap.classattendanceapp.ui.fragments.ConfirmAttendanceFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class StudentHomeAdapter extends RecyclerView.Adapter<StudentHomeAdapter.ViewHolder> {
    private List<Class> classesList;
    private FragmentManager fragmentManager;


    public StudentHomeAdapter(List<Class> classesList, FragmentManager fragmentManager){
        this.classesList = classesList;
        this.fragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attend_class, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class classItem = classesList.get(position);

        holder.className.setText(classItem.getName());

        if (classItem.getCourseId() != null) {
            fetchCourseName(classItem.getCourseId(), holder.classCourse);
        } else {
            holder.classCourse.setText("Unknown Course");
        }

        if (classItem.getTeacherId() != null) {
            fetchTeacherName(classItem.getTeacherId(), holder.classTeacher);
        } else {
            holder.classTeacher.setText("Unknown Teacher");
        }

        if (classItem.getDate() != null && !classItem.getDate().isEmpty()) {
            holder.classDate.setText(classItem.getDate());
        } else {
            holder.classDate.setText("No Date");
        }

        if (classItem.getTime() != null && !classItem.getTime().isEmpty()) {
            holder.classTime.setText(classItem.getTime());
        } else {
            holder.classTime.setText("No Time");
        }

        holder.itemView.setVisibility(View.VISIBLE);

        holder.reviewBtn.setOnClickListener(v -> {//if student has attend button clicked and until class end 45min
            if (fragmentManager != null) {
                AddReviewFragment addReviewFragment = new AddReviewFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.studentFrameLayout, addReviewFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        holder.attendBtn.setOnClickListener(v -> {
            if (fragmentManager != null) {
                ConfirmAttendanceFragment confirmAttendanceFragment = new ConfirmAttendanceFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.studentFrameLayout, confirmAttendanceFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        TextView classCourse;
        TextView classTeacher;
        TextView classDate;
        TextView classTime;
        Button attendBtn;
        Button reviewBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.classAttendName);
            classCourse = itemView.findViewById(R.id.classAttendCourse);
            classTeacher = itemView.findViewById(R.id.teacherNameAttend);
            classDate = itemView.findViewById(R.id.classAttendDate);
            classTime = itemView.findViewById(R.id.classAttendTime);
            attendBtn = itemView.findViewById(R.id.attendBtn);
            reviewBtn = itemView.findViewById(R.id.reviewBtn);
        }
    }
}

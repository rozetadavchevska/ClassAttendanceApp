package com.ap.classattendanceapp.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Class;
import com.ap.classattendanceapp.ui.fragments.ViewReviewsFragment;
import com.ap.classattendanceapp.ui.fragments.ViewStudentsAttendedFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ClassAttendanceAdapter extends RecyclerView.Adapter<ClassAttendanceAdapter.ViewHolder> {
    private List<Class> classesList;
    private FragmentManager fragmentManager;
    String currentCourseName;
    public ClassAttendanceAdapter(List<Class> classesList, FragmentManager fragmentManager){
        this.classesList = classesList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_students_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class classItem = classesList.get(position);

        if (classItem != null) {
            holder.className.setText(classItem.getName());

            if (classItem.getCourseId() != null) {
                fetchCourseName(classItem.getCourseId(), holder.courseName);
            } else {
                holder.courseName.setText("Unknown Course");
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
        }

        holder.viewStudentsBtn.setOnClickListener(v -> {
            if (fragmentManager != null) {
                ViewStudentsAttendedFragment viewStudentsAttendedFragment = ViewStudentsAttendedFragment.newInstance(classItem.getStudentIds());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.teacherFrameLayout, viewStudentsAttendedFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(v.getContext(), "No students at the moment", Toast.LENGTH_SHORT).show();
            }
        });

        holder.viewReviewsBtn.setOnClickListener(v -> {
            if (fragmentManager != null) {
                ViewReviewsFragment viewReviewsFragment = ViewReviewsFragment.newInstance(classItem.getClassId());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.teacherFrameLayout, viewReviewsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(v.getContext(), "No reviews at the moment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCourseName(String courseId, TextView textView) {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentCourseName = snapshot.child("name").getValue(String.class);
                    if (currentCourseName != null) {
                        textView.setText(currentCourseName);
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

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        TextView courseName;
        TextView classDate;
        TextView classTime;
        Button viewStudentsBtn;
        Button viewReviewsBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.className);
            courseName = itemView.findViewById(R.id.classCourse);
            classDate = itemView.findViewById(R.id.classDate);
            classTime = itemView.findViewById(R.id.classTime);
            viewStudentsBtn = itemView.findViewById(R.id.viewStudentsBtn);
            viewReviewsBtn = itemView.findViewById(R.id.viewReviewsBtn);
        }
    }
}

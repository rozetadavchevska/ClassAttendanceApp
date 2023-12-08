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
import com.ap.classattendanceapp.ui.fragments.ConfirmStudentAttendedFragment;
import com.ap.classattendanceapp.ui.fragments.ViewReviewsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class TeacherHomeAdapter extends RecyclerView.Adapter<TeacherHomeAdapter.ViewHolder> {
    private List<Class> classesList;
    private FragmentManager fragmentManager;
    String currentCourseName;
    public TeacherHomeAdapter(List<Class> classesList, FragmentManager fragmentManager){
        this.classesList = classesList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class classItem = classesList.get(position);

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

//        holder.itemView.setVisibility(View.VISIBLE);

        isCurrentClass(classItem, holder);
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

    public void isCurrentClass(Class classItem, ViewHolder holder){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String classDateTimeString = classItem.getDate() + ", " + classItem.getTime();
        LocalDateTime classDateTime = LocalDateTime.parse(classDateTimeString, formatter);
        LocalDateTime classEndTime = classDateTime.plusMinutes(45);

        if (currentDateTime.isAfter(classDateTime) && currentDateTime.isBefore(classEndTime)) {
            holder.confirmStudentsBtn.setOnClickListener(v -> {
                if (fragmentManager != null) {
                    ConfirmStudentAttendedFragment confirmStudentAttendedFragment = ConfirmStudentAttendedFragment.newInstance(classItem.getClassId(), classItem.getName(), currentCourseName, classItem.getStudentIds());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.teacherFrameLayout, confirmStudentAttendedFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            holder.viewReviewsBtn.setOnClickListener(v -> {
                if (fragmentManager != null) {
                    ViewReviewsFragment viewReviewsFragment = ViewReviewsFragment.newInstance(classItem.getClassId(), classItem.getName(), currentCourseName);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.teacherFrameLayout, viewReviewsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        TextView courseName;
        TextView classDate;
        TextView classTime;
        Button confirmStudentsBtn;
        Button viewReviewsBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.className);
            courseName = itemView.findViewById(R.id.classCourse);
            classDate = itemView.findViewById(R.id.classDate);
            classTime = itemView.findViewById(R.id.classTime);
            confirmStudentsBtn = itemView.findViewById(R.id.viewStudentsBtn);
            viewReviewsBtn = itemView.findViewById(R.id.viewReviewsBtn);
        }
    }
}

package com.ap.classattendanceapp.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StudentsAttendingAdapter extends RecyclerView.Adapter<StudentsAttendingAdapter.ViewHolder> {
    private List<String> studentsList;
    String studentFullName;
    String classId;
    public StudentsAttendingAdapter(List<String> studentsList, String classId){
        this.studentsList = studentsList;
        this.classId = classId;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_attends_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String studentId = studentsList.get(position);

        getStudentName(studentId, holder);

        holder.confirm.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Student attending", Toast.LENGTH_SHORT).show();
            holder.cancel.setEnabled(false);
        });

        holder.cancel.setOnClickListener(v -> {
            cancelAttendance(studentId, classId);
            Toast.makeText(v.getContext(), "Student not attending", Toast.LENGTH_SHORT).show();
            holder.confirm.setEnabled(false);
        });
    }

    private void getStudentName(String studentId, ViewHolder holder){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(studentId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String firstName = snapshot.child("userFirstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    if(firstName != null && lastName != null){
                        studentFullName = firstName + " " + lastName;
                        holder.studentName.setText(studentFullName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void cancelAttendance(String currentStudentId, String currentClassId){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentStudentId);
        usersRef.child("attendedClasses").child(currentClassId).removeValue();

        DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("classes").child(currentClassId);
        classesRef.child("studentIds").child(currentStudentId).removeValue();

        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference("attendance");
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String attendanceId = dataSnapshot.getKey();
                    String studentId = dataSnapshot.child("studentId").getValue(String.class);
                    String classesId = dataSnapshot.child("classId").getValue(String.class);
                    if (currentStudentId != null && currentClassId != null && currentStudentId.equals(studentId) && currentClassId.equals(classesId)) {
                        attendanceRef.child(attendanceId).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView studentName;
        ImageButton confirm;
        ImageButton cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.studentName);
            confirm = itemView.findViewById(R.id.confirmBtn);
            cancel = itemView.findViewById(R.id.cancelBtn);
        }
    }
}

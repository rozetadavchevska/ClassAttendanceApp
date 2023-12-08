package com.ap.classattendanceapp.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StudentNameAdapter extends RecyclerView.Adapter<StudentNameAdapter.ViewHolder> {
    private List<String> studentsList;
    public StudentNameAdapter(List<String> studentsList){
        this.studentsList = studentsList;
    }
    @NonNull
    @Override
    public StudentNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentNameAdapter.ViewHolder holder, int position) {
        String studentId = studentsList.get(position);
        getStudentName(studentId, holder);
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
                        String studentFullName = firstName + " " + lastName;
                        holder.studentName.setText(studentFullName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
        }
    }
}

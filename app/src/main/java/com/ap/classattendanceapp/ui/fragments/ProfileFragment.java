package com.ap.classattendanceapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.User;
import com.ap.classattendanceapp.ui.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private TextView firstName;
    private TextView lastName;
    private TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logout = view.findViewById(R.id.logoutBtn);
        firstName = view.findViewById(R.id.fNameProfile);
        lastName = view.findViewById(R.id.lNameProfile);
        email = view.findViewById(R.id.emailProfile);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        getUserDetails(uid);

        logout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        return view;
    }

    private void getUserDetails(String userId){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);

                    if(user != null){
                        firstName.setText(user.getUserFirstName());
                        lastName.setText(user.getLastName());
                        email.setText(user.getUserEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.ap.classattendanceapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.adapters.ReviewsAdapter;
import com.ap.classattendanceapp.data.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewReviewsFragment extends Fragment {
    String classId;
    private List<Review> reviewsList;
    private ReviewsAdapter adapter;

    public static ViewReviewsFragment newInstance(String currentClassId) {
        ViewReviewsFragment fragment = new ViewReviewsFragment();
        Bundle args = new Bundle();
        args.putString("classId", currentClassId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_reviews, container, false);

        ImageButton backBtn = view.findViewById(R.id.reviewBack);
        backBtn.setOnClickListener(v -> {
            FragmentManager fragment = getParentFragmentManager();
            fragment.popBackStack();
        });

        Bundle args = getArguments();
        if (args != null) {
            classId = args.getString("classId");
        }

        RecyclerView reviews = view.findViewById(R.id.reviewAnswers);
        reviewsList = new ArrayList<>();
        adapter = new ReviewsAdapter(reviewsList);
        reviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        reviews.setAdapter(adapter);

        getReviews();

        return view;
    }

    public void getReviews(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference("reviews");
        reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review reviewItem = reviewSnapshot.getValue(Review.class);
                    if (reviewItem != null) {
                        String teacherId = reviewItem.getTeacherId();
                        String currentClassId = reviewItem.getClassId();
                        if (currentUserId.equals(teacherId) && classId.equals(currentClassId)) {
                            reviewsList.add(reviewItem);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
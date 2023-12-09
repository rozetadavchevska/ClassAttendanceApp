package com.ap.classattendanceapp.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.data.models.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Review> reviewsList;

    public ReviewsAdapter(List<Review> reviewsList){
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_answers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewsList.get(position);

        holder.timePace.setText(review.getTimePace());
        holder.orgPresent.setText(review.getOrgPresentation());
        holder.importance.setText(review.getImportance());
        holder.comment.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timePace;
        TextView orgPresent;
        TextView importance;
        TextView comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timePace = itemView.findViewById(R.id.timePaceAnswer);
            orgPresent = itemView.findViewById(R.id.presentationAnswer);
            importance = itemView.findViewById(R.id.importanceAnswer);
            comment = itemView.findViewById(R.id.leaveCommentAnswer);
        }
    }
}

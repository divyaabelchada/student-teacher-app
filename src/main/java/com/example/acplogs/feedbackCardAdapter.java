package com.example.acplogs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class feedbackCardAdapter extends FirestoreRecyclerAdapter<AgencyModel, feedbackCardAdapter.feedbackCardHolder> {

    public feedbackCardAdapter(@NonNull FirestoreRecyclerOptions<AgencyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull feedbackCardAdapter.feedbackCardHolder holder,
                                    int position, @NonNull AgencyModel model) {

        holder.studentName.setText(model.getUserName());
        holder.studentFeedback.setText(model.getUserFeedback());
        holder.feedbackAdded_on.setText(model.getUpdated_on());

    }

    @NonNull
    @Override
    public feedbackCardAdapter.feedbackCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_cards,
                parent,false);
        return new feedbackCardAdapter.feedbackCardHolder(v);
    }

    class feedbackCardHolder extends RecyclerView.ViewHolder{

        TextView studentName,studentFeedback, feedbackAdded_on;

        public feedbackCardHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.studentName);
            studentFeedback= itemView.findViewById(R.id.studentFeedback);
            feedbackAdded_on = itemView.findViewById(R.id.data_added_on);
        }
    }
}



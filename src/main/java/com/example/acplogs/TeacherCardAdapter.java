package com.example.acplogs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TeacherCardAdapter extends FirestoreRecyclerAdapter<TeacherModel, TeacherCardAdapter.TeacherCardHolder> {

    public TeacherCardAdapter(@NonNull FirestoreRecyclerOptions<TeacherModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TeacherCardAdapter.TeacherCardHolder holder,
                                    int position, @NonNull TeacherModel model) {

        holder.teacherName.setText(model.getTeacher_name());
        holder.teacherContact.setText(model.getTeacher_contact());
        holder.teacherDept.setText(model.getTeacher_dept());

    }

    @NonNull
    @Override
    public TeacherCardAdapter.TeacherCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_cards,
                parent,false);
        return new TeacherCardAdapter.TeacherCardHolder(v);
    }

    class TeacherCardHolder extends RecyclerView.ViewHolder{

        TextView teacherName,teacherContact, teacherDept;

        public TeacherCardHolder(@NonNull View itemView) {
            super(itemView);

            teacherName = itemView.findViewById(R.id.studentName);
            teacherContact= itemView.findViewById(R.id.studentFeedback);
            teacherDept = itemView.findViewById(R.id.data_added_on);

        }
    }



}

package com.example.acplogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StudentFeedbacks extends AppCompatActivity {
    TextView noFeedbackTxt;

    RecyclerView feedbackRecyclerView;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    String agency_name;

    feedbackCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_feedbacks);

        noFeedbackTxt = (TextView) findViewById(R.id.noFeedbacktxt);
        noFeedbackTxt.setVisibility(View.INVISIBLE);

        feedbackRecyclerView =(RecyclerView)findViewById(R.id.feedbackRecyclerView);
        agency_name = getIntent().getStringExtra("agency_name");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupFeedbacks();

    }


    private void setupFeedbacks(){
        try {

            Query query = db.collection("AgencyData")
                    .document(agency_name)
                    .collection("Feedback");


            FirestoreRecyclerOptions<AgencyModel> options = new FirestoreRecyclerOptions.Builder<AgencyModel>()
                    .setQuery(query, AgencyModel.class)
                    .build();


            adapter = new feedbackCardAdapter(options);

            feedbackRecyclerView.setHasFixedSize(true);
            feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            feedbackRecyclerView.setAdapter(adapter);
            if(adapter.getItemCount()==0){
                noFeedbackTxt.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "error :" +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



}

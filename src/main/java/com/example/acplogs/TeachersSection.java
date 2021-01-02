package com.example.acplogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TeachersSection extends AppCompatActivity {

    RecyclerView teachersRecyclerView;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    TeacherCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_section);

        teachersRecyclerView = findViewById(R.id.teachersRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupTeachersList();
    }

    private void setupTeachersList(){
        try {

            Query query = db.collection("Teachers");


            FirestoreRecyclerOptions<TeacherModel> options = new FirestoreRecyclerOptions.Builder<TeacherModel>()
                    .setQuery(query, TeacherModel.class)
                    .build();


            adapter = new TeacherCardAdapter(options);

            teachersRecyclerView.setHasFixedSize(true);
            teachersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            teachersRecyclerView.setAdapter(adapter);

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
package com.example.acplogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StudentCorner extends AppCompatActivity {


    //for intent
    private String userId, certificateUrl;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference studentDataRef = db.collection("Students");

    private UserCardAdapter adapter;

    private RecyclerView studentData;

    String studentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_corner);

        studentData= (RecyclerView)findViewById(R.id.studentSection);

        studentClass = getIntent().getStringExtra("studentClass");
        //userId = getIntent().getStringExtra("studentUserId");



        setUpRecyclerView();

    }

    private void setUpRecyclerView(){

        try {

            Query query = studentDataRef.whereEqualTo("userClass", studentClass).orderBy("name", Query.Direction.ASCENDING);

            //db.collection("cities").whereEqualTo("capital", true);
            FirestoreRecyclerOptions<StudentModel> options = new FirestoreRecyclerOptions.Builder<StudentModel>()
                    .setQuery(query, StudentModel.class)
                    .build();

            adapter = new UserCardAdapter(options);

            studentData.setHasFixedSize(true);
            studentData.setLayoutManager(new LinearLayoutManager(this));
            studentData.setAdapter(adapter);

//--------------------------------------------------------------------------
            adapter.setOnItemClickListener(new UserCardAdapter.itemOnClickListener(){

                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    StudentModel model = documentSnapshot.toObject(StudentModel.class);

                    if(model!=null) {
                        userId = model.getUserId();
                        certificateUrl= model.getCertificate();


                        Intent intent = new Intent(StudentCorner.this, StudentProfile.class);
                        intent.putExtra("studentUserId", userId);
                        intent.putExtra("certificateUrl", certificateUrl);
                        startActivity(intent);
                    }
                    else
                        Log.i("Info","Student data not found");

                }
            });
//------------------------------ intent ---------------------------------------

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error:"+ e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            Log.i("Info", e.getMessage());
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
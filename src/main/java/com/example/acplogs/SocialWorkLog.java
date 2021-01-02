package com.example.acplogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

public class SocialWorkLog extends AppCompatActivity {

    RecyclerView studentCertis;

    private socialworkLogCardAdapter certiAdapter;

    private String userId,certificateUrl;

    //firebase
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference documentReference;

    FirebaseStorage firebaseStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_work_log);

        userId = getIntent().getStringExtra("studentUserId");
        certificateUrl = getIntent().getStringExtra("certificateUrl");


        studentCertis =(RecyclerView) findViewById(R.id.studentCertis);


        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        documentReference = db.collection("Students").document(userId);

        try{
            setUpRecyclerView();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }


    }

    private void setUpRecyclerView(){

        try {
            Query query = documentReference.collection("social_work");
            //Query query = db.collection("Students").document(userId).collection("social_work");

            FirestoreRecyclerOptions<StudentModel> options = new FirestoreRecyclerOptions.Builder<StudentModel>()
                    .setQuery(query, StudentModel.class)
                    .build();

            certiAdapter = new socialworkLogCardAdapter(options);

            studentCertis.setHasFixedSize(true);
            studentCertis.setLayoutManager(new LinearLayoutManager(this));
            studentCertis.setAdapter(certiAdapter);

            //------------adapter onclick listener
            certiAdapter.setOnItemClickListener(new socialworkLogCardAdapter.itemOnClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                    //Toast.makeText(getApplicationContext(),"feature updating",Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error:"+ e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            Log.i("Info", e.getLocalizedMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        certiAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        certiAdapter.stopListening();
    }

}
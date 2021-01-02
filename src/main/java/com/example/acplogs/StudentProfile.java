package com.example.acplogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {

    Button certificateButton, dailyLogsButton;

    CircleImageView studentProfile;

    TextView studentName, studentYear, studentEmail, studentContact,studentAddress, studentWorkHours, studentId;

    String student;

    int totalWorkHours = 0;

    private String userId,certificateUrl;

    //firebase
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference documentReference;

    FirebaseStorage firebaseStorage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);

        userId = getIntent().getStringExtra("studentUserId");
        certificateUrl = getIntent().getStringExtra("certificateUrl");



        //Textviews ----------------------------------------
        studentName =(TextView) findViewById(R.id.studentName);
        studentYear=(TextView)findViewById(R.id.studentYear);
        studentEmail=(TextView)findViewById(R.id.studentEmail);
        studentContact=(TextView)findViewById(R.id.studentContact);
        studentAddress=(TextView)findViewById(R.id.studentAddress);
        studentWorkHours=(TextView)findViewById(R.id.studentWorkHours);
        studentId=(TextView)findViewById(R.id.studentId);

        //-------------------------------------------------------------------------

        studentProfile = findViewById(R.id.studentProfile);
        //-------------------------------------------------------------------------

        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        documentReference = db.collection("Students").document(userId);


        try{
            setupProfile();
            calculateWorkHours();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error "+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }

        //certificateButton = (Button) findViewById(R.id.Certificates);

       /* certificateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //startActivity(new Intent(StudentProfile.this, SocialWorkLog.class));
                            Intent intent = new Intent(StudentProfile.this, SocialWorkLog.class);
                            intent.putExtra("studentUserId", mAuth.getUid());
                            startActivity(intent);
                        }
                   });;*/

        dailyLogsButton = (Button) findViewById(R.id.DailyLogs);

        dailyLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfile.this, SocialWorkLog.class);
                intent.putExtra("studentUserId",  userId);
                intent.putExtra("certificateUrl", certificateUrl);
                startActivity(intent);
            }
        });;


    }

    private void setupProfile(){

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();
                    if(document!=null){
                        student = document.getString("name");
                        studentName.setText(document.getString("name"));
                        String userYear = (document.getString("year"));
                        String userClass = (document.getString("userClass"));
                        studentYear.setText(userYear + userClass);
                        studentEmail.setText(document.getString("email"));
                        studentContact.setText(document.getString("contact"));
                        studentAddress.setText(document.getString("address"));
                        studentId.setText(document.getString("student_id"));
                        String imageUrl = document.getString("profile");

                        if(imageUrl == ""){
                            studentProfile.setImageResource(R.drawable.ic_profilepic);
                        }else{
                            try {
                                Picasso.get().load(imageUrl).centerCrop().resize(100, 100)
                                        .into(studentProfile, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.i("Info",e.getLocalizedMessage());
                                            }
                                        });
                            }
                            catch (Exception e){
                                Log.i("Info",e.getLocalizedMessage());
                            }
                        }
                    }

                    }
                else{
                    Log.i("Info", "Error " + task.getException().getLocalizedMessage());
                }
            }
        });


    }

    private void calculateWorkHours(){

        final ArrayList<Integer> workHours= new ArrayList<>();

        CollectionReference social_work_collection = documentReference.collection("social_work");
        social_work_collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d("Info", document.getId() + " => " + document.getString("name"));
                        int item = document.getLong("work_hours").intValue();
                        workHours.add(item);

                        if(!workHours.isEmpty()){
                            totalWorkHours= 0;
                            for(int i: workHours){
                                totalWorkHours += i;
                            }
                        }
                        studentWorkHours.setText(totalWorkHours+ " hours Completed");
                    }

                }
                else{
                    Log.i("Info", "Error " + task.getException().getLocalizedMessage());
                }

            }
        }); //onCompleteListener
    }

}


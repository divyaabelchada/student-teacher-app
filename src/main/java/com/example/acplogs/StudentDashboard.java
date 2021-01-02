package com.example.acplogs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentDashboard extends AppCompatActivity {

    //adding student Data
    private DocumentReference studentProfile;
    TextView text_one, text_two, text_three, text_four;
    CircleImageView studentProfilePic;


    //clickableTextView
    Button profile,agencies, dailyLogs, addToReport, teachers;


    ConstraintLayout baseConstraintLayout;
    Button addData ;

    //firebase
    private FirebaseFirestore db;
    private  CollectionReference socialWorkDataRef;
    private FirebaseAuth mAuth;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);


        //clickable Buttons
        profile =(Button)findViewById(R.id.viewProfile);
        agencies=(Button)findViewById(R.id.viewAgencies);
        dailyLogs =(Button)findViewById(R.id.dailyLogs);
        addToReport =(Button)findViewById(R.id.updateReport);
        teachers = findViewById(R.id.contactTeacher);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Updating this feature shortly!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentDashboard.this, StudentProfile.class);
                intent.putExtra("studentUserId", mAuth.getUid());
                startActivity(intent);
            }
        });

        agencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboard.this, AgencyInfo.class));
            }
        });

        /*certificates.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(StudentDashboard.this, SocialWorkLog.class);
                            intent.putExtra("studentUserId", mAuth.getUid());
                            startActivity(intent);
                        }
               });*/

        dailyLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(StudentDashboard.this, SocialWorkLog.class));
                Intent intent = new Intent(StudentDashboard.this, SocialWorkLog.class);
                intent.putExtra("studentUserId", mAuth.getUid());
                startActivity(intent);
            }
        });


        //opening link to google forms
        addToReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"will open map location",Toast.LENGTH_SHORT).show();
                try {
//                    Uri uri = Uri.parse(map_link); // missing 'http://' will cause crashed
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://forms.gle/XtvmjKRuCxKZysSk8"));
                    startActivity(intent);
                }
                catch (Exception e){
                    Log.i("Info",e.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), "Can't open link", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //------------------ add data -------------

        addData = (Button)findViewById(R.id.addDataButton);

        //setting on click listener for fabAddData---
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboard.this, AddingSocialWorkLog.class));
            }
        });

        teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboard.this, TeachersSection.class));
            }
        });

        //defining views and layouts-----------------
        baseConstraintLayout= (ConstraintLayout)findViewById(R.id.baseConstraintLayout);


        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        socialWorkDataRef = db.collection("Students").document(mAuth.getUid()).collection("social_work");


        studentProfilePic =(CircleImageView)findViewById(R.id.profile_image);

        studentProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentDashboard.this,UploadProfile.class));
            }
        });




        //set student profile
        text_one = (TextView) findViewById(R.id.student_name);
        text_two = (TextView) findViewById(R.id.student_class);
        text_three = (TextView) findViewById(R.id.student_email);
        text_four = (TextView) findViewById(R.id.student_contact);

        showStudentProfile();


        //setting actionbar
        //getSupportActionBar().setElevation(0);

        //swiping between activities
        baseConstraintLayout.setOnTouchListener(new OnSwipeTouchListener(StudentDashboard.this) {

            public void onSwipeTop(){
                startActivity(new Intent(StudentDashboard.this,StudentDashboard.class));
                finish();

            }
            public void onSwipeBottom() {
                startActivity(new Intent(StudentDashboard.this, AddingSocialWorkLog.class));
            }
            public void onSwipeRight() {
                startActivity(new Intent(StudentDashboard.this,AgencyInfo.class));
            }
            //required
            public void onSwipeLeft() {
                //startActivity(new Intent(StudentDashboard.this,StudentProfile.class));
                //Toast.makeText(getApplicationContext(), "Adding Shortly!", Toast.LENGTH_SHORT).show();
                //--- earlier you didn't give userId so there was an exception.
                Intent intent = new Intent(StudentDashboard.this, StudentProfile.class);
                intent.putExtra("studentUserId", mAuth.getUid());
                startActivity(intent);
            }

        });

    }

    private void showStudentProfile() {

        studentProfile = db.collection("Students").document(mAuth.getUid());
        studentProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                       text_one.setText(document.getString("name"));
                       text_two.setText(document.getString("year"));
                       text_three.setText(document.getString("email"));
                       text_four.setText(document.getString("contact"));
                       String imageUrl = document.getString("profile");

                       if(imageUrl == ""){
                           studentProfilePic.setImageResource(R.drawable.ic_profilepic);
                       }
                       else{
                           try {
                               Picasso.get().load(imageUrl).centerCrop().resize(90, 90).into(studentProfilePic ,
                                       new com.squareup.picasso.Callback() {
                                           @Override
                                           public void onSuccess() {

                                           }

                                           @Override
                                           public void onError(Exception e) {
                                               Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       });
                           }
                           catch (Exception e){
                               Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }
                    }
                }else
                    Toast.makeText(getApplicationContext(),"Error"+task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    //------------- end showStudentProfile---------------

    // ----------------menu code -------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case (R.id.logout):
                //Toast.makeText(UserLogin.this,"logout selected",Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Alert!")
                        .setMessage("Do you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code for logging out
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(StudentDashboard.this,"logged out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(StudentDashboard.this, LoginActivity.class));
                                finish();

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
               // startActivity(new Intent(StudentDashboard.this, UploadProfile.class));
            default:
                return false;
        }
    }
    //-------------- menu code end ------------------

    }




package com.example.acplogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class TeacherDashboard extends AppCompatActivity {


    //adding teacher Data
    private DocumentReference teacherProfile;

    TextView teacherName,teacherdept,teacherContact,teacherEmail, role;

    Button studentCorner, agencySection, addAgency, teachersSection;

// firestore
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        //firebase
        //firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        teacherName = (TextView) findViewById(R.id.teacherNameTextView);
        teacherdept = (TextView) findViewById(R.id.teacherDeptTextView);
        teacherContact = (TextView) findViewById(R.id.teacherContactTextView);
        teacherEmail = (TextView) findViewById(R.id.teacherEmailTextView);
        role = (TextView) findViewById(R.id.roleTextView);
        showTeacherProfile();

        //cards
        studentCorner = (Button)findViewById(R.id.studentCorner);
        agencySection = findViewById(R.id.agencySection);
        addAgency = findViewById(R.id.addAgency);
        teachersSection = findViewById(R.id.viewTeachers);


        //onclickListeners
        studentCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboard.this, AllStudents.class));
            }
        });

        agencySection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboard.this, AgencyInfo.class));
            }
        });

        addAgency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboard.this, AddAgencyData.class));
            }
        });

        teachersSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboard.this, TeachersSection.class));
            }
        });


        //opening link to google forms
        /*
        Reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"will open map location",Toast.LENGTH_SHORT).show();
                try {
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
        }); */
        //-------------------------------------------------------





    }


    private void showTeacherProfile(){

        teacherProfile = db.collection("Teachers").document(mAuth.getUid());
        teacherProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        teacherName.setText(document.getString("teacher_name"));
                        teacherdept.setText(document.getString("teacher_dept"));
                        teacherEmail.setText(document.getString("teacher_email"));
                        teacherContact.setText(document.getString("teacher_contact"));
                    }
                }else
                    Toast.makeText(getApplicationContext(),"Error"+task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


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
                                Toast.makeText(TeacherDashboard.this,"logged out", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TeacherDashboard.this, LoginActivity.class));
                                finish();

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            default:
                return false;
        }
    }
    //-------------- menu code end ------------------



}
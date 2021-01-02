package com.example.acplogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class TeacherSignUpActivity extends AppCompatActivity {

    EditText teacherName, teacherDept, teacherContact, teacherEmail, teacherPassword, teacherDob;
    String teacherNametxt,teacherDepttxt,teacherContacttxt,teacherEmailtxt,teacherDobtxt;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    Button teacherSignUpButton, securityCodeSubmit;

    //security code
    EditText securityCode ;
    CardView securityCard;
    int verificationCode = 2021;

    FirebaseAuth mAuth;

    //------- firestore object-----

    private FirebaseFirestore db;


    private Boolean validateContact(EditText value){
        String data;
        data = value.getText().toString().trim();
        if(data.length() != 10){
            value.setError("Invalid Contact");
            return false;
        }
        else{
            value.setError(null);
            return true;
        }
    }

    //----------validating data--------------
    private Boolean dataValidating(EditText value){
        String data;
        data = value.getText().toString().trim();

        if(data.isEmpty()){
            value.setError("Field can't be null");
            return false;
        }
        else{
            value.setError(null);
            return true;
        }
    }

    //------------ email validation -------------

    private boolean validateEmail(EditText userEmail){
        String emailTxt = userEmail.getText().toString().trim();
        if(emailTxt.isEmpty()){
            userEmail.setError("Field can't be null");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()){
            userEmail.setError("Invalid Email Address");
            return false;
        }
        else{
            userEmail.setError(null);
            return true;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_sign_up);

        getSupportActionBar().setElevation(0);

        //firestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //EditText
        teacherName = findViewById(R.id.teacherNameData);
        teacherDept =  findViewById(R.id.deptData);
        teacherContact = findViewById(R.id.teacherContactData);
        teacherEmail =  findViewById(R.id.teacherEmailData);
        teacherPassword = findViewById(R.id.teacherPasswordData);
        securityCode = findViewById(R.id.securityCode);
        securityCodeSubmit = findViewById(R.id.securityCodeSubmit);
        securityCard = (CardView) findViewById(R.id.askForTokenCard);
        teacherSignUpButton= findViewById(R.id.teacherSignUpBtn);


        securityCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCode();
            }
        });


        //on button click
        teacherSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addData();
            }
        });

    }

    private void checkCode(){
        int code = Integer.parseInt(securityCode.getText().toString());
        if(code == verificationCode){
            securityCard.setVisibility(View.INVISIBLE);
        }
        else
            makeText(getApplicationContext(), "Verification Failed !", LENGTH_SHORT).show();
    }

    private void addData(){

        if (!dataValidating(teacherName) || !dataValidating(teacherDept) || !validateContact(teacherContact) ||
                !dataValidating(teacherPassword) || !validateEmail(teacherEmail)) {
            makeText(getApplicationContext(), "Please fill Details Correctly", LENGTH_SHORT).show();
        }
        else {

            try {
                String password = teacherPassword.getText().toString();
                String email = teacherEmail.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            makeText(getApplicationContext(), task.getException().getLocalizedMessage(), LENGTH_LONG).show();
                        } else {
                            saveData();
                        }


                    }
                });
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"Error "+ e.getLocalizedMessage(), LENGTH_SHORT ).show();
            }

        }

    }


    private void saveData(){


        // Converting to String
        teacherNametxt = teacherName.getText().toString();
        teacherDepttxt = teacherDept.getText().toString();
        teacherContacttxt =teacherContact.getText().toString();
        teacherEmailtxt = teacherEmail.getText().toString();



        //mapping

       

        String date = Timestamp.now().toDate().toString();

        Map<String, Object> docData = new HashMap<>();
        docData.put("teacher_name",teacherNametxt);
        docData.put("Teacher", true); //extra
        docData.put("teacher_contact",teacherContacttxt);
        docData.put("dateExample", date); //extra
        docData.put("teacher_email", teacherEmailtxt);
        docData.put("teacher_dept", teacherDepttxt);
        docData.put("role", "Teacher");
        docData.put("userId", mAuth.getUid());

        //add data to firebase

        db.collection("Teachers").document(mAuth.getUid())
                .set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    makeText(getApplicationContext(), "Attempt FAILED"+ task.getException().getLocalizedMessage(), LENGTH_SHORT).show();
                }
                else{
                    makeText(getApplicationContext(), "Data Added", LENGTH_SHORT).show();
                }
            }
        });


    }


}
package com.example.acplogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.*;

public class StudentSignUpActivity extends AppCompatActivity {


    EditText userName, userContact, userEmail, userPassword, userAddress;
    Spinner userClass, userYear;

    String userNametxt, userClasstxt, userYearTxt, userContacttxt, userEmailtxt, userPasswordtxt, userAddresstxt;

    Button signUpButton;

    FirebaseAuth mAuth;

    //------- firestore object-----

    private FirebaseFirestore db;

    //----------validating contact-------------
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
    private boolean validateEmail(){
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
        setContentView(R.layout.student_sign_up_activity);

        getSupportActionBar().setElevation(0);

        //firestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //EditText
        userName = findViewById(R.id.nameData);
        userYear = (Spinner)findViewById(R.id.yearData);
        userClass = (Spinner)findViewById(R.id.classData);
        userEmail =  findViewById(R.id.emailData);
        userContact = findViewById(R.id.contactData);
        userPassword = findViewById(R.id.studentIdData);
        userAddress = findViewById(R.id.addressData);

        setUpClassSpinner();
        setUpYearSpinner();

        //spinner userBloodGrp =  findViewById(R.id.bloodGrpData);

        //buttons
        signUpButton= findViewById(R.id.signUpBtn);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

    }

    private void setUpClassSpinner(){
        // Spinner Drop down elements
        List<String> classes = new ArrayList<String>();
        classes.add("BSc");
        classes.add("BSc CS");
        classes.add("BSc BT");
        classes.add("BSc IT");
        classes.add("BA");
        classes.add("BCOM");
        classes.add("BMS");
        classes.add("BFM");
        classes.add("BAF");
        classes.add("BMM");
        classes.add("BBI");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,classes );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userClass.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        userClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userClasstxt = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }

    private void setUpYearSpinner(){
        // Spinner Drop down elements
        List<String> year = new ArrayList<String>();
        year.add("Third Year");
        year.add("Second Year");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,year );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userYear.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        userYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userYearTxt = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }


//--------- add data --------------
    private void addData(){

        if (!dataValidating(userName) || !dataValidating(userAddress) || !dataValidating(userPassword)
                || !validateContact(userContact) || !dataValidating(userAddress) || !validateEmail()) {
            makeText(getApplicationContext(), "Please fill Details Correctly", LENGTH_SHORT).show();
        }
        else {

          try {
              String email = userEmail.getText().toString();
              String password = userPassword.getText().toString();

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
        userNametxt = userName.getText().toString();
        userContacttxt =userContact.getText().toString();
        userEmailtxt = userEmail.getText().toString();
        userPasswordtxt = userPassword.getText().toString();
        userAddresstxt =userAddress.getText().toString();



        //mapping

        Map<String, Object> docData = new HashMap<>();
        docData.put("name",userNametxt);
        docData.put("Teacher", false); //extra
        docData.put("student_id", userPasswordtxt);
        docData.put("contact",userContacttxt );
        docData.put("updated_on", new Timestamp(new Date())); //extra
        docData.put("address", userAddresstxt);
        docData.put("email", userEmailtxt);
        docData.put("year", userYearTxt);
        docData.put("userClass", userClasstxt);
        docData.put("role", "Student");
        docData.put("profile","");
        docData.put("userId", mAuth.getUid());

        //add data to firebase

        db.collection("Students").document(mAuth.getUid())
                .set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    makeText(getApplicationContext(), "Attempt FAILED"+ task.getException().getLocalizedMessage(), LENGTH_SHORT).show();
                }
                else{
                    makeText(getApplicationContext(), "Student Enrolled", LENGTH_SHORT).show();
                }
            }
        });


    }
}

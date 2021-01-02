package com.example.acplogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    CardView intro;


    TextView forgotPassword, signUp, signUpTeacher;

    EditText email , password ;
    Button loginButton;

    String emailTxt , passwordTxt;



    //firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //firestore
    FirebaseFirestore db;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    //---------------- validate email -------------
    private boolean validateEmail(){
        emailTxt = email.getText().toString().trim();
        if(emailTxt.isEmpty()){
            email.setError("Field can't be null");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()){
            email.setError("Invalid Email Address");
            return false;
        }
        else{
            email.setError(null);
            return true;
        }

    }
    //---------------- validate password -----------
    private boolean validatePassword(){
        passwordTxt = password.getText().toString().trim();
        if(passwordTxt.isEmpty()){
            password.setError("Field can't be null");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }


    //------------- LOGIN FUNCTION -------------
    public void startLogin(){
        if(validateEmail() && validatePassword()){
            mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Sign in Failed",Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(StudentDashboard.this,Dashboard.class));
                    }

                }
            });


        }

    }//end of startSignup

    public void SignUp(){
        startActivity(new Intent(LoginActivity.this, StudentSignUpActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        intro = (CardView)findViewById(R.id.introduction);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intro.setVisibility(View.INVISIBLE);
            }
        },5000);

        //actionbar shadow eliminated.
        //getSupportActionBar().setElevation(0);

        db= FirebaseFirestore.getInstance();


        //auth state listener
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (!(firebaseAuth.getCurrentUser()== null)){
                    Toast.makeText(LoginActivity.this,"Sign In Successful",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(StudentDashboard.this, UserLogin.class));

                    //checking if student or Teacher
                    DocumentReference data = db.collection("Students").document(mAuth.getUid());
                    data.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("Info", "Document exists!");
                                    startActivity(new Intent(LoginActivity.this, StudentDashboard.class));
                                    finish();

                                } else {
                                    Log.d("Info", "Teacher Logged In");
                                    startActivity(new Intent(LoginActivity.this, TeacherDashboard.class));
                                    finish();
                                }
                            } else {
                                Log.d("Info", "Failed with: ", task.getException());
                            }
                        }
                    });

                }
            }
        }; // ------------- auth state listener----

// --------------------- DEFINING VIEWS ------------
        email = (EditText) findViewById(R.id.emailTxt);
        password = (EditText) findViewById(R.id.passwordTxt);
        loginButton = (Button)findViewById(R.id.loginButton);
        forgotPassword = (TextView)findViewById(R.id.forgotPasswordTxt);
        signUp = (TextView)findViewById(R.id.signUpTextView);
        signUpTeacher = (TextView)findViewById(R.id.teacherSignUpBtn);


        //-------------- login onclick------------
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

        //-------------- Forgot pass onclick------------
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAuth.sendPasswordResetEmail(email.toString().trim())
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d("Info", "Email sent.");
//                                }
//                            }
//                        });
                Toast.makeText(LoginActivity.this, "Enter your student ID",Toast.LENGTH_SHORT).show();
            }
        });

        //-------------- Login onclick------------
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        //------------- teacher signUp----------
        signUpTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, TeacherSignUpActivity.class));
            }
        });

    }


    }

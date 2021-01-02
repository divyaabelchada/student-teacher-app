package com.example.acplogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class AddingSocialWorkLog extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    //for uploading image
    private ProgressBar mProgressBar;
    private Uri mImageUri;

    //timer
    int oHour,oMin, cHour, cMin;

    private Spinner spinner;

    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseAuth mAuth;
    StorageReference mStorageReference;


    ImageView dailyLogPicture;


    String time;


    //agency data

    EditText DateEdt, workDescEdt,workHoursEdt,feedbackEdt, checkIn, checkOut;
    String agency_name, activityDate, workDesc,feedback, checkInTxt, checkOutTxt;
    int workHours;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    Button submitBtn;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_your_social_work_log);

        getSupportActionBar().setElevation(0);

        time = Timestamp.now().toString();

        //userData
        DateEdt = findViewById(R.id.dateData);
        workDescEdt = findViewById(R.id.descData);
        workHoursEdt= findViewById(R.id.workingHoursData);
        feedbackEdt= findViewById(R.id.feedbackData);

        //timeEditText
        checkIn = findViewById(R.id.startTimeData);
        checkOut = findViewById(R.id.leaveTimeData);

        // check in time picker start --------------------
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddingSocialWorkLog.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                oHour = hourOfDay;
                                oMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,oHour,oMin);
                                checkIn.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(oHour,oMin);
                timePickerDialog.show();

            }
        });

        //check in time picker end ---------------------------------------

        // check out time picker start --------------------

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddingSocialWorkLog.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                cHour = hourOfDay;
                                cMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,cHour,cMin);
                                checkOut.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(cHour,cMin);
                timePickerDialog.show();

            }
        });

        //check out time picker end ---------------------------------------


        DateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddingSocialWorkLog.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("Info", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                DateEdt.setText(date);
            }
        };

        //submitbtn
        submitBtn = findViewById(R.id.dataSubmitButton);


        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference("StudentSocialWork");
        collectionReference = db.collection("AgencyData");


        //for spinner ------------------
        spinner = findViewById(R.id.addAgencyData);
        addingDataToSpinner();



        //for uploading pictures
        mProgressBar=(ProgressBar)findViewById(R.id.certiUploadProgressBar);
        dailyLogPicture = (ImageView)findViewById(R.id.uploadPictureIcon);


        //for certificate--------------------------------------------------------
        dailyLogPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataValidating(DateEdt) && dataValidating(workDescEdt) &&
                        dataValidating(workHoursEdt) && uploadFile()
                        && !agency_name.equals("Choose an Agency....")){
                    savingUserData();
                    uploadFile();
                    if(!feedback.isEmpty()){
                        uploadingFeedbacks();
                    }
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill details", LENGTH_SHORT).show();

                }
            }
        });



    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).resize(400,300).centerCrop().into(dailyLogPicture);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private boolean uploadFile(){



        if(mImageUri!= null){
            StorageReference fileReference = mStorageReference.child(agency_name + time + getExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 1000);

                            //Toast.makeText(getApplicationContext(), "Upload Successful!", Toast.LENGTH_LONG).show();
                            Log.i("Info","Upload Successful!");

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("certificate",uri.toString());

                                    db.collection("Students").document(mAuth.getUid()).collection("social_work")
                                            .document(agency_name + time ).set(userData, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Info", "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Info", "Error writing document", e);
                                                }
                                            });
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int)progress);
                }
            });

            return  true;

        }else{
            Toast.makeText(getApplicationContext(), "No Image selected!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void addingDataToSpinner(){
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Choose an Agency....");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d("Info", document.getId() + " => " + document.getString("name"));
                        String item = (String) document.getString("agency_name");
                        assert item!=null;
                        arrayList.add(item);
                    }
                }
                else
                    Log.i("Info", "error" + task.getException().getLocalizedMessage());
            }
        });
        //end of adding items to arraylist


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   agency_name = parent.getItemAtPosition(position).toString();
                   //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,Toast.LENGTH_LONG).show();
                }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    private void savingUserData(){


        // Converting to String
        activityDate = DateEdt.getText().toString();
        workDesc =workDescEdt.getText().toString();
        workHours = Integer.parseInt(workHoursEdt.getText().toString());
        feedback = feedbackEdt.getText().toString();
        checkInTxt = checkIn.getText().toString();
        checkOutTxt = checkOut.getText().toString();


        //mapping

        Map<String, Object> docData = new HashMap<>();
        docData.put("added_on",Timestamp.now().toDate().toString()); //extra
        docData.put("agency_name",agency_name);
        docData.put("work_desc",workDesc );
        docData.put("work_hours", workHours);
        docData.put("date", activityDate);
        docData.put("feedback", feedback);
        docData.put("checkIn", checkInTxt);
        docData.put("checkOut", checkOutTxt);

        //add data to firebase

        db.collection("Students").document(mAuth.getUid())
                .collection("social_work").document(agency_name + time)
                .set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    makeText(getApplicationContext(), "Attempt FAILED"+ task.getException()
                            .getLocalizedMessage(), LENGTH_SHORT).show();
                }
                else{
                    makeText(getApplicationContext(), "Data Added!", LENGTH_SHORT).show();

                }
            }
        });


    }


    private void uploadingFeedbacks(){

        db.collection("Students").document(mAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String name = document.getString("name");
                    //mapping

                    Map<String, Object> feedbackData = new HashMap<>();
                    feedbackData.put("updated_on",Timestamp.now().toDate().toString());
                    feedbackData.put("userName", name);
                    feedbackData.put("userFeedback", feedback);
                    collectionReference.document(agency_name).collection("Feedback").document(name + time)
                            .set(feedbackData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.i("Info","added feedback----------------------");
                                finish();
                            }else{
                                Log.i("Info","FEEDBACK NOT ADDED------------------");

                            }

                        }
                    });



                }
            }
        });



    }

}

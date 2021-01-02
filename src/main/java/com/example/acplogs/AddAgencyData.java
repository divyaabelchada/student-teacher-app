package com.example.acplogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class AddAgencyData extends AppCompatActivity  {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ProgressBar mProgressBar;
    private Uri mImageUri;

    //timer
    int oHour,oMin, cHour, cMin;

    //firestore
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference mStorageReference;


    ImageView agencyPicture;

    EditText agency_name, agency_desc,agency_open,agency_close,agency_address,agency_location,agency_contact, map_link;
    String agency_name_txt, agency_desc_txt,agency_time_txt,agency_address_txt,
            agency_location_txt,agency_contact_txt, map_link_txt;

    Button dataSubmitButton;


    //----------validate contact
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agency_data);

        //firestore
        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference("AgencyImages");
        db = FirebaseFirestore.getInstance();

        //imageview
        agencyPicture = (ImageView) findViewById(R.id.uploadPictureIcon);
        mProgressBar=(ProgressBar)findViewById(R.id.imageUploadProgressBar);




        //defining edit text
        agency_name = findViewById(R.id.agencyNameData);
        agency_desc = findViewById(R.id.agencyDescData);
        agency_contact= findViewById(R.id.agencyContactData);
        agency_address= findViewById(R.id.agencyAddressData);
        agency_location=findViewById(R.id.agencyLocationData);
        agency_open= findViewById(R.id.openingTimeData);
        agency_close= findViewById(R.id.closingTimeData);
        map_link = findViewById(R.id.map_link_data);
        dataSubmitButton = findViewById(R.id.dataSubmitButton);

        agency_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddAgencyData.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                oHour = hourOfDay;
                                oMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,oHour,oMin);
                                agency_open.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(oHour,oMin);
                timePickerDialog.show();

            }
        });

        agency_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddAgencyData.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                cHour = hourOfDay;
                                cMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,cHour,cMin);
                                agency_close.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(cHour,cMin);
                timePickerDialog.show();

            }
        });



        //imageUploading
        agencyPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });




        dataSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataValidating(agency_address)||!dataValidating(agency_name)||
                        !validateContact(agency_contact)||!dataValidating(agency_location)
                        ||!dataValidating(agency_desc)||!uploadFile()){

                    Toast.makeText(getApplicationContext(),"Please fill all details correctly", LENGTH_SHORT).show();
                }else{
                    saveData();
                    uploadFile();
                    finish();
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
            Picasso.get().load(mImageUri).resize(70, 70)
                    .centerCrop().into(agencyPicture);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private boolean uploadFile(){

        if(mImageUri!= null){
            StorageReference fileReference = mStorageReference.child(agency_name_txt + getExtension(mImageUri));
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

                            Toast.makeText(getApplicationContext(), "Upload Successful !", Toast.LENGTH_LONG).show();

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("agencyProfile",uri.toString());

                                    db.collection("AgencyData").document(agency_name_txt).set(userData, SetOptions.merge())
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

            return true;

        }else{
            Toast.makeText(getApplicationContext(), "No Image selected!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void saveData(){

        // Converting to String
        agency_name_txt = agency_name.getText().toString();
        agency_desc_txt = agency_desc.getText().toString();
        agency_contact_txt =agency_contact.getText().toString();
        agency_address_txt = agency_address.getText().toString();
        agency_location_txt = agency_location.getText().toString();
        //agency_time_txt = agency_time.getText().toString();
        agency_time_txt = agency_open.getText().toString() + " - " + agency_close.getText().toString();
        map_link_txt = map_link.getText().toString();


        //agency_name, agencyTime,agencyLocation,agencyDesc,agencyContact,agencyAddress ;

        //mapping

        String agency_added_on = Timestamp.now().toDate().toString();

        Map<String, Object> docData = new HashMap<>();
        docData.put("agency_name",agency_name_txt);
        docData.put("agencyContact",agency_contact_txt );
        docData.put("agencyTime", agency_time_txt);
        docData.put("agency_added_on",agency_added_on); //extra
        docData.put("agencyLocation", agency_location_txt);
        docData.put("agencyAddress", agency_address_txt);
        docData.put("agencyDesc", agency_desc_txt);
        docData.put("map_link", map_link_txt);


        //add data to firebase

        db.collection("AgencyData").document(agency_name_txt)
                .set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    makeText(AddAgencyData.this, "Attempt FAILED"+ task.getException()
                            .getLocalizedMessage(), LENGTH_SHORT).show();
                }
                else{
                    makeText(AddAgencyData.this, "New Agency "+agency_name_txt+" added successfully! ",
                            LENGTH_SHORT).show();
                }
            }
        });


    }

}
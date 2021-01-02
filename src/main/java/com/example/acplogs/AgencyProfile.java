package com.example.acplogs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AgencyProfile extends AppCompatActivity {

    private static final String TAG = "AgencyProfile";

    TextView nameData, locationData, timeData, descData, addressData, contactData, agency_added_onData;
    String agency_name, agencyTime,agencyLocation,agencyDesc,
            agencyContact,agencyAddress,map_link,agencyProfile ,agency_added_on;

    ImageView agencyProfileData;

    FloatingActionButton mapLink, feedback;

    public void settingUpProfile(){

        agency_name = getIntent().getStringExtra("agencyName");
        agencyTime = getIntent().getStringExtra("agencyTime");
        agencyLocation=getIntent().getStringExtra("agencyLocation");
        agencyDesc= getIntent().getStringExtra("agencyDesc");
        agencyAddress= getIntent().getStringExtra("agencyAddress");
        agencyContact= getIntent().getStringExtra("agencyContact");
        map_link= getIntent().getStringExtra("map_link");
        agencyProfile= getIntent().getStringExtra("agencyProfile");
        agency_added_on= getIntent().getStringExtra("agency_added_on");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_profile);


        mapLink = (FloatingActionButton) findViewById(R.id.agencyMapLocation);
        feedback=(FloatingActionButton)findViewById(R.id.agencyContactLink);

        nameData= (TextView)findViewById(R.id.agencyName);
        locationData =(TextView)findViewById(R.id.areaData);
        timeData =(TextView)findViewById(R.id.timeData);
        descData=(TextView)findViewById(R.id.description);
        addressData=(TextView)findViewById(R.id.addressDataProfile);
        contactData=(TextView)findViewById(R.id.contactData);
        agencyProfileData=(ImageView)findViewById(R.id.agencyImage);
        agency_added_onData=(TextView)findViewById(R.id.added_on);

        settingUpProfile();
        settingTexts();


        mapLink.setOnClickListener(new View.OnClickListener() {
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
                    intent.setData(Uri.parse(map_link));
                    startActivity(intent);
                }
                catch (Exception e){
                    Log.i("Info",e.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), "Can't open link", Toast.LENGTH_SHORT).show();
                }
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"will open contact/WhatsApp link",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AgencyProfile.this, StudentFeedbacks.class);
                intent.putExtra("agency_name",agency_name);
                startActivity(intent);

            }
        });
    }

    private void settingTexts(){
        //editing texts
        nameData.setText(agency_name);
        locationData.setText(agencyLocation);
        descData.setText(agencyDesc);
        addressData.setText(agencyAddress);
        contactData.setText(agencyContact);
        agency_added_onData.setText(agency_added_on);

        //picasso
        try{
            Picasso.get().load(agencyProfile).into(agencyProfileData, new com.squareup.picasso.Callback() {
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
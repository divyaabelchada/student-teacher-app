package com.example.acplogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.security.spec.RSAOtherPrimeInfo;

public class AgencyInfo extends AppCompatActivity  {

    String agencyName, agencyTime,agencyLocation,agencyDesc,
            agencyContact,agencyAddress,map_link,agencyProfile ,agency_added_on;

    //firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference agencyDataRef = db.collection("AgencyData");



    //recyclerView
    private RecyclerView agency_list;
    private AgencyCardAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_info);

        getSupportActionBar().setElevation(0);


        //defining recyclerView
        agency_list= (RecyclerView)findViewById(R.id.agencyList);

        setUpRecyclerView();
    }

    //setup recyclerView
    private void setUpRecyclerView(){

        try {

            Query query = agencyDataRef.orderBy("agency_added_on", Query.Direction.ASCENDING);

            FirestoreRecyclerOptions<AgencyModel> options = new FirestoreRecyclerOptions.Builder<AgencyModel>()
                    .setQuery(query, AgencyModel.class)
                    .build();

            adapter = new AgencyCardAdapter(options);

            agency_list.setHasFixedSize(true);
            agency_list.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            agency_list.setAdapter(adapter);

            adapter.setOnItemClickListener(new AgencyCardAdapter.itemOnClickListener(){

                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                   AgencyModel model = documentSnapshot.toObject(AgencyModel.class);
                    if(model!=null) {
                        agencyName = model.getAgency_name();
                        agencyContact = model.getAgencyContact();
                        agencyLocation = model.getAgencyLocation();
                        agencyAddress = model.getAgencyAddress();
                        agencyDesc = model.getAgencyDesc();
                        agencyTime = model.getAgencyTime();
                        map_link = model.getMap_link();
                        agency_added_on = model.getAgency_added_on();
                        agencyProfile = model.getAgencyProfile();

                        Intent intent = new Intent(AgencyInfo.this, AgencyProfile.class);
                        intent.putExtra("agencyName", agencyName);
                        intent.putExtra("agencyLocation", agencyLocation);
                        intent.putExtra("agencyDesc", agencyDesc);
                        intent.putExtra("agencyTime", agencyTime);
                        intent.putExtra("agencyContact", agencyContact);
                        intent.putExtra("agencyAddress", agencyAddress);
                        intent.putExtra("agencyProfile", agencyProfile);
                        intent.putExtra("agency_added_on", agency_added_on);
                        intent.putExtra("map_link", map_link);


                        startActivity(intent);

                    }
                    else
                        Toast.makeText(getApplicationContext(),"some error",Toast.LENGTH_SHORT).show();

                }
            });

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error:"+ e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            Log.i("Info", e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}


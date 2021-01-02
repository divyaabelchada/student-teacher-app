package com.example.acplogs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AllStudents extends AppCompatActivity {

    Button bsc, bsc_cs, bsc_it, bsc_bt, bcom, baf, bfm, bbi, bmm, bms, ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_students);

        bsc = findViewById(R.id.bsc);
        bsc_cs = findViewById(R.id.bsc_cs);
        bsc_it = findViewById(R.id.bsc_it);
        bsc_bt = findViewById(R.id.bsc_bt);
        bcom = findViewById(R.id.bcom);
        baf = findViewById(R.id.baf);
        bfm = findViewById(R.id.bfm);
        bms = findViewById(R.id.bms);
        bbi = findViewById(R.id.bbi);
        bmm = findViewById(R.id.bmm);
        ba = findViewById(R.id.ba);

        bsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BSc");
                startActivity(intent);
            }
        });

        bsc_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BSc CS");
                startActivity(intent);
            }
        });

        bsc_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BSc IT");
                startActivity(intent);
            }
        });

        bsc_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BSc BT");
                startActivity(intent);
            }
        });

        bcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BCOM");
                startActivity(intent);
            }
        });

        baf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BAF");
                startActivity(intent);
            }
        });

        bms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BMS");
                startActivity(intent);
            }
        });

        bfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BFM");
                startActivity(intent);
            }
        });

        bbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BBI");
                startActivity(intent);
            }
        });

        bmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BMM");
                startActivity(intent);
            }
        });

        ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStudents.this, StudentCorner.class);
                intent.putExtra("studentClass",  "BA");
                startActivity(intent);
            }
        });






    }
}
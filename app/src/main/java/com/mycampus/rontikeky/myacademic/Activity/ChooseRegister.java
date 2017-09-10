package com.mycampus.rontikeky.myacademic.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.R;

public class ChooseRegister extends AppCompatActivity {

    ImageView student,guest;
    TextView student_view, guest_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_register);

        student = (ImageView)findViewById(R.id.imgStudent);
        guest = (ImageView)findViewById(R.id.imgGuest);
        student_view = (TextView)findViewById(R.id.viewStudent);
        guest_view = (TextView)findViewById(R.id.viewGuest);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRegister.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        student_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRegister.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRegister.this, RegisterGuest.class);
                startActivity(intent);
            }
        });

        guest_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRegister.this, RegisterGuest.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Pilih Pendaftaran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

package com.mycampus.rontikeky.myacademic.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.R;

public class SuccessRegister extends AppCompatActivity {

    TextView nama,email;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        nama = (TextView)findViewById(R.id.nama);
        email = (TextView)findViewById(R.id.email);
        btnBack = (Button)findViewById(R.id.btnBack);

        Intent result= getIntent();
        Bundle extrasResult = result.getExtras();

        if(extrasResult!=null)
        {
            String extrasNama =(String) extrasResult.get("nama");
            String extrasEmail = (String) extrasResult.get("email");
            nama.setText(extrasNama);
            email.setText("Link Konfirmasi telah di kirimkan ke "+extrasEmail+"  anda, silahkan lakukan konfirmasi. Cek Spam jika tidak ada di inbox");

        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessRegister.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

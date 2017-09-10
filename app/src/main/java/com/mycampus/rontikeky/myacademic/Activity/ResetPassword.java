package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Request.ResetPasswordRequest;
import com.mycampus.rontikeky.myacademic.Response.ResetPasswordResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    EditText txtReset;
    Button btnReset;
    TextView result;
    ProgressDialog pDialog;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_reset_password);

        txtReset = (EditText)findViewById(R.id.txtEmailReset);
        btnReset = (Button)findViewById(R.id.btnReset);
        result = (TextView)findViewById(R.id.resultReset);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/droid/DroidSerif.ttf");
        txtReset.setTypeface(custom_font);
        btnReset.setTypeface(custom_font);
        result.setTypeface(custom_font);

        pDialog = new ProgressDialog(ResetPassword.this);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.setCancelable(false);
                pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pDialog.show();

                sendResetPassword();

            }
        });

        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    private void sendResetPassword(){
        AcademicClient client = ServiceGenerator.createService(AcademicClient.class);
        ResetPasswordRequest request = new ResetPasswordRequest(txtReset.getText().toString());
        Call<ResetPasswordResponse> call = client.doReset(request);



        call.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                pDialog.dismiss();


                try {
                    if (response.body().getStatus().equalsIgnoreCase("false")) {
                        result.setBackgroundColor(Color.parseColor("#FF0000"));
                    } else {
                        result.setBackgroundColor(Color.parseColor("#00ff00"));
                    }
                    result.setVisibility(View.VISIBLE);
                    result.setText(response.body().getMessage());
                    Log.d("TRY", response.body().getStatus());
                } catch (Exception e) {
                    result.setText("Link Reset Password Telah dikirim ke Email Anda! \n" +
                            "note: Jika Email anda sudah terdaftar");
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                pDialog.dismiss();
                result.setBackgroundColor(Color.parseColor("#FF0000"));
                result.setVisibility(View.VISIBLE);
                Log.d("FAILURE", t.toString());
                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                } else {
                    result.setText("Format Email Salah, Silahkan Perbaiki Format Email Inputan");
                }
            }
        });
    }
    
    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

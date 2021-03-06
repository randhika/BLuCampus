package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Request.ChangePasswordRequest;
import com.mycampus.rontikeky.myacademic.Response.ChangePasswordResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    EditText password, konfirmasi_password;
    TextView result,viewHeader,viewMessage;
    Button btnChange;

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";
    String token;

    FontHandler fontHandler;

    ProgressDialog pDialog;

    boolean isConnected,valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        password = (EditText)findViewById(R.id.txtPassword);
        konfirmasi_password = (EditText)findViewById(R.id.txtPasswordKonfirmasi);
        btnChange = (Button)findViewById(R.id.btnChange);
        viewHeader = (TextView)findViewById(R.id.viewHeader);
        viewMessage = (TextView)findViewById(R.id.viewMessage);
        result = (TextView)findViewById(R.id.resultChange);

        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");

        pDialog = new ProgressDialog(ChangePassword.this);

        fontHandler = new FontHandler(this);
        Typeface custom_font = fontHandler.getFont();
        Typeface custom_font_bold = fontHandler.getFontBold();
        password.setTypeface(custom_font);
        konfirmasi_password.setTypeface(custom_font);
        btnChange.setTypeface(custom_font_bold);
        viewHeader.setTypeface(custom_font_bold);
        viewMessage.setTypeface(custom_font);
        result.setTypeface(custom_font);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                result.setVisibility(View.GONE);
                if (validation()){
                    if (password.getText().toString().trim().equalsIgnoreCase(konfirmasi_password.getText().toString().trim())){
                        Log.d("VALIDITY SAMA","SAMA");
                        doChangePassword();
                    }else{
                        Log.d("VALIDITY BEDA","SAMA");
                    }
                }else{
                    Log.d("VALIDITY 6", "GAGAL");
                }
            }
        });

        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void doChangePassword(){

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client = ServiceGeneratorAuth.createService(AcademicClient.class, token);
        ChangePasswordRequest request = new ChangePasswordRequest(password.getText().toString().trim(),konfirmasi_password.getText().toString().trim());
        Call<ChangePasswordResponse> call = client.doChangePassword(request);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                pDialog.dismiss();
                try{
                    if (response.isSuccessful()){
                        Log.d("VALIDITY 8", response.body().getMessage());
                        result.setVisibility(View.VISIBLE);
                        result.setText(response.body().getMessage());
                    }else{
                        Log.d("DEBUG PROFILE 2",response.body().getMessage());
                    }
                }catch (Exception e){
                    Log.d("DEBUG PROFILE 4",e.toString());

                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("VALIDITY 7",t.toString());
                result.setVisibility(View.VISIBLE);
                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                } else {
                    result.setText("Gagal Ubah Password!");
                }
            }
        });
    }

    private boolean validation(){

        valid = true;

        if (password.getText().toString().trim().isEmpty() || konfirmasi_password.getText().toString().trim().isEmpty()){
            Log.d("VALIDITY 1", "TIDAK BOLEH ADA FIELD KOSONG");
            result.setVisibility(View.VISIBLE);
            result.setText("Tidak ada Boleh Ada Field Yang Kosong");
            valid = false;
        }else{
            if (password.getText().toString().trim().length() < 8 || konfirmasi_password.getText().toString().trim().length() < 8){
                Log.d("VALIDITY 2", "Panjang Password minimal 8 karakter");
                result.setVisibility(View.VISIBLE);
                result.setText("Panjang Password minimal 8 karakter");
                valid = false;
            }else{
                if (password.getText().toString().trim().equalsIgnoreCase(konfirmasi_password.getText().toString().trim())){
                    Log.d("VALIDITY 4", "Berhasil");
                    result.setVisibility(View.VISIBLE);
                    result.setText("Berhasil");
                }else{
                    Log.d("VALIDITY 3", "Password Harus Sesuai dengan Konfirmasi password");
                    result.setVisibility(View.VISIBLE);
                    result.setText("Password Harus Sesuai dengan Konfirmasi password");
                    valid = false;
                }
            }
        }

        return valid;
    }

    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }
}

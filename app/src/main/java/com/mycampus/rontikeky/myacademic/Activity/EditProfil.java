package com.mycampus.rontikeky.myacademic.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Request.EditProfileRequest;
import com.mycampus.rontikeky.myacademic.Response.EditProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileEditMahasiswaResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponseEdit;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfil extends AppCompatActivity {
    EditText username, alamat, lahir, nama, email, password, txtTelp,nim;
    TextView result;
    Button simpan;
    ScrollView scrollView;

    SharedPreferences sharedPreferences, sharedPreferences2;
    String token_key = "token";
    String nama_key = "nama";
    String email_key = "email";
    String faculty_key = "faculty";
    String mypref = "MYPREFRENCES";
    String token;
    String nama_feed;
    String email_feed;

    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;

    ProgressDialog pDialog;
    boolean isConnected,valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        username = (EditText)findViewById(R.id.txtUsername);
        alamat = (EditText)findViewById(R.id.txtAlamat);
        lahir = (EditText)findViewById(R.id.txtDate);
        nama = (EditText)findViewById(R.id.txtName);
        email = (EditText)findViewById(R.id.txtEmail);
        password = (EditText)findViewById(R.id.txtPassword);
        txtTelp = (EditText)findViewById(R.id.txtTelp);
        result = (TextView)findViewById(R.id.result);
        simpan = (Button)findViewById(R.id.btnSimpan);

        scrollView = (ScrollView)findViewById(R.id.profileScrollView);

        username.setFocusable(false);
        username.setClickable(false);
        email.setFocusable(false);
        email.setClickable(false);

        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");

        pDialog = new ProgressDialog(EditProfil.this);

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client = ServiceGeneratorAuth.createService(AcademicClient.class,token);
        Call<ProfileResponse> call = client.getProfile();
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                try{
                    pDialog.dismiss();
                    Log.d("RESPONSE : ", new Gson().toJson(response.body()));
                    username.setText(response.body().getUsername());
                    alamat.setText(response.body().getAlamat());
                    nama.setText(response.body().getNama());
                    lahir.setText(response.body().getTanggalLahir());
                    email.setText(response.body().getEmail());
                    txtTelp.setText(response.body().getTelepon());

                    String jsonObject = new Gson().toJson(response.body());


                    Gson gson = new Gson();

                    ProfileResponseEdit mhs = gson.fromJson(jsonObject, ProfileResponseEdit.class);


                    if (response.body().mahasiswa != null){
                        nim = (EditText)findViewById(R.id.txtNIM);
                        nim.setVisibility(View.VISIBLE);
                        nim.setText(mhs.mahasiswa.nim);
                        nim.setEnabled(false);
                    }else{
                        nim = (EditText)findViewById(R.id.txtNIM);
                        nim.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    Log.d("CATCH : ",e.toString());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("Failure", t.toString());
            }
        });

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfil.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                result.setVisibility(View.GONE);
                result.setBackgroundColor(Color.parseColor("#00ff00"));
                if (validation()){
                    doEditProfile();
                }
            }
        });


        getSupportActionBar().setTitle("Edit Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showDialogOnClick(){
        lahir = (EditText)findViewById(R.id.txtDate);

        lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID){
            return new DatePickerDialog(this, dpickerdialog, 1998,month_x,day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerdialog = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month+1;
            day_x = dayOfMonth;
            lahir.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };

    private void doEditProfile(){
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client = ServiceGeneratorAuth.createService(AcademicClient.class, token);

        EditProfileRequest request = new EditProfileRequest(nama.getText().toString(),lahir.getText().toString(),alamat.getText().toString(),txtTelp.getText().toString());

        Call<EditProfileResponse> call = client.doEditProfile(request);

        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                pDialog.dismiss();
                try{
                    if (response.isSuccessful()){
                        Log.d("DEBUG PROFILE 1",response.body().getMessage());
                        result.setVisibility(View.VISIBLE);
                        result.setBackgroundColor(Color.parseColor("#00ff00"));
                        result.setText(response.body().getMessage());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(nama_key, nama.getText().toString());
                        editor.commit();
                    }else{
                        Log.d("DEBUG PROFILE 2",response.body().getMessage());
                    }
                    scrollView.smoothScrollTo(0, 0);
                }catch (Exception e){
                    Log.d("DEBUG PROFILE 4",e.toString());
                    scrollView.smoothScrollTo(0, 0);
                }

            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("DEBUG PROFILE 3",t.toString());
                result.setVisibility(View.VISIBLE);
                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                } else {
                    result.setText("Semua Field Tidak Boleh Kosong");
                }
                scrollView.smoothScrollTo(0, 0);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean validation(){

        valid = true;

        if (nama.getText().toString().trim().isEmpty() || lahir.getText().toString().trim().isEmpty() || alamat.getText().toString().trim().isEmpty()){
            Log.d("VALIDITY 1", "TIDAK BOLEH ADA FIELD KOSONG");
            result.setVisibility(View.VISIBLE);
            result.setText("Tidak ada Boleh Ada Field Yang Kosong");
            valid = false;
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

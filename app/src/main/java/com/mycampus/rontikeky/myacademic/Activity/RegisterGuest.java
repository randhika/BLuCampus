package com.mycampus.rontikeky.myacademic.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Request.SignupGuestRequest;
import com.mycampus.rontikeky.myacademic.Response.SignupGuestResponse;
import com.mycampus.rontikeky.myacademic.ResponseError.RegisterUserErrorResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ErrorRegisterUtil;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGenerator;
import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterGuest extends AppCompatActivity {

    EditText txtUsername, txtName, txtEmail, txtPassword,txtKonfirmasiPassword, txtAlamat,txtTelp;
    TextView test,result;
    RadioGroup radioGender;
    RadioButton radioGenderItem;
    Button btnRegister;
    ScrollView scrollView;

    EditText txtDate;
    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;

    boolean isConnected,valid;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_guest);

        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtName = (EditText)findViewById(R.id.txtName);
        //radioGender = (RadioGroup)findViewById(R.id.radioGender);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtKonfirmasiPassword = (EditText)findViewById(R.id.txtKonfirmasiPassword);
        //txtAlamat = (EditText)findViewById(R.id.txtAlamat);
        txtTelp = (EditText)findViewById(R.id.txtTelp);
        btnRegister = (Button)findViewById(R.id.btnDaftar);
        test = (TextView)findViewById(R.id.test);
        result = (TextView)findViewById(R.id.result);
        scrollView = (ScrollView)findViewById(R.id.scrollGuest);

        pDialog = new ProgressDialog(RegisterGuest.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                result.setVisibility(View.GONE);
                if (validation()){
                    Log.d("CEK","BERHASIL");
                    doSignup();
                }else{
                    Log.d("CEK","GAGAL");
                }
            }
        });

    }

    public void doSignup(){

//        int selectedId = radioGender.getCheckedRadioButtonId();
//        radioGenderItem = (RadioButton)findViewById(selectedId);
//        String gender  = "";
//        if (radioGenderItem.getText().toString().trim().equalsIgnoreCase("Pria")){
//            gender = "L";
//        }else{
//            gender = "P";
//        }

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client = ServiceGenerator.createService(AcademicClient.class);

        SignupGuestRequest request = new SignupGuestRequest(txtUsername.getText().toString().trim(),txtName.getText().toString(),txtEmail.getText().toString().trim(),txtPassword.getText().toString().trim(),txtTelp.getText().toString().trim());

        Call<SignupGuestResponse> call = client.dosignupGuest(request);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i("tag", "This'll run 300 milliseconds later");
                    }
                },
                300);

        call.enqueue(new Callback<SignupGuestResponse>() {
            @Override
            public void onResponse(Call<SignupGuestResponse> call, Response<SignupGuestResponse> response) {
                pDialog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Berhasil Mendaftar!",Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE ALL", response.raw().message());
                    Log.d("RESPONSE EMAIL",response.body().getEmail());
                    Log.d("RESPONSE ID", String.valueOf(response.body().getId()));
                    Intent intent = new Intent(RegisterGuest.this, SuccessRegister.class);
                    intent.putExtra("nama",response.body().getNama());
                    intent.putExtra("email",response.body().getEmail());
                    startActivity(intent);
                }else{
                    RegisterUserErrorResponse errorResponse = ErrorRegisterUtil.parseError(response);
                    if (errorResponse.getEmail() != null){
                        result.setVisibility(View.VISIBLE);
                        scrollView.smoothScrollTo(0,0);
                        txtEmail.setError("Email sudah digunakan");
                        result.append("*Email sudah digunakan\n");
                    }
                    if (errorResponse.getUsername() != null){
                        result.setVisibility(View.VISIBLE);
                        scrollView.smoothScrollTo(0,0);
                        txtUsername.setError("Username sudah digunakan");
                        result.append("*Username sudah digunakan\n");
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupGuestResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("FAIL", t.toString());
                result.setVisibility(View.VISIBLE);

                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                }else if(t instanceof SocketTimeoutException){
                    result.setText("Server sedang bermasalah atau cek jaringan internet anda!");
                    scrollView.smoothScrollTo(0, 0);
                }else {
                    result.setText("Username atau Email sudah digunakan");
                    scrollView.smoothScrollTo(0, 0);
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validation(){

        valid = true;

        txtPassword.setError(null);
        txtUsername.setError(null);
        txtTelp.setError(null);
        txtKonfirmasiPassword.setError(null);
        txtEmail.setError(null);
        txtName.setError(null);


        if (txtUsername.getText().toString().trim().isEmpty() || txtName.getText().toString().isEmpty() ||  txtPassword.getText().toString().trim().isEmpty() || txtEmail.getText().toString().trim().isEmpty()){
            Log.d("VALIDITY 1", "TIDAK BOLEH ADA FIELD KOSONG");
            result.setVisibility(View.VISIBLE);
            result.append("*Tidak ada Boleh Ada Field Yang Kosong\n");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        }

        if (txtUsername.getText().toString().trim().length() < 6){
            Log.d("VALIDITY 5", "ISI");
            result.setVisibility(View.VISIBLE);
            result.append("Username minimal 6 karakter\n");
            txtUsername.setError("Username minimal 6 karakter");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        }

        if (txtPassword.getText().toString().trim().length() < 8){
            result.setVisibility(View.VISIBLE);
            result.append("Password minimal 8 karakter\n");
            txtPassword.setError("Password minimal 8 karakter");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        }

        if (!isEmailValid(txtEmail.getText().toString())){
            result.setVisibility(View.VISIBLE);
            result.append("*Format Email Salah\n");
            txtEmail.setError("Format Email Salah");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        }

        if (!txtPassword.getText().toString().trim().equalsIgnoreCase(txtKonfirmasiPassword.getText().toString().trim())){
            result.setVisibility(View.VISIBLE);
            result.append("*Konfirmasi password tidak sesuai dengan password\n");
            txtKonfirmasiPassword.setError("Konfirmasi password tidak sesuai dengan password");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        }

        if (txtTelp.getText().toString().trim().length() < 10 || txtTelp.getText().toString().length() >15){
            result.setVisibility(View.VISIBLE);
            result.append("*Telepon harus 10-15 panjang karakter\n");
            txtTelp.setError("Telepon harus 10-15 panjang karakter");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        }

        return valid;
    }

}

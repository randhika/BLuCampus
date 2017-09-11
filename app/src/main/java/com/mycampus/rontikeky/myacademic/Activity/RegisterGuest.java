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
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
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
                Toast.makeText(getApplicationContext(),"Berhasil Mendaftar!",Toast.LENGTH_SHORT).show();
                Log.d("RESPONSE ALL", response.raw().message());
                Log.d("RESPONSE EMAIL",response.body().getEmail());
                Log.d("RESPONSE ID", String.valueOf(response.body().getId()));
                Intent intent = new Intent(RegisterGuest.this, SuccessRegister.class);
                intent.putExtra("nama",response.body().getNama());
                intent.putExtra("email",response.body().getEmail());
                startActivity(intent);
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

        if (txtUsername.getText().toString().trim().isEmpty() || txtName.getText().toString().isEmpty() || txtPassword.getText().toString().trim().isEmpty() || txtEmail.getText().toString().trim().isEmpty() || txtTelp.getText().toString().isEmpty()){
            Log.d("VALIDITY 1", "TIDAK BOLEH ADA FIELD KOSONG");
            result.setVisibility(View.VISIBLE);
            result.setText("Tidak ada Boleh Ada Field Yang Kosong");
            scrollView.smoothScrollTo(0, 0);
            valid = false;
        }else{
            Log.d("VALIDITY 2", "ISI");
            if (txtUsername.getText().toString().trim().length() < 8 || txtPassword.getText().toString().trim().length() < 8){
                Log.d("VALIDITY 5", "ISI");
                result.setVisibility(View.VISIBLE);
                result.setText("Username dan Password minimal 8 karakter");
                scrollView.smoothScrollTo(0, 0);
                valid = false;
            }else{
                if (isEmailValid(txtEmail.getText().toString())){
                    if (txtPassword.getText().toString().trim().equalsIgnoreCase(txtKonfirmasiPassword.getText().toString().trim())){

                            if (txtTelp.getText().toString().trim().length() >= 10 && txtTelp.getText().toString().length() <=15){
                                Log.d("VALIDITY 7", "Berhasil");
                                result.setVisibility(View.VISIBLE);

                            }else{
                                Log.d("VALIDITY 20", "ISI");
                                result.setVisibility(View.VISIBLE);
                                result.setText("Telp minimal 10 dan maximal 15 karakter");
                                scrollView.smoothScrollTo(0, 0);
                                valid = false;
                            }

                    }else{
                        Log.d("VALIDITY 12", "Password Harus Sesuai dengan Konfirmasi password");
                        result.setVisibility(View.VISIBLE);
                        result.setText("Password Harus Sesuai dengan Konfirmasi password");
                        scrollView.smoothScrollTo(0, 0);
                        valid = false;
                    }
                }else{
                    result.setVisibility(View.VISIBLE);
                    result.setText("Format Email Tidak Valid");
                    Log.d("VALIDITY 4", "TIDAK VALID");
                    scrollView.smoothScrollTo(0, 0);
                    valid = false;
                }
            }
        }
        return valid;
    }

}

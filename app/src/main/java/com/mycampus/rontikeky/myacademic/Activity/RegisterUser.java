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
import com.mycampus.rontikeky.myacademic.Request.SignupUserRequest;
import com.mycampus.rontikeky.myacademic.Response.SignupUserResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGenerator;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUser extends AppCompatActivity {


    EditText txtDate,txtNIM, txtUsername, txtName, txtEmail, txtPassword,txtKonfirmasiPassword, txtAlamat, txtTelp;
    TextView test,result;
    RadioGroup radioGender;
    RadioButton radioGenderItem;
    Button btnRegister;
    ScrollView scrollView;
    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;

    String gender;
    ProgressDialog pDialog;

    boolean isConnected;
    boolean valid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        txtNIM = (EditText)findViewById(R.id.txtNIM);
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtName = (EditText)findViewById(R.id.txtName);
        //radioGender = (RadioGroup)findViewById(R.id.radioGender);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtKonfirmasiPassword = (EditText)findViewById(R.id.txtKonfirmasiPassword);
        //txtAlamat = (EditText)findViewById(R.id.txtAddress);
        txtTelp = (EditText)findViewById(R.id.txtTelp);
        btnRegister = (Button)findViewById(R.id.btnDaftar);
        scrollView = (ScrollView)findViewById(R.id.scrollViewuser);
        test = (TextView)findViewById(R.id.test);
        result = (TextView)findViewById(R.id.result);

//        final Calendar calendar = Calendar.getInstance();
//        year_x = calendar.get(Calendar.YEAR);
//        month_x = calendar.get(Calendar.MONTH);
//        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        //showDialogOnClick();

        pDialog = new ProgressDialog(RegisterUser.this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setFocusable(false);
                result.setText("");
                result.setVisibility(View.GONE);

                if (validation()){
                    Log.d("CEK","BERHASIL");
                    doRegisterUser();
                }else{
                    Log.d("CEK","GAGAL");
                }
            }
        });
    }

//    public void showDialogOnClick(){
//        txtDate = (EditText)findViewById(R.id.txtDate);
//
//        txtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog(DIALOG_ID);
//            }
//        });
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        if (id == DIALOG_ID){
//            //return new DatePickerDialog(this, dpickerdialog, year_x,month_x,day_x);
//            return new DatePickerDialog(this, dpickerdialog, 2000,month_x,day_x);
//        }
//        return null;
//    }
//
//    private DatePickerDialog.OnDateSetListener dpickerdialog = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//            year_x = year;
//            month_x = month+1;
//            day_x = dayOfMonth;
//            txtDate.setText(year_x+"-"+month_x+"-"+day_x);
//        }
//    };


    private void doRegisterUser(){
//        int selectedId = radioGender.getCheckedRadioButtonId();
//        radioGenderItem = (RadioButton)findViewById(selectedId);
//
//        if (radioGenderItem.getText().toString().equalsIgnoreCase("Pria")){
//            gender = "L";
//        }else{
//            gender = "P";
//        }


        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client = ServiceGenerator.createService(AcademicClient.class);

        SignupUserRequest request = new SignupUserRequest(txtUsername.getText().toString().trim(),txtName.getText().toString(),txtEmail.getText().toString().trim(),txtPassword.getText().toString().trim(),txtTelp.getText().toString().trim(),txtNIM.getText().toString().trim());

        Call<SignupUserResponse> call = client.dosignup(request);

        call.enqueue(new Callback<SignupUserResponse>() {
            @Override
            public void onResponse(Call<SignupUserResponse> call, Response<SignupUserResponse> response) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Berhasil Mendaftar!",Toast.LENGTH_LONG).show();
                Log.d("RESPONSE ALL", response.raw().message());
                Log.d("RESPONSE EMAIL",response.body().getEmail());
                Log.d("RESPONSE ID", String.valueOf(response.body().getId()));
                Intent intent = new Intent(RegisterUser.this, SuccessRegister.class);
                intent.putExtra("nama",response.body().getNama());
                intent.putExtra("email",response.body().getEmail());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<SignupUserResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("FAIL", t.toString());
                result.setVisibility(View.VISIBLE);
                if (!checkConnection()) {
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                } else {
                    result.setText("Username atau Email atau NIM sudah digunakan");
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
        //String expression = "^[A-Z0-9._%+-]+@student.budiluhur.ac.id$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validation(){

        valid = true;

        if (txtUsername.getText().toString().trim().isEmpty() || txtName.getText().toString().isEmpty() || txtNIM.getText().toString().trim().isEmpty() || txtPassword.getText().toString().trim().isEmpty() || txtEmail.getText().toString().trim().isEmpty()){
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
                result.setText("Username dan Password minamal 8 karakter");
                scrollView.smoothScrollTo(0, 0);
                valid = false;
            }else{
                if (isEmailValid(txtEmail.getText().toString())){
                    Log.d("VALIDITY 3", "VALID");
                    if (txtNIM.getText().toString().trim().length() != 10){
                        Log.d("VALIDITY 6", "TIDAK VALID");
                        result.setVisibility(View.VISIBLE);
                        result.setText("Panjang NIM harus 10");
                        scrollView.smoothScrollTo(0, 0);
                        valid = false;
                    }else{
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
                    }
                }else{
                    result.setVisibility(View.VISIBLE);
                    //result.setText("Format Email Student Tidak Valid (xxxx@student.budiluhur.ac.id");
                    result.setText("Format Email Tidak Valid");
                    scrollView.smoothScrollTo(0,0);
                    Log.d("VALIDITY 4", "TIDAK VALID");
                    valid = false;
                }
            }
        }

        return valid;
    }

}

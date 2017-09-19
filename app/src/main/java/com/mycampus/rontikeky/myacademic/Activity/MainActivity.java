package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Request.LoginRequest;
import com.mycampus.rontikeky.myacademic.Response.LoginResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.CheckConnection;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtUsername, txtPassword;
    CheckBox showPassword;
    TextView txtRegister,txtForgotPassword, error;

    CheckConnection checkConnection;
    ProgressDialog pDialog;

    //Shared Prefrences
    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";

    PrefHandler prefHandler;

    boolean isConnected,valid,doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Inisialisasi
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        error       = (TextView)findViewById(R.id.failed_login);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        showPassword = (CheckBox)findViewById(R.id.cbShowPwd);
        txtRegister = (TextView)findViewById(R.id.txtRegister);
        txtForgotPassword = (TextView)findViewById(R.id.forgotPassword);
        checkConnection = new CheckConnection(getApplicationContext());


        //Setting Custom Fonts dengan Typeface
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/droid/DroidSerif.ttf");
        //Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Gotham Rounded Bold.otf");
        txtForgotPassword.setTypeface(custom_font);
        txtRegister.setTypeface(custom_font);
        txtPassword.setTypeface(custom_font);
        txtUsername.setTypeface(custom_font);
        btnLogin.setTypeface(custom_font);
        error.setTypeface(custom_font);
        showPassword.setTypeface(custom_font);


        //Instansiasi Progress Dialog
        pDialog = new ProgressDialog(MainActivity.this);


        //Instansisasi SharedPref class PrefHandler
        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        prefHandler = new PrefHandler(this);


        //Mengecek apakah token tersimpan kedalam SharedPref
        if (prefHandler.isToken_Key_Exist()){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            Log.d("TOKEN 1", "ADA");
        }else{
            Log.d("TOKEN 2","TIDAK ADA");
        }


        //Menampilkan ShowPassword saat button Show diklik
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        //Melakukan Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setText("");
                error.setVisibility(View.GONE);

                //Jika Valid maka tampilkan Progress Dialog dan lakukan proses Login, else TextView error mucul
                if (validation()) {
                    pDialog.setCancelable(false);
                    pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pDialog.show();
                    doLogin();
                } else {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Username dan Password Salah!");
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseRegister.class);
                startActivity(intent);
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });





        //Instansiasi ActionBar untuk mengatur icon pada bagian atas
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        //Membuat Custom Layout pada Action Bar
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.logo_actionbar, null);
        actionBar.setCustomView(v);

    }


    //Proses Login dengan Retrofit (proses mengirim data ke Server beserta responsenya), Jika Data Valid maka akan didirect ke Trantition.class
    private void doLogin(){
        AcademicClient client = ServiceGenerator.createService(AcademicClient.class);

        LoginRequest loginRequest = new LoginRequest(txtUsername.getText().toString().trim(),txtPassword.getText().toString().trim());

        Call<LoginResponse> call = client.doLogin(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                pDialog.dismiss();

                try {
                    if (response.isSuccessful()){
                        Log.d("VALIDITY LOGIN 1", "BERHASIL");
                        if (response.body().getStatus().toString().equalsIgnoreCase("false")){
                            Log.d("VALIDITY LOGIN 3", "BERHASIL");
                            error.setVisibility(View.VISIBLE);
                            error.setText(response.body().getMessage());
                        }else{
                            if (sharedPreferences.contains(token_key)){
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                                error.setText(token_key);
                                Log.d("TOKEN 1", "ADA");
                            }else{

                                //Simapn Token ke SharedPref
                                prefHandler.setTOKEN_KEY(response.body().getToken());

                                Toast.makeText(getApplicationContext(), "Berhasil Login!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, transition.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                                error.setText(response.body().getToken());
                                Log.d("VALIDITY LOGIN 4", "BERHASIL");
                                Log.d("TOKEN 2","TIDAK ADA");
                            }
                        }
                    }else{
                        Log.d("VALIDITY LOGIN 2", "GAGAL");
                        error.setVisibility(View.VISIBLE);
                        error.setText(response.body().getMessage());
                    }
                } catch (Exception e) {
                    error.setText("Password dan Username Salah!");
                    Log.d("DEBUG EXCEPTION : ", e.toString());
                }

            }

            //Jika Response Gagal
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("FAILURE", t.toString());
                if (!checkConnection()){
                    Toast.makeText(getApplicationContext(), "Internet bermasalah!", Toast.LENGTH_LONG).show();
                }else{
                    error.setText("Password dan Username Salah!");
                }
            }
        });
    }

    //Mengecek Koneksi internet
    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }


    //Lakukan Validasi pada EditText
    private boolean validation(){

        valid = true;

        if (txtUsername.getText().toString().trim().isEmpty() || txtPassword.getText().toString().trim().isEmpty()
                || txtUsername.getText().toString().trim().length() < 8 || txtPassword.getText().toString().trim().length() < 8){
            Log.d("VALIDITY 1", "Username dan Password Salah");
            error.setVisibility(View.VISIBLE);
            error.setText("Username dan Password Salah!");
            valid = false;
        }else{
            Log.d("VALIDITY 2","BERHASIL");
        }

        return valid;
    }


    //Ketika Back Button diClik
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            //finish();
            //moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}

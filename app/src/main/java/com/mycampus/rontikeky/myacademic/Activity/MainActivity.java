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
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.GsonBuilder;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Request.LoginGoogleRequest;
import com.mycampus.rontikeky.myacademic.Request.LoginRequest;
import com.mycampus.rontikeky.myacademic.Response.LoginGoogleFailedResponse;
import com.mycampus.rontikeky.myacademic.Response.LoginGoogleSucccesResponse;
import com.mycampus.rontikeky.myacademic.Response.LoginResponse;
import com.mycampus.rontikeky.myacademic.ResponseError.RegisterUserErrorResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.CheckConnection;
import com.mycampus.rontikeky.myacademic.RestApi.ErrorRegisterUtil;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

//    implements
//    View.OnClickListener,
//    GoogleApiClient.OnConnectionFailedListener
//
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
    FontHandler fontHandler;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;

    boolean isConnected,valid,doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        //Inisialisasi
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        error       = (TextView)findViewById(R.id.failed_login);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        //showPassword = (CheckBox)findViewById(R.id.cbShowPwd);
        txtRegister = (TextView)findViewById(R.id.txtRegister);
        txtForgotPassword = (TextView)findViewById(R.id.forgotPassword);
        checkConnection = new CheckConnection(getApplicationContext());
        //btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);


        //Setting Custom Fonts dengan Typeface
        fontHandler = new FontHandler(this);
        Typeface custom_font        = fontHandler.getFont();
        Typeface custom_font_bold   = fontHandler.getFontBold();
        txtForgotPassword.setTypeface(custom_font);
        txtRegister.setTypeface(custom_font_bold);
        txtPassword.setTypeface(custom_font);
        txtUsername.setTypeface(custom_font);
        btnLogin.setTypeface(custom_font);
        error.setTypeface(custom_font);

        //showPassword.setTypeface(custom_font);


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

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        // Customizing G+ button
//        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
//        btnSignIn.setScopes(gso.getScopeArray());

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
                //signOut();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });


//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signIn();
//            }
//        });


//        //Instansiasi ActionBar untuk mengatur icon pada bagian atas
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowCustomEnabled(true);
//
//        //Membuat Custom Layout pada Action Bar
//        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.logo_actionbar, null);
//        actionBar.setCustomView(v);

    }

    /*--------------------------------------------------------------------------------------------------------------*/
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        Log.d("Status", String.valueOf(status));
//                    }
//                });
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//
//            Log.e(TAG, "display name: " + acct.getIdToken());
//
//            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
//            String email = acct.getEmail();
//
//            Log.e(TAG, "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl);
//
//            prefHandler.setAUTH_TOKEN_GOOGLE(acct.getIdToken());
//
//            doGoogleLogin(acct.getIdToken());
//
//        } else {
//            Log.e(TAG, "display name x: " + "Gagal");
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//
//        switch (id) {
//            case R.id.btn_sign_in:
//                signIn();
//                Log.d("Jamban","awddwa");
//                break;
//
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mGoogleApiClient.isConnected()){
//            mGoogleApiClient.clearDefaultAccountAndReconnect();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mGoogleApiClient.isConnected()){
//            mGoogleApiClient.clearDefaultAccountAndReconnect();
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//    }
//
//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage("Tunggu lah...");
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }
//
//
//    private void doGoogleLogin(String tokenId){
//        AcademicClient client = ServiceGenerator.createService(AcademicClient.class);
//
//        LoginGoogleRequest loginGoogleRequest = new LoginGoogleRequest(tokenId);
//
//        Call<LoginGoogleSucccesResponse> call = client.doGoogleLogin(loginGoogleRequest);
//
//        call.enqueue(new Callback<LoginGoogleSucccesResponse>() {
//            @Override
//            public void onResponse(Call<LoginGoogleSucccesResponse> call, Response<LoginGoogleSucccesResponse> response) {
//                if (response.isSuccessful()){
//                    //Log.d("RESPONSE",new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
//                    if (response.body().getStatus().equalsIgnoreCase("false")){
//                        Intent toRegister = new Intent(MainActivity.this,ChooseRegister.class);
//                        startActivity(toRegister);
//                    }else{
//                        //Simapn Token ke SharedPref
//                        prefHandler.setTOKEN_KEY(response.body().getToken());
//
//                        Toast.makeText(getApplicationContext(), "Berhasil Login!", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(MainActivity.this, transition.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        startActivity(intent);
//                        finish();
//                    }
//                }else{
//                    LoginGoogleFailedResponse errorResponse = ErrorRegisterUtil.parseErrorGoogleLogin(response);
//
//                    Toast.makeText(MainActivity.this,"Gagal Login",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginGoogleSucccesResponse> call, Throwable t) {
//                Log.d("FAILURE",t.getMessage());
//            }
//        });
//    }



    /* ---------------------------------------------------------------------------------------------------------------*/



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

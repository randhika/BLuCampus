package com.mycampus.rontikeky.myacademic.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.GsonBuilder;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.AndroidUpdateResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class splash_screen extends AppCompatActivity {
    //Set waktu lama splashscreen
    private static int splashInterval = 2000;
    private String versionName;
    private int versionCode;
    PrefHandler prefHandler;
    boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        //Instansiasi Class PrefHandler
        prefHandler = new PrefHandler(this);


        //Memberi Delay pada Activity selama @splashInterval
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub


                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    versionName = pInfo.versionName;
                    versionCode = pInfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }


                //Cek jika token tersimpan didalam sharedpref, maka akan didirect ke class transtition, else ke kelas MainActivity

                if (checkConnection()){
                    getAndroidVersion();
                }else{
                    askLoadAgain();
                }


                //jeda selesai Splashscreen
                //this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);

    };

    private void getAndroidVersion(){
        if (!checkConnection()){
            askLoadAgain();
        }

        AcademicClient client = ServiceGenerator.createService(AcademicClient.class);

        Call<AndroidUpdateResponse> call = client.getAndroidVersion();

        call.enqueue(new Callback<AndroidUpdateResponse>() {
            @Override
            public void onResponse(Call<AndroidUpdateResponse> call, Response<AndroidUpdateResponse> response) {
                Log.d("RESPONSE",new GsonBuilder().setPrettyPrinting().create().toJson(response));

                if (versionCode < response.body().getVersionCode()){
                    Log.d("TAG","ada update!");
                    if (response.body().getStatusUpdate() == 0){
                        Log.d("TAG","Rekomendasi Update!");
                        askRecommendedUpdate();
                    }else{
                        Log.d("TAG","Harus update!");
                        askForceUpdate();
                    }
                }else{
                    if (prefHandler.isToken_Key_Exist()){
                        Intent intent = new Intent(splash_screen.this, transition.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(splash_screen.this, MainActivity.class);
                        startActivity(intent);
                    }
                    Log.d("TAG","No Update!");
                }

            }

            @Override
            public void onFailure(Call<AndroidUpdateResponse> call, Throwable t) {
                Log.d("RESPONSE ERROR",t.getMessage());
            }
        });
    }

    private void askRecommendedUpdate(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(splash_screen.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(splash_screen.this);
        }
        builder.setTitle("Pemberitahuan!")
                .setMessage("Silahkan update aplikasi anda..")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                ("market://details?id=com.mycampus.rontikeky.myacademic")));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (prefHandler.isToken_Key_Exist()){
                            Intent intent = new Intent(splash_screen.this, transition.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(splash_screen.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .show();
    }

    private void askForceUpdate(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(splash_screen.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(splash_screen.this);
        }
        builder.setTitle("Pemberitahuan!")
                .setMessage("Silahkan update aplikasi anda..")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                ("market://details?id=com.mycampus.rontikeky.myacademic")));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    private void askLoadAgain(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(splash_screen.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(splash_screen.this);
        }
        builder.setTitle("Pemberitahuan!")
                .setMessage("Internet Bermasalah.Ulangi?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        getAndroidVersion();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    private boolean checkConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }

}

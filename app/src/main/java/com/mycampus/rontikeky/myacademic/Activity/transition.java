package com.mycampus.rontikeky.myacademic.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.HottestEventResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class transition extends AppCompatActivity {

    //Set waktu lama splashscreen
    private static int splashInterval = 3000;
    TextView txtNama,txtJudul,txtWaktu;
    ImageView imgHot;
    Button btnSkip;

    String token;
    String nama_feed;
    String email_feed;

    PrefHandler prefHandler;
    FontHandler fontHandler;

    ProgressBar progressBar;

    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transition);

        btnSkip = (Button)findViewById(R.id.skip);
        txtNama = (TextView)findViewById(R.id.welcome);
        txtJudul = (TextView)findViewById(R.id.titleHot);
        txtWaktu = (TextView)findViewById(R.id.dateHot);
        imgHot = (ImageView)findViewById(R.id.imgHot);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        btnSkip.setVisibility(View.GONE);

        fontHandler = new FontHandler(this);
        Typeface custom_font = fontHandler.getFont();
        Typeface custom_font_bold = fontHandler.getFontBold();
        txtNama.setTypeface(custom_font);
        txtJudul.setTypeface(custom_font_bold);
        txtWaktu.setTypeface(custom_font);
        btnSkip.setTypeface(custom_font_bold);

        prefHandler = new PrefHandler(this);
        token = prefHandler.getTOKEN_KEY();

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(transition.this,MainFeedDrawer.class);
                startActivity(i);
                finish();
            }
        });


        getProfile();
        getHottestEvent();

        try {
            File httpCacheDir = new File(transition.this.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i("TEST", "HTTP response cache installation failed:" + e);
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout_transition);

        overridePendingTransition(R.animator.enter,R.animator.exit);
    };

    private void getProfile(){
        AcademicClient client = ServiceGeneratorAuth2.createService(AcademicClient.class, token,transition.this);
        Call<ProfileResponse> call1 = client.getProfile();

        call1.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    btnSkip.setVisibility(View.VISIBLE);
                    Log.d("NAMA : ", response.body().getNama());
                    Log.d("EMAIL : ", response.body().getEmail());

                    txtNama.setText(response.body().getNama());

                    prefHandler.setIMAGE_PROFILE_KEY(response.body().getFoto());

                    nama_feed = response.body().getNama();
                    email_feed = response.body().getEmail();


                    prefHandler.setNAME_KEY(nama_feed);
                    prefHandler.setEMAIL_KEY(email_feed);


                } catch (Exception e) {
                    Log.d("EXCEPTION PROFILE", e.toString());
                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                try {
                    Log.d("FAILURE 1 : ", t.toString());
                    if (!checkConnection()) {
                        Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
                        askLoadAgainProfile();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet 2",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getHottestEvent(){
        AcademicClient client2 = ServiceGeneratorAuth2.createService(AcademicClient.class, token,transition.this);
        Call<HottestEventResponse> call2 = client2.getHotEvent();

        call2.enqueue(new Callback<HottestEventResponse>() {
            @Override
            public void onResponse(Call<HottestEventResponse> call, Response<HottestEventResponse> response) {
                try {
                    if (response.body().data.size() != 0){
                        txtJudul.setText(response.body().data.get(0).judulAcara);

                        //Glide.with(getApplicationContext()).load(response.body().data.get(0).fotoAcara).into(imgHot);
                        Glide.with(getApplicationContext()).load(response.body().data.get(0).fotoAcara).placeholder(R.drawable.nopicture).into(imgHot);

                        Calendar c = Calendar.getInstance();

                        String tgl = response.body().data.get(0).tanggalAcara;

                        SimpleDateFormat tm = new SimpleDateFormat("kk:mm");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

                        String formattedTime = tm.format(c.getTime());
                        String formattedDate = df.format(c.getTime());
                        String formmatedDate2 = vd.format(df.parse(tgl));


                        txtWaktu.setText(formmatedDate2 + ", " + response.body().data.get(0).jamAcara);

                        Log.d("DATE",formmatedDate2);

                        Date timeFormated = tm.parse(formattedTime);
                        Date tanggalFormatted = df.parse(tgl);
                        Date dateFormmated = df.parse(formattedDate);
                    }else{
                        Log.d("ELSE 1 : ", "Hmmm");
                    }

                    if (response.raw().cacheResponse() != null) {
                        // true: response was served from cache
                        Log.d("CACHE","ADA");
                    }else{
                        Log.d("CACHE 2","ADA");
                    }

                    if (response.raw().networkResponse() != null) {
                        // true: response was served from network/server
                        Log.d("CACHE 3","ADA");
                    }else{
                        Log.d("CACHE 4","ADA");
                    }



                }catch (Exception e){
                    Log.d("ELSE 2 : ", e.toString());
                }

            }

            @Override
            public void onFailure(Call<HottestEventResponse> call, Throwable t) {
                try {
                    Log.d("FAILURE 2 : ", t.toString());
                    if (!checkConnection()) {
                        Toast.makeText(getApplicationContext(),"Tidak ada koneksi Internet",Toast.LENGTH_SHORT).show();
                        //askLoadAgainEvent();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Silahkan Buka Ulang Aplikasi.",Toast.LENGTH_SHORT).show();
                    System.exit(0);
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

    private void askLoadAgainProfile() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getApplicationContext());
        }
        builder.setTitle("Warning")
                .setMessage("Connecttion problem, want to resend?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        getProfile();
                        getHottestEvent();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void askLoadAgainEvent() {
        AlertDialog.Builder builder2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder2 = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder2 = new AlertDialog.Builder(getApplicationContext());
        }
        builder2.setTitle("Warning")
                .setMessage("Connecttion problem, want to resend?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        getHottestEvent();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


}

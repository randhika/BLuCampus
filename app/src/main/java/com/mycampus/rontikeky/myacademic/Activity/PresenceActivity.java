package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.mycampus.rontikeky.myacademic.Adapter.EventRegisteredUserAdapter;
import com.mycampus.rontikeky.myacademic.Adapter.PresenceAdapter;
import com.mycampus.rontikeky.myacademic.Adapter.SeminarAdapter;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.EventRegisteredUserResponse;
import com.mycampus.rontikeky.myacademic.Response.PresenceResponse;
import com.mycampus.rontikeky.myacademic.Response.SeminarResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresenceActivity extends AppCompatActivity {

    private PresenceAdapter adapter;

    protected List<PresenceResponse> presenceResponses = new ArrayList<>();
    protected List<PresenceResponse.Pivot> pivots = new ArrayList<>();

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";
    String token;
    public int current_page;
    public int total_prev_page;
    public String next_page_url;

    String extrasSlug;

    PrefHandler prefHandler;
    FontHandler fontHandler;

    //All About Progress and Loading
    ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView title;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewPresence);
        title = (TextView)findViewById(R.id.titleEvent);

        prefHandler = new PrefHandler(this);
        fontHandler = new FontHandler(this);
        Typeface custom_font = fontHandler.getFont();
        Typeface custom_font_bold = fontHandler.getFontBold();
        title.setTypeface(custom_font_bold);

        token = prefHandler.getTOKEN_KEY();

        Intent result= getIntent();
        Bundle extrasResult = result.getExtras();

        if(extrasResult!=null)
        {
            extrasSlug =(String) extrasResult.get("slug");
        }



        Log.d("TOKEN",token);

        pDialog = new ProgressDialog(PresenceActivity.this);

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadUser();
    }

    private void loadUser(){
        presenceResponses.clear();
        pivots.clear();
        AcademicClient client1= ServiceGeneratorAuth2.createService(AcademicClient.class,token,PresenceActivity.this);
        //Fetch list of Seminar
        Call<PresenceResponse> call1 = client1.getUserPresence(extrasSlug);

        call1.enqueue(new Callback<PresenceResponse>() {
            @Override
            public void onResponse(Call<PresenceResponse> call, Response<PresenceResponse> response) {
                try {
                    pDialog.dismiss();
                    int i = 0;

                    Log.d("USER", response.body().toString());
//                    while (i < response.body().) {
//
//
//                        PresenceResponse presenceResponse = new PresenceResponse(response.body().)
//                        i++;
//                    }
//
//
//                    try {
//
//                        Log.d("AHAY 4", String.valueOf(adapter.getValue()));
//                    }catch (Exception e){
//                        Log.d("AHAY 2",e.getMessage());
//                    }
//
//                    adapter.notifyDataSetChanged();

                    //checkItem();
                } catch (Exception e) {
                    Log.d("CATCH : ", e.toString());
                }
            }

            @Override
            public void onFailure(Call<PresenceResponse> call, Throwable t) {
                pDialog.dismiss();
                //askLoadAgain();
                Log.d("FAILURE", t.toString());
            }
        });
    }
}

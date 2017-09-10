package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.mycampus.rontikeky.myacademic.Adapter.EventRegisteredUserAdapter;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.EventRegisteredUserResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisteredEventActivity extends AppCompatActivity {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private EventRegisteredUserAdapter adapter;

    protected String[] judul, isi, slug, image, tanggal, jam, tempat, contact_person,daftar;
    protected int[] id,biaya_acara,jumlah_peserta,jumlah_perserta_tersisa;

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";
    String token;
    public int current_page;
    public int total_prev_page;
    public String next_page_url;


    //All About Progress and Loading
    ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView empty;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_event);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewRegister);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshHome);
        empty = (TextView)findViewById(R.id.single_empty);

        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");

        Log.d("TOKEN",token);

        pDialog = new ProgressDialog(RegisteredEventActivity.this);

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        loadRegisteredEvent();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRegisteredEvent();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


//        if (adapter.getItemCount() == 0){
//            Log.d("AHAY","ADA");
//        }else{
//            Log.d("AHAY","GAK ADA");
//        }

        getSupportActionBar().setTitle("Event yang diikuti");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadRegisteredEvent(){
        AcademicClient client1= ServiceGeneratorAuth2.createService(AcademicClient.class, token,RegisteredEventActivity.this);
        //Fetch list of Seminar
        Call<EventRegisteredUserResponse> call1 = client1.getResgisteredEvent();

        call1.enqueue(new Callback<EventRegisteredUserResponse>() {
            @Override
            public void onResponse(Call<EventRegisteredUserResponse> call, Response<EventRegisteredUserResponse> response) {
                try {
                    pDialog.dismiss();
                    int i = 0;
                    current_page = response.body().currentPage;
                    total_prev_page = response.body().data.size();
                    next_page_url = (String) response.body().nextPageUrl;
                    judul = new String[response.body().data.size()];
                    isi = new String[response.body().data.size()];
                    id = new int[response.body().data.size()];
                    image = new String[response.body().data.size()];
                    tanggal = new String[response.body().data.size()];
                    jam = new String[response.body().data.size()];
                    slug = new String[response.body().data.size()];
                    tempat = new String[response.body().data.size()];
                    contact_person = new String[response.body().data.size()];
                    daftar = new String[response.body().data.size()];
                    jumlah_peserta = new int[response.body().data.size()];
                    jumlah_perserta_tersisa = new int[response.body().data.size()];
                    biaya_acara = new int[response.body().data.size()];
                    while (i < response.body().data.size()) {
                        id[i] = response.body().data.get(i).id;
                        judul[i] = response.body().data.get(i).judulAcara;
                        isi[i] = response.body().data.get(i).isiAcara;
                        image[i] = response.body().data.get(i).fotoAcara;
                        slug[i] = response.body().data.get(i).slug;
                        tanggal[i] = response.body().data.get(i).tanggalAcara;
                        jam[i] = response.body().data.get(i).jamAcara;
                        tempat[i] = response.body().data.get(i).tempatAcara;
                        contact_person[i] = response.body().data.get(i).contactPersonAcara;
                        daftar[i] = response.body().data.get(i).daftar;
                        jumlah_peserta[i] = response.body().data.get(i).jumlahPeserta;
                        jumlah_perserta_tersisa[i] = response.body().data.get(i).jumlahPesertaSisa;
                        biaya_acara[i] = response.body().data.get(i).biayaAcara;
                        Log.d("JUDUL", image[i]);
                        i++;
                    }
                    Log.d("JUDUL", String.valueOf(image.length));

                    adapter = new EventRegisteredUserAdapter(judul,isi,id,slug,image,tanggal,jam,tempat,biaya_acara,jumlah_peserta,jumlah_perserta_tersisa,contact_person,daftar,RegisteredEventActivity.this);

                    mRecyclerView.setAdapter(adapter);

                    try {
                        if (adapter.getItemCount() == 0){
                            Log.d("AHAY 1", String.valueOf(adapter.getItemCount()));
                            Toast.makeText(RegisteredEventActivity.this,"Belum ada event yang anda ikuti",Toast.LENGTH_LONG).show();
                            mRecyclerView.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                        }else {
                            Log.d("AHAY 3", String.valueOf(adapter.getItemCount()));
                            mRecyclerView.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        Log.d("AHAY 2",e.getMessage());
                    }

                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.d("CATCH : ", e.toString());
                }
            }

            @Override
            public void onFailure(Call<EventRegisteredUserResponse> call, Throwable t) {
                pDialog.dismiss();
                //askLoadAgain();
                Log.d("FAILURE", t.toString());
            }
        });

    }
}

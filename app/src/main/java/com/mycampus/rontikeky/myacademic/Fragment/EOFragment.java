package com.mycampus.rontikeky.myacademic.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.Adapter.EOAdapter;
import com.mycampus.rontikeky.myacademic.Adapter.SeminarAdapter;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.SeminarResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anggit on 07/10/2017.
 */

public class EOFragment extends Fragment{
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private EOAdapter adapter;

    protected String[] judul, isi, slug, image, tanggal, jam, tempat, contact_person,daftar;
    protected int[] id,biaya_acara,jumlah_peserta,jumlah_perserta_tersisa;


    protected List<SeminarResponse.Datum> seminarList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";
    String token;
    public int current_page;
    public int total_prev_page;
    public String next_page_url;

    protected CardView cvEmpty;
    protected TextView txtEmpty;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    //All About Progress and Loading
    ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    Context context;
    PrefHandler prefHandler;

    private static int firstVisibleInListview;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected EOFragment.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setRetainInstance(true);
    }

    public EOFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seminar, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSeminar);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        cvEmpty = (CardView)rootView.findViewById(R.id.cvEmpty);
        txtEmpty = (TextView)rootView.findViewById(R.id.textEmpty);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = EOFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (EOFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        //firstVisibleInListview = mLayoutManager.findFirstVisibleItemPosition();


        sharedPreferences = this.getActivity().getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");

        pDialog = new ProgressDialog(this.getActivity());

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSeminar();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        adapter = new EOAdapter(seminarList,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(adapter);

        loadSeminar();


        return rootView;
    }

    public void setRecyclerViewLayoutManager(EOFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 60);
                mCurrentLayoutManagerType = EOFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = EOFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = EOFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void loadSeminar(){
        seminarList.clear();
        AcademicClient client1= ServiceGeneratorAuth2.createService(AcademicClient.class,token,getActivity().getApplicationContext());
        //Fetch list of Seminar
        Call<SeminarResponse> call1 = client1.getEoResponse();

        call1.enqueue(new Callback<SeminarResponse>() {
            @Override
            public void onResponse(Call<SeminarResponse> call, Response<SeminarResponse> response) {
                try {
                    pDialog.dismiss();
                    int i = 0;
                    current_page = response.body().currentPage;
                    total_prev_page = response.body().data.size();
                    next_page_url = response.body().nextPageUrl;
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
                        Log.d("JUDUL",image[i]);
                        SeminarResponse.Datum seminarResponse = new SeminarResponse.Datum(response.body().data.get(i).id,response.body().data.get(i).judulAcara,response.body().data.get(i).isiAcara,
                                response.body().data.get(i).tempatAcara,response.body().data.get(i).biayaAcara,response.body().data.get(i).contactPersonAcara,
                                response.body().data.get(i).jumlahPeserta,response.body().data.get(i).tanggalAcara,response.body().data.get(i).jamAcara,
                                response.body().data.get(i).slug,response.body().data.get(i).fotoAcara,response.body().data.get(i).jumlahPesertaSisa,
                                response.body().data.get(i).daftar,response.body().data.get(i).statusAcara,response.body().data.get(i).batasAkhirDaftar);
                        seminarList.add(seminarResponse);
                        i++;
                    }
                    Log.d("JUDUL", String.valueOf(image.length));


                    try {

                        Log.d("AHAY 4", String.valueOf(adapter.getValue()));
                    }catch (Exception e){
                        Log.d("AHAY 2",e.getMessage());
                    }

                    adapter.notifyDataSetChanged();

                    checkItem();
                } catch (Exception e) {
                    Log.d("CATCH : ", e.toString());
                }
            }

            @Override
            public void onFailure(Call<SeminarResponse> call, Throwable t) {
                pDialog.dismiss();
                askLoadAgain();
                Log.d("FAILURE", t.toString());
            }
        });
    }


    private void askLoadAgain() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Warning")
                .setMessage("Connecttion problem, want to resend?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        loadSeminar();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void checkItem(){
        if (adapter.getItemCount() == 0){
            Log.d("AHAY 1", String.valueOf(adapter.getItemCount()));
            cvEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText("Belum ada seminar..");
        }else {
            Log.d("AHAY 3", String.valueOf(adapter.getItemCount()));
            cvEmpty.setVisibility(View.GONE);
        }
    }

}

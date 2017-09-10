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

import com.mycampus.rontikeky.myacademic.Adapter.WorkshopAdapter;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.WorkshopResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anggit on 18/07/2017.
 */
public class WorkshopFragment extends Fragment{

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private WorkshopAdapter adapter;

    protected String[] judul, isi, slug, image, tanggal, jam, tempat, contact_person,daftar;
    protected int[] id,biaya_acara,jumlah_peserta,jumlah_perserta_tersisa;
    protected List<WorkshopResponse.Datum> workshopList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";
    String token;

    //All About Refresh
    ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    protected CardView cvEmpty;
    protected TextView txtEmpty;

    Context context;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setRetainInstance(true);
    }

    public WorkshopFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_workshop, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewWorkshop);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshWorkshop);
        cvEmpty = (CardView)rootView.findViewById(R.id.cvEmpty);
        txtEmpty = (TextView)rootView.findViewById(R.id.textEmpty);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        sharedPreferences = this.getActivity().getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");

        pDialog = new ProgressDialog(this.getActivity());

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        adapter = new WorkshopAdapter(workshopList,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(adapter);

        loadWorkshop();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWorkshop();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return rootView;
    }


    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 60);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void loadWorkshop(){
        workshopList.clear();
        AcademicClient client1= ServiceGeneratorAuth.createService(AcademicClient.class, token);
        //Fetch list of Workshop
        Call<WorkshopResponse> call1 = client1.getWorkshop();

        call1.enqueue(new Callback<WorkshopResponse>() {
            @Override
            public void onResponse(Call<WorkshopResponse> call, Response<WorkshopResponse> response) {
                try {
                    pDialog.dismiss();
                    Log.d("WORKSHOP JUDUL", response.body().data.get(0).judulAcara);
                    int i = 0;
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

                        WorkshopResponse.Datum workshopResponse = new WorkshopResponse.Datum(response.body().data.get(i).id,response.body().data.get(i).judulAcara,response.body().data.get(i).isiAcara,
                                response.body().data.get(i).tempatAcara,response.body().data.get(i).biayaAcara,response.body().data.get(i).contactPersonAcara,
                                response.body().data.get(i).jumlahPeserta,response.body().data.get(i).tanggalAcara,response.body().data.get(i).jamAcara,
                                response.body().data.get(i).slug,response.body().data.get(i).fotoAcara,response.body().data.get(i).jumlahPesertaSisa,
                                response.body().data.get(i).daftar);
                        workshopList.add(workshopResponse);

                        Log.d("WORKSHOP SLUG " + i, String.valueOf(response.body().data.get(i).slug));
                        i++;
                    }

                    try {

                    }catch (Exception e){
                        Log.d("AHAY 2",e.getMessage());
                    }

                    adapter.notifyDataSetChanged();

                    checkItem();
                } catch (Exception e) {
                    pDialog.dismiss();
                    Log.d("CATCH : ", e.toString());
                }
            }

            @Override
            public void onFailure(Call<WorkshopResponse> call, Throwable t) {
                Log.d("WORKSHOP FAILURE : ", t.toString());
                askLoadAgain();
                pDialog.dismiss();
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
                        loadWorkshop();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void checkItem(){
        if (adapter.getItemCount() == 0){
            Log.d("AHAY 1", String.valueOf(adapter.getItemCount()));
            cvEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText("Belum ada workshop..");
        }else {
            Log.d("AHAY 3", String.valueOf(adapter.getItemCount()));
            cvEmpty.setVisibility(View.GONE);
        }
    }
}

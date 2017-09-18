package com.mycampus.rontikeky.myacademic.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.Activity.ChooseFaculty;
import com.mycampus.rontikeky.myacademic.Adapter.CustomAdapter;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.InfoResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth3;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anggit on 12/09/2017.
 */

public class HomeFragmentV2 extends Fragment{
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60; // menampilkan data sebanyak value

    private static int firstVisibleInListview;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected HomeFragmentV2.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] judul_info , isi_info , tanggal_info , slug_info, foto_info;
    protected List<InfoResponse.Datum> infoResponses = new ArrayList<>();

    protected CardView cvEmpty;
    protected TextView txtEmpty;

    private EditText search;
    private Button btnSearch;

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String faculty_key = "faculty";
    String mypref = "MYPREFRENCES";
    String fromDate_key = "fromDate";
    String toDate_key = "toDate";
    String token;
    String faculty;
    String fromDateS;
    String toDateS;
    String date;
    String facultyString;
    public int current_page;

    //Filter
    Button btnFilter;
    RadioGroup radioGroupFilter;
    RadioButton rdSelected;
    TextView infoText;

    //All About Refresh
    ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean views;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_homev2, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshHome);
        infoText = (TextView)rootView.findViewById(R.id.infoText);
        search = (EditText)rootView.findViewById(R.id.search);
        btnSearch = (Button)rootView.findViewById(R.id.search_btn);
        cvEmpty = (CardView)rootView.findViewById(R.id.cvEmpty);
        txtEmpty = (TextView)rootView.findViewById(R.id.textEmpty);
        //radioGroupFilter = (RadioGroup)rootView.findViewById(R.id.groupFilter);

        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mRecyclerView.setHasFixedSize(false);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = HomeFragmentV2.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (HomeFragmentV2.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        sharedPreferences = this.getActivity().getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");
        faculty = sharedPreferences.getString(faculty_key, "");
        fromDateS = sharedPreferences.getString(fromDate_key,"");
        toDateS = sharedPreferences.getString(toDate_key,"");

        if (sharedPreferences.contains(fromDate_key) && sharedPreferences.contains(toDate_key)) {
            date = fromDateS+"/"+toDateS;
        }else{
            date = "";
        }

        //Toast.makeText(this.getActivity(),faculty,Toast.LENGTH_LONG).show();

        pDialog = new ProgressDialog(this.getActivity());

        mAdapter = new CustomAdapter(infoResponses,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


        checkFaculty();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                infoResponses.clear();
                checkFaculty();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    filter(v.toString());
//                    return true;
//                }
//                return false;
//            }
//        });



        search.setVisibility(View.GONE);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (views == true){
                    views = false;
                    search.setText("");
                    search.setVisibility(View.GONE);
                }else{
                    search.setVisibility(View.VISIBLE);
                    views = true;
                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            // filter your list from your input
                            filter(s.toString());
                            //you can use runnable postDelayed like 500 ms to delay search text
                        }
                    });
                }

            }
        });

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(HomeFragmentV2.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = HomeFragmentV2.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = HomeFragmentV2.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = HomeFragmentV2.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
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


    private void loadInfoFaculty(String faculty){
        infoResponses.clear();
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        AcademicClient client1= ServiceGeneratorAuth3.createService(AcademicClient.class, token,getActivity().getApplicationContext());
        //Fetch list of Seminar
        Call<InfoResponse> call1 = client1.getInfoFaculty(faculty);

        call1.enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                pDialog.dismiss();
                try {
                    int i = 0;
                    judul_info = new String[response.body().data.size()];
                    isi_info = new String[response.body().data.size()];
                    tanggal_info = new String[response.body().data.size()];
                    slug_info = new String[response.body().data.size()];
                    foto_info = new String[response.body().data.size()];
                    while (i < response.body().data.size()) {
                        judul_info[i] = response.body().data.get(i).judulInfo;
                        isi_info[i] = response.body().data.get(i).isiInfo;
                        tanggal_info[i] = response.body().data.get(i).tanggalInfo;
                        slug_info[i] = response.body().data.get(i).slugUrlInfo;
                        foto_info[i] = response.body().data.get(i).fotoInfo;
                        Log.d("xxx",response.body().data.get(i).isiInfoDepan);
                        InfoResponse.Datum infoResponse = new InfoResponse.Datum(response.body().data.get(i).judulInfo,response.body().data.get(i).isiInfoDepan,
                                response.body().data.get(i).tanggalInfo,response.body().data.get(i).slugUrlInfo,response.body().data.get(i).fotoInfo,response.body().data.get(i).getIsiInfo());
                        infoResponses.add(infoResponse);
                        i++;
                    }

                    mAdapter.notifyDataSetChanged();

                    ;                   if (response.raw().cacheResponse() != null) {
                        // true: response was served from cache
                        Log.d("CACHE 9","ADA");
                    }else{
                        Log.d("CACHE 10","ADA");
                    }

                    if (response.raw().networkResponse() != null) {
                        // true: response was served from network/server
                        Log.d("CACHE 11","ADA");
                    }else{
                        Log.d("CACHE 12","ADA");
                    }

                    checkItem();
                } catch (Exception e) {
                    Log.d("INFO 2 : ", e.toString());
                }
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                pDialog.dismiss();
                askLoadAgain();
                Log.d("INFO 3 : ", t.toString());
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
                        loadInfoFaculty("baak");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


    void filter(String text){
        List<InfoResponse.Datum> temp = new ArrayList();
        for(InfoResponse.Datum d: infoResponses){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getJudulInfo().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        mAdapter.updateList(temp);
    }

    private void checkItem(){
        if (mAdapter.getItemCount() == 0){
            Log.d("AHAY 1", String.valueOf(mAdapter.getItemCount()));
            cvEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText("Tidak ada berita pertanggal "+date);
        }else {
            Log.d("AHAY 3", String.valueOf(mAdapter.getItemCount()));
            cvEmpty.setVisibility(View.GONE);
        }
    }

    private void checkFaculty(){

        facultyString = "baak";

        if (faculty.equalsIgnoreCase("BAAK")){
            Log.d("FILTER 1","LOAD BAAK");
            infoText.setText("Informasi BAAK");
            //loadInfoBaak();
            facultyString = "baak";
        } else if(faculty.equalsIgnoreCase("FTI")){
            Log.d("FILTER 2","LOAD FTI");
            infoText.setText("Informasi FTI");
            //loadInfoFti();
            facultyString = "fti";
        }else if (faculty.equalsIgnoreCase("FT")){
            Log.d("FILTER 2","LOAD FT");
            infoText.setText("Informasi FT");
            //loadInfoFt();
            facultyString = "ft";
        }else if (faculty.equalsIgnoreCase("FE")){
            Log.d("FILTER 2","LOAD FE");
            infoText.setText("Informasi FEB");
            //loadInfoFe();
            facultyString = "fe";
        }else if (faculty.equalsIgnoreCase("FIKOM")){
            Log.d("FILTER 2","LOAD FIKOM");
            infoText.setText("Informasi FIKOM");
            //loadInfoFikom();
            facultyString = "fikom";
        }else if (faculty.equalsIgnoreCase("FISIP")){
            Log.d("FILTER 2","LOAD FISIP");
            infoText.setText("Informasi FISIP");
            //loadInfoFisip();
            facultyString = "fisip";
        } else if (faculty.equalsIgnoreCase("Pascasarjana")){
            Log.d("FILTER 2","LOAD Pascasarjana");
            infoText.setText("Informasi Pascasarjana");
            //loadInfoPascasarjana();
            facultyString = "pascasarjana";
        } else if (faculty.equalsIgnoreCase("Karir")){
            Log.d("FILTER 2","LOAD FISIP");
            infoText.setText("Informasi Karir");
            //loadInfoKarir();
            facultyString = "karir";
        }else if (faculty.equalsIgnoreCase("Kemahasiswaan")){
            Log.d("FILTER 2","LOAD Kemahasiswaan");
            infoText.setText("Informasi Kemahasiswaan");
            //loadInfoKemahasiswaan();
            facultyString = "kemahasiswaan";
        }else if (faculty.equalsIgnoreCase("Executive")){
            Log.d("FILTER 2","LOAD Executive");
            infoText.setText("Informasi Kelas Karyawan");
            //loadInfoKemahasiswaan();
            facultyString = "executive";
        }else {
            Log.d("FILTER 2", "LOAD BAAK");
            infoText.setText("Informasi BAAK");
            facultyString = "baak";
            //loadInfoBaak();
        }

        loadInfoFaculty(facultyString);
    }
}

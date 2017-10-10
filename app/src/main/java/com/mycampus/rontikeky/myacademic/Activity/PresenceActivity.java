package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.mycampus.rontikeky.myacademic.Adapter.PresenceAdapter;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.InfoResponse;
import com.mycampus.rontikeky.myacademic.Response.PresenceResponse;
import com.mycampus.rontikeky.myacademic.Response.PresenceUserResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ErrorRegisterUtil;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;
import com.mycampus.rontikeky.myacademic.Service.DownloadService;
import com.mycampus.rontikeky.myacademic.Util.Download;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    String extrasTitle;

    PrefHandler prefHandler;
    FontHandler fontHandler;

    //All About Progress and Loading
    ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView title, emptyMessage;
    Button btnSave;
    ImageView btnSearch;
    EditText etSearch;
    CardView cvEmpty;
    RecyclerView mRecyclerView;

    HashMap<String,HashMap<String,String>> parentMap;
    HashMap<String,String> map;

    JSONObject jsonObject;

    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewPresence);
        title = (TextView)findViewById(R.id.titleEvent);
        btnSave = (Button)findViewById(R.id.btnSavePresence);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        cvEmpty = (CardView)findViewById(R.id.cvEmptyCard);
        emptyMessage = (TextView)findViewById(R.id.emptyMessage);
        //etSearch = (EditText)findViewById(R.id.searchUser);

        cvEmpty.setVisibility(View.GONE);

        prefHandler = new PrefHandler(this);
        fontHandler = new FontHandler(this);
        Typeface custom_font = fontHandler.getFont();
        Typeface custom_font_bold = fontHandler.getFontBold();
        title.setTypeface(custom_font_bold);
        emptyMessage.setTypeface(custom_font_bold);

        mProgressBar.setVisibility(View.GONE);

        token = prefHandler.getTOKEN_KEY();

        Intent result= getIntent();
        Bundle extrasResult = result.getExtras();

        if(extrasResult!=null)
        {
            extrasSlug =(String) extrasResult.get("slug");
            extrasTitle = (String)extrasResult.get("title");
        }

        title.setText(extrasTitle);

        Log.d("TOKEN",token);

        pDialog = new ProgressDialog(PresenceActivity.this);

        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PresenceAdapter(PresenceActivity.this,presenceResponses);
        mRecyclerView.setAdapter(adapter);
        loadUser();
        map = new HashMap<String, String>();
        parentMap = new HashMap<>();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                while (i < adapter.getPresenceResponses().size()){
                    Log.d("HASIL","USER "+adapter.getPresenceResponses().get(i).getNama()+"- Kehadiran : "+adapter.getPresenceResponses().get(i).pivot.getStatusUser()+ " - User ID :"+adapter.getPresenceResponses().get(i).getId());
                    map.put(adapter.getPresenceResponses().get(i).getId()+"",adapter.getPresenceResponses().get(i).pivot.getStatusUser()+"");
                    i++;
                }


                parentMap.put("hadir",map);

                jsonObject = new JSONObject(parentMap);
                doSavePresence(jsonObject);
            }
        });

//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                // filter your list from your input
//                filter(s.toString());
//                //you can use runnable postDelayed like 500 ms to delay search text
//            }
//        });

        getSupportActionBar().setTitle("Absensi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerReceiver();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_print_option,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.pdf :
                Toast.makeText(PresenceActivity.this,"Downloading PDF..",Toast.LENGTH_LONG).show();
                doPrint("pdf");
                DownloadService.token = token;
                DownloadService.extrasSlug = extrasSlug;
                DownloadService.optionFormatPrint = "pdf";
                DownloadService.context = PresenceActivity.this;

                startDownload();

                break;
            case R.id.xls :
                Toast.makeText(PresenceActivity.this,"Downloading XLS..",Toast.LENGTH_LONG).show();
                doPrint("xls");
                break;
            case android.R.id.home :
                finish();
                break;
            default:
                Toast.makeText(PresenceActivity.this,"PDF DEFAULT",Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void loadUser(){
        presenceResponses.clear();
        AcademicClient client1= ServiceGeneratorAuth2.createService(AcademicClient.class,token,PresenceActivity.this);
        //Fetch list of Seminar
        Call<List<PresenceResponse>> call1 = client1.getUserPresence(extrasSlug);

        call1.enqueue(new Callback<List<PresenceResponse>>() {
            @Override
            public void onResponse(Call<List<PresenceResponse>> call, Response<List<PresenceResponse>> response) {
                try {
                    pDialog.dismiss();

                    if (response.isSuccessful()){
                        int i = 0;

                        while (i < response.body().size()) {

                            PresenceResponse presenceResponse = new PresenceResponse(response.body().get(i).getId(),response.body().get(i).getNama(),response.body().get(i).getUsername(),response.body().get(i).getTelepon(),response.body().get(i).pivot);
                            presenceResponses.add(presenceResponse);
                            i++;
                        }

                        i = 0;
                        while (i < presenceResponses.size()){
                            Log.d("test", String.valueOf(presenceResponses.get(i).getNama()));
                            i++;
                        }

                        adapter.notifyDataSetChanged();
                    }else{
                        PresenceUserResponse errorResponse = ErrorRegisterUtil.parseErrorPresence(response);

                        if (errorResponse.getStatus().equalsIgnoreCase("false")){
                            cvEmpty.setVisibility(View.VISIBLE);
                        }else{
                            cvEmpty.setVisibility(View.GONE);
                        }

                    }

                } catch (Exception e) {
                    Log.d("CATCH : ", e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<PresenceResponse>> call, Throwable t) {
                pDialog.dismiss();
                cvEmpty.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.GONE);
                //askLoadAgain();
                Log.d("FAILURE", t.toString());
            }
        });
    }

    private void doSavePresence(JSONObject jsonObject){
        mRecyclerView.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        RequestBody requestBody = null;
        try{
            requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());

        }catch (Exception e){
            Log.d("E S",e.toString());
        }

        AcademicClient client = ServiceGeneratorAuth2.createService(AcademicClient.class,token,PresenceActivity.this);
        Call<PresenceUserResponse> call = client.doUserPresence(extrasSlug,requestBody);
        call.enqueue(new Callback<PresenceUserResponse>() {
            @Override
            public void onResponse(Call<PresenceUserResponse> call, Response<PresenceUserResponse> response) {
                try{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()){
                        Toast.makeText(PresenceActivity.this,"Berhasil memyimpan presensi..",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PresenceActivity.this,"Gagal memyimpan presensi..",Toast.LENGTH_SHORT).show();
                        Log.d("E","GAGAL");
                    }
                }catch (Exception e){
                    Log.d("E",e.toString());
                }
            }

            @Override
            public void onFailure(Call<PresenceUserResponse> call, Throwable t) {
                Toast.makeText(PresenceActivity.this,"Gagal memyimpan presensi..",Toast.LENGTH_SHORT).show();
                mRecyclerView.setVisibility(View.VISIBLE);
                btnSave.setEnabled(false);
                btnSave.setBackgroundColor(Color.RED);
                btnSave.setTextColor(Color.WHITE);
                mProgressBar.setVisibility(View.GONE);
                Log.d("E",t.toString());
            }
        });
    }

    private void doPrint(final String optionFormatPrint){
        AcademicClient client = ServiceGeneratorAuth2.createService(AcademicClient.class,token,PresenceActivity.this);
        Call<ResponseBody> call = client.downloadPresenceUser(extrasSlug,optionFormatPrint);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if (response.isSuccessful()){
                        Log.d("DOWNLOAD CONTACTING", "server contacted and has file");

                        Log.d("DOWNLOAD FORM : ",new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                        boolean writtenToDisk = writeResponseBodyToDisk(response.body(),extrasSlug,optionFormatPrint);

                        Log.d("DOWNLOAD IS SUCCESS", "file download was a success? " + writtenToDisk);

                        Toast.makeText(PresenceActivity.this,"Download Selesai,File tersimpan di folder Download..",Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("DOWNLOAD 1", "GAGAL ");
                    }
                }catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Throw 1", t.getMessage() );
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body,String extrasSlug,String format) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"BluCampus-"+extrasSlug+"."+format);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("DOWNLOAD X", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    void filter(String text){
        List<PresenceResponse> temp = new ArrayList();
        for(PresenceResponse d: presenceResponses){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getNama().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    private void checkItem(){
        if (adapter.getItemCount() == 0){
            Log.d("AHAY 1", String.valueOf(adapter.getItemCount()));
            cvEmpty.setVisibility(View.VISIBLE);
        }else {
            Log.d("AHAY 3", String.valueOf(adapter.getItemCount()));
            cvEmpty.setVisibility(View.GONE);
        }
    }

    /**********************************************************************************/

    private void startDownload(){

        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);

    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");
                mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                    Toast.makeText(PresenceActivity.this,"Download Selesai",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PresenceActivity.this,"Download sedang jalan",Toast.LENGTH_SHORT).show();
                    //mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }
            }
        }
    };

//    private boolean checkPermission(){
//        int result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED){
//
//            return true;
//
//        } else {
//
//            return false;
//        }
//    }

//    private void requestPermission(){
//
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload();
                } else {

                    Toast.makeText(PresenceActivity.this,"GAK bOLEh",Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

}




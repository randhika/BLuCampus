package com.mycampus.rontikeky.myacademic.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.DaftarAcaraResponse;
import com.mycampus.rontikeky.myacademic.Response.DetailSeminarResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeminarDetial extends AppCompatActivity {
    TextView judul, isi, tanggal, jam, tempat, contact_person, available, biaya, urlImage,tanggal_valid;
    Button btnDaftar,btnCancel;
    ImageView imgSeminar;
    String extrasSlug;
    String token;
    ProgressDialog pDialog;
    boolean isImageFitToScreen;
    FontHandler fontHandler;
    PrefHandler prefHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar_detial);

        judul = (TextView)findViewById(R.id.judulDetail);
        isi = (TextView)findViewById(R.id.isiDetail);
        tanggal = (TextView)findViewById(R.id.tanggalDetail);
        tanggal_valid = (TextView)findViewById(R.id.tanggalDetailValid);
        jam = (TextView)findViewById(R.id.jamDetail);
        imgSeminar = (ImageView)findViewById(R.id.imgSeminarDetail);
        tempat = (TextView)findViewById(R.id.tempat);
        contact_person = (TextView)findViewById(R.id.contact_person);
        biaya = (TextView)findViewById(R.id.biaya);
        available = (TextView)findViewById(R.id.available);
        urlImage = (TextView)findViewById(R.id.urlImage);
        btnDaftar = (Button)findViewById(R.id.btnDaftar);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        fontHandler = new FontHandler(this);
        Typeface custom_font = fontHandler.getFont();
        Typeface custom_font_bold = fontHandler.getFontBold();

        judul.setTypeface(custom_font_bold);
        isi.setTypeface(custom_font);
        tanggal.setTypeface(custom_font);
        tanggal_valid.setTypeface(custom_font);
        tempat.setTypeface(custom_font);
        contact_person.setTypeface(custom_font);
        biaya.setTypeface(custom_font);
        available.setTypeface(custom_font);


        pDialog = new ProgressDialog(SeminarDetial.this);

        prefHandler = new PrefHandler(this);
        token = prefHandler.getTOKEN_KEY();

        Intent result= getIntent();
        Bundle extrasResult = result.getExtras();

        if(extrasResult!=null)
        {
            extrasSlug =(String) extrasResult.get("slug");
        }

        loadSeminar();

        imgSeminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PENCET", "DIPENCET");
                Intent fullscreen = new Intent(SeminarDetial.this,FullScreenImageActivity.class);
                fullscreen.putExtra("url",urlImage.getText().toString());
                startActivity(fullscreen);
//                fullScreen();
            }
        });

        contact_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contact_person.getText().toString().trim()));
                startActivity(intent);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationEvent();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirmationCancel();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void addEvent2(String title, String deskripsi, String location, String date, String time){
        int hasAlarm;
        try {


            int sTime = Integer.parseInt(time.substring(8, 10));
            int eTime = Integer.parseInt(time.substring(2, 4));
            int sYear = Integer.parseInt(date.substring(0, 4));
            int sMonth = Integer.parseInt(date.substring(5, 7));
            int sDate = Integer.parseInt(date.substring(8, 10));
            int sRemainder = 6;

            Log.d("xx", sTime + " - " + eTime + " - " + date + " - " + sYear + " - " + sMonth + " - " + Calendar.AUGUST);
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(sYear, sMonth-1, sDate, eTime, 0);
            long startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(sYear, sMonth-1, sDate, sTime, 0);
            long endMillis = endTime.getTimeInMillis();

            Calendar beginRemainder = Calendar.getInstance();
            beginTime.set(sYear, sMonth-1, sDate, 6, 0);
            long remainderMillis = beginTime.getTimeInMillis();

            final ContentValues event = new ContentValues();
            event.put(CalendarContract.Events.CALENDAR_ID, 1);

            event.put(CalendarContract.Events.TITLE, title);
            event.put(CalendarContract.Events.DESCRIPTION, deskripsi);
            event.put(CalendarContract.Events.EVENT_LOCATION, location);

            event.put(CalendarContract.Events.DTSTART, startMillis);
            event.put(CalendarContract.Events.DTEND, endMillis);
            event.put(CalendarContract.Events.ALL_DAY, false);   // 0 for false, 1 for true
            event.put(CalendarContract.Events.HAS_ALARM, true); // 0 for false, 1 for true



            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);


            Uri baseUri;
            if (Build.VERSION.SDK_INT >= 8) {
                baseUri = Uri.parse("content://com.android.calendar/events");
            } else {
                baseUri = Uri.parse("content://calendar/events");
            }

            Uri uri = getApplicationContext().getContentResolver().insert(baseUri, event);

            long eventID = Long.parseLong(uri.getLastPathSegment());
            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES, 5);

            Uri uri2 = getApplicationContext().getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminders);
        }catch (Exception e){
            Log.d("borak",e.toString());
        }
    }

    private void loadSeminar(){

        final AcademicClient client = ServiceGeneratorAuth2.createService(AcademicClient.class, token,SeminarDetial.this);

        Call<DetailSeminarResponse> call = client.getSemanarDetail(extrasSlug);

        call.enqueue(new Callback<DetailSeminarResponse>() {
            @Override
            public void onResponse(Call<DetailSeminarResponse> call, Response<DetailSeminarResponse> response) {

                try {

                    int kursi_tersisa = response.body().getJumlahPeserta() - response.body().getJumlahPesertaSisa();
                    if (response.body().getJumlahPesertaSisa() == 0){
                        available.setText("Sudah Penuh");
                        available.setTextColor(Color.parseColor("#FF0000"));
                    }else{
                        available.setText(Integer.toString(kursi_tersisa) + " dari " + response.body().getJumlahPeserta());
                        available.setTextColor(Color.parseColor("#000000"));
                    }

                    String s = String.format("%,d", response.body().getBiayaAcara());

                    String udata= response.body().getContactPersonAcara();
                    SpannableString content = new SpannableString(udata);
                    content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
                    contact_person.setText(content);

                    Glide.with(getApplicationContext()).load(response.body().getFotoAcara()).into(imgSeminar);
                    judul.setText(response.body().getJudulAcara());
                    isi.setText(response.body().getIsiAcara());
                    tanggal.setText(response.body().getTanggalAcara());
                    tempat.setText(response.body().getTempatAcara());
                    jam.setText(", " + response.body().getJamAcara());
                    urlImage.setText(response.body().getFotoAcara());
                    //contact_person.setText(response.body().getContactPersonAcara());
                    //available.setText(response.body().getJumlahPesertaSisa() + " dari " + response.body().getJumlahPeserta());
                    biaya.setText("Rp." + s);

                    if (response.body().getDaftar().equalsIgnoreCase("true")) {
                        btnDaftar.setBackgroundColor(Color.parseColor("#e74c3c"));
                        btnDaftar.setText("Batal ikut?");
                        btnCancel.setVisibility(View.VISIBLE);
                        btnDaftar.setVisibility(View.GONE);
                        if (response.body().getJumlahPesertaSisa() == 0){
                            btnCancel.setEnabled(true);
                            btnDaftar.setEnabled(false);
                        }else{
                            btnCancel.setEnabled(true);
                            btnDaftar.setEnabled(true);
                        }
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_clear_24dp);
                        btnDaftar.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    } else {
                        btnDaftar.setBackgroundColor(Color.parseColor("#303F9F"));
                        btnDaftar.setText("Daftar");
                        btnCancel.setEnabled(false);
                        btnCancel.setVisibility(View.GONE);
                        btnDaftar.setVisibility(View.VISIBLE);
                        if (response.body().getJumlahPesertaSisa() == 0){
                            btnDaftar.setEnabled(false);
                            btnDaftar.setText("Maaf, sudah penuh..");
                            btnDaftar.setBackgroundColor(Color.parseColor("#ff0000"));
                        }else{
                            btnDaftar.setText("Daftar");
                            btnDaftar.setBackgroundColor(Color.parseColor("#303F9F"));
                            btnCancel.setEnabled(true);
                            btnDaftar.setEnabled(true);
                        }
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_24dp);
                        btnDaftar.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    }

                    Calendar c = Calendar.getInstance();

                    String jam = response.body().getJamAcara();
                    String subjam = jam.substring(6,11);
                    String tglBatas = response.body().batasAkhirDaftar.substring(0,10);

                    String tgl = response.body().tanggalAcara;

                    SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

                    String formattedTime = tm.format(c.getTime());
                    String formattedDate = df.format(c.getTime());
                    String formmatedDate2 = vd.format(df.parse(tgl));

                    Log.d("DATE",subjam);

                    Date jamFormmated = tm.parse(subjam);

                    Date tanggalFormatted = df.parse(tgl);
                    Date tanggalFormattedBatas = df.parse(tglBatas);
                    Date dateFormmated = df.parse(formattedDate);

                    //BATAS JAM
                    String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
                    String timeToCompare = response.body().batasAkhirDaftar.substring(10,16);
                    Log.d("BATAS",timeToCompare);

                    Date jamFor = tm.parse(currentTime);
                    Date jamFor2 = tm.parse(timeToCompare);

                    if (dateFormmated.compareTo(tanggalFormattedBatas) > 0){
                        Log.d("WAKTU AU : ","TANGGAL EVENT TELAH BERAKHIR");
                        btnDaftar.setText("Pendaftaran sudah ditutup..");
                        btnCancel.setText("Pendaftaran sudah ditutup..");
                        btnDaftar.setEnabled(false);
                        btnCancel.setEnabled(false);
                        btnDaftar.setBackgroundColor(Color.parseColor("#ff0000"));
                        btnCancel.setBackgroundColor(Color.parseColor("#ff0000"));
                    }else{
                        if (dateFormmated.compareTo(tanggalFormattedBatas) == 0){
                            if (jamFor.compareTo(jamFor2) > 0){
                                Log.d("WAKTU x : ","TANGGAL EVENT TELAH BERAKHIR");
                                btnDaftar.setText("Pendaftaran sudah ditutup..");
                                btnCancel.setText("Pendaftaran sudah ditutup..");
                                btnDaftar.setEnabled(false);
                                btnCancel.setEnabled(false);
                                btnDaftar.setBackgroundColor(Color.parseColor("#ff0000"));
                                btnCancel.setBackgroundColor(Color.parseColor("#ff0000"));
                            }else{
                                Log.d("WAKTU y : ","TANGGAL EVENT BELUM BERAKHIR");
                            }
                        }
                        Log.d("WAKTU AH : ","TANGGAL EVENT BELUM BERAKHIR");
                    }



                    if (dateFormmated.compareTo(tanggalFormatted) > 0){
                        Log.d("WAKTU 4 : ","TANGGAL EVENT TELAH BERAKHIR");
                        btnDaftar.setText("Event Sudah Berakhir");
                        btnCancel.setText("Event Sudah Berakhir");
                        btnDaftar.setEnabled(false);
                        btnCancel.setEnabled(false);
                        btnDaftar.setBackgroundColor(Color.parseColor("#ff0000"));
                        btnCancel.setBackgroundColor(Color.parseColor("#ff0000"));
                    }else{
                        if (dateFormmated.compareTo(tanggalFormattedBatas) == 0){
                            if (jamFor.compareTo(jamFor2) > 0){
                                Log.d("WAKTU x : ","TANGGAL EVENT TELAH BERAKHIR");
                                btnDaftar.setText("Pendaftaran sudah ditutup..");
                                btnCancel.setText("Pendaftaran sudah ditutup..");
                                btnDaftar.setEnabled(false);
                                btnCancel.setEnabled(false);
                                btnDaftar.setBackgroundColor(Color.parseColor("#ff0000"));
                                btnCancel.setBackgroundColor(Color.parseColor("#ff0000"));
                            }else{
                                Log.d("WAKTU y : ","TANGGAL EVENT BELUM BERAKHIR");
                            }
                        }
                        Log.d("WAKTU AH : ","TANGGAL EVENT BELUM BERAKHIR");
                    }

                    tanggal_valid.setText(formmatedDate2);

                    Log.d("WAKTU 1 : ",subjam);
                    Log.d("WAKTU 3 : ",formattedTime);

                    Log.d("JUDUL : ", response.body().getJudulAcara());
                    Log.d("ISI : ", response.body().getIsiAcara());
                    Log.d("TANGGAL : ", response.body().getTanggalAcara());
                    getSupportActionBar().setTitle(response.body().getJudulAcara());
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } catch (Exception e) {
                    Log.d("EXCEPTION : ", e.toString());
                }
            }

            @Override
            public void onFailure(Call<DetailSeminarResponse> call, Throwable t) {
                Log.d("FAILURE : ", t.toString());
            }
        });
    }

    private void registrationEvent(){
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client1 = ServiceGeneratorAuth2.createService(AcademicClient.class, token,SeminarDetial.this);

        Call<DaftarAcaraResponse> call1 = client1.doRegisterEvent(extrasSlug);

        call1.enqueue(new Callback<DaftarAcaraResponse>() {
            @Override
            public void onResponse(Call<DaftarAcaraResponse> call, Response<DaftarAcaraResponse> response) {
                try {
                    pDialog.dismiss();
                    if (response.isSuccessful()) {
                        btnCancel.setVisibility(View.VISIBLE);
                        btnDaftar.setVisibility(View.GONE);
                        addEvent2(judul.getText().toString(),isi.getText().toString(),tempat.getText().toString(),tanggal.getText().toString(),jam.getText().toString());
                        Toast.makeText(getApplicationContext(),"Event telah ditambahkan ke kalendar anda",Toast.LENGTH_SHORT).show();
                        showDialog();
                    }

                } catch (Exception e) {
                    Log.d("TRY 2", e.toString());
                }
            }

            @Override
            public void onFailure(Call<DaftarAcaraResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("TRY 3", t.toString());
            }
        });
    }

    private void cancelParticipation(){
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();

        AcademicClient client1 = ServiceGeneratorAuth2.createService(AcademicClient.class, token,SeminarDetial.this);

        Call<DaftarAcaraResponse> call1 = client1.doCancelEvent(extrasSlug);

        call1.enqueue(new Callback<DaftarAcaraResponse>() {
            @Override
            public void onResponse(Call<DaftarAcaraResponse> call, Response<DaftarAcaraResponse> response) {
                try {
                    pDialog.dismiss();
                    if (response.isSuccessful()) {
                        btnCancel.setVisibility(View.GONE);
                        btnDaftar.setVisibility(View.VISIBLE);
                        showDialogCancel();
                    }
                } catch (Exception e) {
                    Log.d("TRY 2", e.toString());
                }
            }

            @Override
            public void onFailure(Call<DaftarAcaraResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.d("TRY 3", t.toString());
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SeminarDetial.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SeminarDetial.this);
        }
        builder.setTitle("Selamat!")
                .setMessage("Anda berhasil berpartisipasi dalam event!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        loadSeminar();
                    }
                })
                .show();
    }

    private void showDialogConfirmationCancel(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SeminarDetial.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SeminarDetial.this);
        }
        builder.setTitle("Pesan..")
                .setMessage("Anda yakin ingin membatalkan partisipasi dalam event ini?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        cancelParticipation();
                        loadSeminar();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void showDialogCancel(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SeminarDetial.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SeminarDetial.this);
        }
        builder.setTitle("Selamat!")
                .setMessage("Anda berhasil membatalkan partisipasi event!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        loadSeminar();
                    }
                })
                .show();
    }

    public void fullScreen() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("AHAY", "Turning immersive mode mode off. ");
        } else {
            Log.i("AHAY", "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}

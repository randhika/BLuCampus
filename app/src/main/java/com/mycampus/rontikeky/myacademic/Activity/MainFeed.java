package com.mycampus.rontikeky.myacademic.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponse;
import com.mycampus.rontikeky.myacademic.RestApi.AcademicClient;
import com.mycampus.rontikeky.myacademic.RestApi.ServiceGeneratorAuth2;
import com.mycampus.rontikeky.myacademic.Tab.MyAdapter;
import com.mycampus.rontikeky.myacademic.Tab.SlidingTabLayout;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFeed extends AppCompatActivity {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    //Drawer
    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;

    SharedPreferences sharedPreferences,sharedPreferences2;
    String token_key = "token";
    String nama_key = "nama";
    String email_key = "email";
    String mypref = "MYPREFRENCES";
    String fromDate_key = "fromDate";
    String toDate_key = "toDate";
    String token;
    String nama_feed;
    String email_feed;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setLogo(R.drawable.splashfix);
        getSupportActionBar().setTitle("Main Feed");

        mViewPager=(ViewPager)findViewById(R.id.vp_tabs);
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), this));

        mSlidingTabLayout=(SlidingTabLayout)findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
        mSlidingTabLayout.setViewPager(mViewPager);

        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(mypref, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");


        AcademicClient client = ServiceGeneratorAuth2.createService(AcademicClient.class,token,MainFeed.this);
        Call<ProfileResponse> call1 = client.getProfile();

        call1.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                try {

                    Log.d("NAMA : ", response.body().getNama());
                    Log.d("EMAIL : ", response.body().getEmail());

                    nama_feed = response.body().getNama();
                    email_feed = response.body().getEmail();

                    if (!sharedPreferences.contains(nama_key) && !sharedPreferences.contains(email_key)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(nama_key, nama_feed);
                        editor.putString(email_key, email_key);
                        editor.commit();
                        Log.d("RESPONSE","SHARED");
                    }else{
                        Log.d("RESPONSE 2","NOT SHARED");
                    }
                } catch (Exception e) {
                    Log.d("EXCEPTION PROFILE", e.toString());
                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.d("FAILURE 1 : ", t.toString());
            }
        });

        nama_feed = sharedPreferences.getString(nama_key,"");
        email_feed = sharedPreferences.getString(email_key,"");

        Log.d("PROFILE", nama_feed +" "+ email_feed);
        //Toast.makeText(getApplicationContext(),"Selamat Datang "+nama_feed,Toast.LENGTH_LONG).show();
        //=========================================================================================
        headerNavigationLeft = new AccountHeader().withActivity(this).withCompactStyle(false).withSavedInstance(savedInstanceState).withHeaderBackground(R.drawable.userprofilebg).addProfiles(
                new ProfileDrawerItem().withName(nama_feed).withEmail(email_feed).withIcon(getResources().getDrawable(R.drawable.nopicture))
        ).build();

        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withAccountHeader(headerNavigationLeft)
                .withSelectedItem(0)
                .build();

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Profil").withIdentifier(0).withIcon(getResources().getDrawable(R.drawable.ic_face_black_24dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Event terdaftar").withIdentifier(1).withIcon(getResources().getDrawable(R.drawable.ic_receipt_24dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("About").withIdentifier(3).withIcon(getResources().getDrawable(R.drawable.ic_info_outline_black_24dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Logout").withIdentifier(2).withIcon(getResources().getDrawable(R.drawable.logout)));

        //Sharedprefrences object
        sharedPreferences = getSharedPreferences(mypref, Context.MODE_PRIVATE);

        navigationDrawerLeft.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                Intent intent = null;
                switch (drawerItem.getIdentifier()) {
                    case 0:
                        SharedPreferences preferences1 = getSharedPreferences(token_key, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = preferences1.edit();
                        editor1.remove(fromDate_key);
                        editor1.remove(toDate_key);
                        editor1.commit();
                        intent = new Intent(MainFeed.this, EditProfil.class);
                        startActivity(intent);
                        break;
                    case 1:
                        SharedPreferences preferences2 = getSharedPreferences(token_key, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = preferences2.edit();
                        editor2.remove(fromDate_key);
                        editor2.remove(toDate_key);
                        editor2.commit();
                        intent = new Intent(MainFeed.this, RegisteredEventActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        SharedPreferences preferences4 = getSharedPreferences(token_key, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor4 = preferences4.edit();
                        editor4.remove(fromDate_key);
                        editor4.remove(toDate_key);
                        editor4.commit();
                        intent = new Intent(MainFeed.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        SharedPreferences preferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
//                        Intent i = getBaseContext().getPackageManager()
//                                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
//                        finish();

                        Intent i=new Intent(MainFeed.this,splash_screen.class);
                        i.setAction(Intent.ACTION_MAIN);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        try {startActivity(i);}
                        catch (java.lang.Exception ignore) {}
                        System.exit(0);

//                        doRestart(MainFeed.this);
                        //doRestart2();
                        Toast.makeText(getApplicationContext(),"Anda Berhasil Logout",Toast.LENGTH_LONG).show();
                    default:
                        Log.d("Test","Default");
                        break;
                }
            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            SharedPreferences preferences3 = getSharedPreferences(token_key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor3 = preferences3.edit();
            editor3.remove(fromDate_key);
            editor3.remove(toDate_key);
            editor3.commit();
            moveTaskToBack(true);
            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e("TEST 1", "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e("TEST 2", "Was not able to restart application, PM null");
                }
            } else {
                Log.e("TEST 3", "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e("TEST 4", "Was not able to restart application");
        }
    }

    private void doRestart2(){
        Intent mStartActivity = new Intent(MainFeed.this, splash_screen.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(MainFeed.this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) MainFeed.this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}

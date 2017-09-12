package com.mycampus.rontikeky.myacademic.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.Fragment.FtiFragment;
import com.mycampus.rontikeky.myacademic.Fragment.HomeFragment;
import com.mycampus.rontikeky.myacademic.Fragment.HomeFragmentV2;
import com.mycampus.rontikeky.myacademic.Fragment.SeminarFragment;
import com.mycampus.rontikeky.myacademic.Fragment.WorkshopFragment;
import com.mycampus.rontikeky.myacademic.R;

public class MainFeedDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";
    String faculty_key = "faculty";
    String fromDate_key = "fromDate";
    String toDate_key = "toDate";
    String nama_key = "nama";
    String email_key = "email";
    String token;
    String faculty;
    String fromDateS;
    String toDateS;
    String nama_feed;
    String email_feed;

    TextView tvNameHeader, tvEmailHeader;

    Fragment fragment;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("BLu Feed");



        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(token_key, "");
        nama_feed = sharedPreferences.getString(nama_key,"");
        email_feed = sharedPreferences.getString(email_key,"");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        tvNameHeader = (TextView)view.findViewById(R.id.nameHeader);
        tvEmailHeader = (TextView)view.findViewById(R.id.emailHeader);

        Log.d("PROFILE : ",nama_feed + " : " + email_feed);

        tvNameHeader.setText(nama_feed);
        tvEmailHeader.setText(email_feed);

        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new HomeFragmentV2()).commit();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_feed_drawer, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        NavigationView nv= (NavigationView) findViewById(R.id.nav_view);
        Menu m=nv.getMenu();
        int id = item.getItemId();

        fragment = null;

        if (id == R.id.nav_info) {
            boolean b=!m.findItem(R.id.nav_baak).isVisible();
            m.findItem(R.id.nav_baak).setVisible(b);
            m.findItem(R.id.nav_fti).setVisible(b);
            m.findItem(R.id.nav_ft).setVisible(b);
            m.findItem(R.id.nav_fe).setVisible(b);
            m.findItem(R.id.nav_fikom).setVisible(b);
            return true;
        } else if (id == R.id.nav_seminar) {
            fragment = new SeminarFragment();
        } else if (id == R.id.nav_workshop) {
            fragment = new WorkshopFragment();
        } else if (id == R.id.nav_baak) {
            prefEditor("BAAK");
        } else if (id == R.id.nav_fti) {
            prefEditor("FTI");
        }else if (id == R.id.nav_ft) {
            prefEditor("FT");
        }else if (id == R.id.nav_fe) {
            prefEditor("FE");
        }else if (id == R.id.nav_fikom) {
            prefEditor("FIKOM");
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainFeedDrawer.this, AboutActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {

            SharedPreferences preferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent i=new Intent(MainFeedDrawer.this,splash_screen.class);
            i.setAction(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            try {startActivity(i);}
            catch (java.lang.Exception ignore) {}
            System.exit(0);

            Toast.makeText(getApplicationContext(),"Anda Berhasil Logout",Toast.LENGTH_LONG).show();
        }else if (id == R.id.nav_profile){
            Intent intent = new Intent(MainFeedDrawer.this,EditProfil.class);
            startActivity(intent);
        }else if (id == R.id.nav_registered_event){
            Intent intent = new Intent(MainFeedDrawer.this,RegisteredEventActivity.class);
            startActivity(intent);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prefEditor(String faculty){
        sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(faculty_key, faculty);
        editor.commit();
        fragment = new HomeFragmentV2();
    }
}

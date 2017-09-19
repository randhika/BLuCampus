package com.mycampus.rontikeky.myacademic.Activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.Fragment.FtiFragment;
import com.mycampus.rontikeky.myacademic.Fragment.HomeFragment;
import com.mycampus.rontikeky.myacademic.Fragment.HomeFragmentV2;
import com.mycampus.rontikeky.myacademic.Fragment.SeminarFragment;
import com.mycampus.rontikeky.myacademic.Fragment.WorkshopFragment;
import com.mycampus.rontikeky.myacademic.R;

public class MainFeedDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String token;
    String nama_feed;
    String email_feed;

    PrefHandler prefHandler;

    TextView tvNameHeader, tvEmailHeader;
    ImageView ivDisplayHeader;


    Fragment fragment;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("BLuFeed");


        prefHandler = new PrefHandler(this);
        token = prefHandler.getTOKEN_KEY();
        nama_feed = prefHandler.getNAME_KEY();
        email_feed = prefHandler.getEMAIL_KEY();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        tvNameHeader = (TextView)view.findViewById(R.id.nameHeader);
        tvEmailHeader = (TextView)view.findViewById(R.id.emailHeader);
        ivDisplayHeader = (ImageView)view.findViewById(R.id.imageHeader);


        navigationView.setCheckedItem(R.id.nav_info);

        Log.d("PROFILE : ",nama_feed + " : " + email_feed);

        tvNameHeader.setText(nama_feed);
        tvEmailHeader.setText(email_feed);

        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new HomeFragmentV2()).commit();


        ivDisplayHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });

    }

    //Hasil Select Image dari gallery dan foto(Not Fix Yet)
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ivDisplayHeader.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ivDisplayHeader.setImageURI(selectedImage);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            SharedPreferences preferences3 = getSharedPreferences(token_key, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor3 = preferences3.edit();
//            editor3.remove(fromDate_key);
//            editor3.remove(toDate_key);
//            editor3.commit();
            prefHandler.clearFROM_DATE_KEY();
            prefHandler.clearTO_DATE_KEY();
            moveTaskToBack(true);
            finish();

            //android.os.Process.killProcess(android.os.Process.myPid());
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




    //Mengatur Navigation Drawer Berdasarkan ID MENU MAIN_DRAWER_MENU
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
            m.findItem(R.id.nav_fisip).setVisible(b);
            m.findItem(R.id.nav_karir).setVisible(b);
            m.findItem(R.id.nav_executive).setVisible(b);
            m.findItem(R.id.nav_kemahasiswaan).setVisible(b);
            m.findItem(R.id.nav_pascasarjana).setVisible(b);
            return true;
        } else if (id == R.id.nav_seminar) {
            fragment = new SeminarFragment();
        } else if (id == R.id.nav_workshop) {
            fragment = new WorkshopFragment();
        }else if (id == R.id.nav_radio) {
            Intent radioIntent = new Intent(this,StreamRadioActivity.class);
            startActivity(radioIntent);
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
        }else if (id == R.id.nav_fisip) {
            prefEditor("FISIP");
        }else if (id == R.id.nav_pascasarjana) {
            prefEditor("Pascasarjana");
            Log.d("Test","Pascasarjana");
        }else if (id == R.id.nav_karir) {
            prefEditor("Karir");
            Log.d("Test","Karir");
        }else if (id == R.id.nav_kemahasiswaan) {
            prefEditor("Kemahasiswaan");
            Log.d("Test","Kemahasiswaan");
        }else if (id == R.id.nav_executive) {
            prefEditor("Executive");
            Log.d("Test","Executive");
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainFeedDrawer.this, AboutActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {


            prefHandler.setLogout();

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
        }else if(id == R.id.nav_contactus){
            Intent i = new Intent(MainFeedDrawer.this,ContactActivity.class);
            startActivity(i);
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

    //menyimpan fakultas ke dalam SharedPref, digunakan untuk memanggil info fakultas berdasarkan nilai fakultas yang disimpan
    private void prefEditor(String faculty){

        prefHandler.setFACULTY_KEY(faculty);

        fragment = new HomeFragmentV2();
    }
}

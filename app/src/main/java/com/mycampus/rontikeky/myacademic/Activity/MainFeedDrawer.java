package com.mycampus.rontikeky.myacademic.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.Fragment.EOFragment;
import com.mycampus.rontikeky.myacademic.Fragment.FtiFragment;
import com.mycampus.rontikeky.myacademic.Fragment.HomeFragment;
import com.mycampus.rontikeky.myacademic.Fragment.HomeFragmentV2;
import com.mycampus.rontikeky.myacademic.Fragment.SeminarFragment;
import com.mycampus.rontikeky.myacademic.Fragment.WorkshopFragment;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Util.CircleTransformation;
import com.mycampus.rontikeky.myacademic.Util.ImageFilePath;

import java.io.IOException;

public class MainFeedDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    String token;
    String nama_feed;
    String email_feed;
    String auth_eo;
    String count_eo;

    PrefHandler prefHandler;
    FontHandler fontHandler;

    TextView tvNameHeader, tvEmailHeader;
    ImageView ivDisplayHeader;

    private GoogleApiClient mGoogleApiClient;

    Fragment fragment;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("BLuFeed");

        fontHandler = new FontHandler(this);
        Typeface custom_font = fontHandler.getFont();


        prefHandler = new PrefHandler(this);
        token = prefHandler.getTOKEN_KEY();
        nama_feed = prefHandler.getNAME_KEY();
        email_feed = prefHandler.getEMAIL_KEY();
        auth_eo = prefHandler.getAUTHORIZATION_EO_KEY();
        count_eo = prefHandler.getEO_COUNT_KEY();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.nav_eo);


        /*Google Setup Initialization*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        /*End Google Setup Initialization*/

        tvNameHeader = (TextView)view.findViewById(R.id.nameHeader);
        tvEmailHeader = (TextView)view.findViewById(R.id.emailHeader);
        ivDisplayHeader = (ImageView)view.findViewById(R.id.imageHeader);
        tvNameHeader.setTypeface(custom_font);
        tvEmailHeader.setTypeface(custom_font);

        navigationView.setCheckedItem(R.id.nav_info);



        if (auth_eo.equalsIgnoreCase("4")){
            if (!count_eo.equalsIgnoreCase("0")){
                target.setVisible(true);
            }
        }else{
            target.setVisible(false);
        }

        Log.d("PROFILE : ",nama_feed + " : " + email_feed);

        Glide.with(MainFeedDrawer.this).load(prefHandler.getIMAGE_PROFILE_KEY())
                .centerCrop().error(R.drawable.nopicture).placeholder(R.drawable.nopicture).transform(new CircleTransformation(MainFeedDrawer.this))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivDisplayHeader);

        tvNameHeader.setText(nama_feed);
        tvEmailHeader.setText(email_feed);

        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new HomeFragmentV2()).commit();


        ivDisplayHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPhoto(ivDisplayHeader,50,50);
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(MainFeedDrawer.this).load(prefHandler.getIMAGE_PROFILE_KEY())
                .centerCrop().error(R.drawable.nopicture).placeholder(R.drawable.nopicture).transform(new CircleTransformation(MainFeedDrawer.this))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivDisplayHeader);
    }

    private void loadPhoto(ImageView imageView, int width, int height) {

        ImageView tempImageView = imageView;


        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });


        imageDialog.create();
        imageDialog.show();
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
        }else if(id == R.id.nav_eo){
            fragment = new EOFragment();
        }
//        else if (id == R.id.nav_radio) {
//            Intent radioIntent = new Intent(this,StreamRadioActivity.class);
//            startActivity(radioIntent);
//        }
        else if (id == R.id.nav_baak) {
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
            signOut();
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

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("Status", String.valueOf(status));
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Con", "onConnectionFailed:" + connectionResult);
    }
}

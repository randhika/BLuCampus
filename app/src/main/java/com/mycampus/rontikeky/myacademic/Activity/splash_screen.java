package com.mycampus.rontikeky.myacademic.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.mycampus.rontikeky.myacademic.R;

public class splash_screen extends AppCompatActivity {
    //Set waktu lama splashscreen
    private static int splashInterval = 2000;
    SharedPreferences sharedPreferences;
    String token_key = "token";
    String mypref = "MYPREFRENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        sharedPreferences = getSharedPreferences(mypref, Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub

                sharedPreferences = getSharedPreferences(token_key, Context.MODE_PRIVATE);
                if (sharedPreferences.contains(token_key)){

                    Intent intent = new Intent(splash_screen.this, transition.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(splash_screen.this, MainActivity.class);
                    startActivity(intent);
                }

                //jeda selesai Splashscreen
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);

    };

}

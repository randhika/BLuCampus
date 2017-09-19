package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mycampus.rontikeky.myacademic.R;

import java.io.IOException;

public class StreamRadioActivity extends AppCompatActivity {
    ImageView btnPlay,btnStop;
    boolean isPlay;

    MediaPlayer mp;
    ProgressDialog pDialog;
    private final String URL_RADIO = "http://us2.internet-radio.com:8046/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_radio);

        getSupportActionBar().hide();

        btnPlay = (ImageView)findViewById(R.id.btnPlay);
        btnStop = (ImageView) findViewById(R.id.btnStopNormal);

        pDialog = new ProgressDialog(StreamRadioActivity.this);

        radioConfig();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pDialog.show();
                isPlay = true;
                Log.d("TES","Radio is On!");
                isRadioPlay();
                radioConfig();
                startRadio();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPlay = false;
                Log.d("TES","Radio is Off!");
                isRadioPlay();
                stopRadio();
            }
        });


    }

    private void isRadioPlay(){
        if (isPlay){
            btnStop.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.GONE);
            pDialog.dismiss();
        }else{
            btnPlay.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
        }

    }

    private void radioConfig(){
         mp = new MediaPlayer();
        try {
            mp.setDataSource(URL_RADIO);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void startRadio(){
        try {
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void stopRadio(){
        mp.pause();
//        try {
//            mp.prepare();
//            mp.stop();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
}

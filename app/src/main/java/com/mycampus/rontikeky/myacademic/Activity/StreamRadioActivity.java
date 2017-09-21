package com.mycampus.rontikeky.myacademic.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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

    private boolean playPause;
    private MediaPlayer mediaPlayer;
    /**
     * remain false till media is not completed, inside OnCompletionListener make it true.
     */
    private boolean intialStage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_radio);

        getSupportActionBar().hide();

        btnPlay = (ImageView)findViewById(R.id.btnPlay);
        btnStop = (ImageView) findViewById(R.id.btnStopNormal);

        pDialog = new ProgressDialog(StreamRadioActivity.this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playPause) {
                    btnPlay.setBackgroundResource(R.drawable.stop_normal);
                    if (intialStage)
                        new Player()
                                .execute(URL_RADIO);
                    else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }
                    playPause = true;
                } else {
                    btnPlay.setBackgroundResource(R.drawable.play);
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    playPause = false;
                }
            }
        });

//        radioConfig();
//
//        btnPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//                pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                pDialog.show();
//                isPlay = true;
//                Log.d("TES","Radio is On!");
//                isRadioPlay();
//                radioConfig();
//                startRadio();
//            }
//        });
//
//        btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                isPlay = false;
//                Log.d("TES","Radio is Off!");
//                isRadioPlay();
//                stopRadio();
//            }
//        });


    }

//    private void isRadioPlay(){
//        if (isPlay){
//            btnStop.setVisibility(View.VISIBLE);
//            btnPlay.setVisibility(View.GONE);
//            pDialog.dismiss();
//        }else{
//            btnPlay.setVisibility(View.VISIBLE);
//            btnStop.setVisibility(View.GONE);
//        }
//
//    }
//
//    private void radioConfig(){
//         mp = new MediaPlayer();
//        try {
//            mp.setDataSource(URL_RADIO);
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    private void startRadio(){
//        try {
//            mp.prepare();
//            mp.start();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private void stopRadio(){
//        mp.pause();
////        try {
////            mp.prepare();
////            mp.stop();
////        } catch (IllegalStateException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//    }

//    private OnClickListener pausePlay = new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            // TODO Auto-generated method stub
//
//            if (!playPause) {
//                btn.setBackgroundResource(R.drawable.button_pause);
//                if (intialStage)
//                    new Player()
//                            .execute("http://www.virginmegastore.me/Library/Music/CD_001214/Tracks/Track1.mp3");
//                else {
//                    if (!mediaPlayer.isPlaying())
//                        mediaPlayer.start();
//                }
//                playPause = true;
//            } else {
//                btn.setBackgroundResource(R.drawable.button_play);
//                if (mediaPlayer.isPlaying())
//                    mediaPlayer.pause();
//                playPause = false;
//            }
//        }
//    };
    /**
     * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
     * @author piyush
     *
     */

    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        intialStage = true;
                        playPause=false;
                        btnPlay.setBackgroundResource(R.drawable.play);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            mediaPlayer.start();

            intialStage = false;
        }

        public Player() {
            progress = new ProgressDialog(StreamRadioActivity.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Sedang memuat data dari server..");
            this.progress.show();

        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

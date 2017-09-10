package com.mycampus.rontikeky.myacademic.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mycampus.rontikeky.myacademic.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenImageActivity extends Activity {

    TextView share,back;
    public String extrasUrl;
    PhotoViewAttacher mAttacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
        share = (TextView)findViewById(R.id.share);
        back = (TextView)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, extrasUrl);
                // Target whatsapp:
                shareIntent.setPackage("com.whatsapp");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FullScreenImageActivity.this,
                            "Whatsapp have not been installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Intent callingActivityIntent = getIntent();
//        if(callingActivityIntent != null) {
//            Uri imageUri = callingActivityIntent.getData();
//            if(imageUri != null && fullScreenImageView != null) {
//                Glide.with(this)
//                        .load(imageUri)
//                        .into(fullScreenImageView);
//            }
//        }

        Intent result= getIntent();
        Bundle extrasResult = result.getExtras();

        if(extrasResult!=null)
        {
            extrasUrl =(String) extrasResult.get("url");
        }

        Glide.with(FullScreenImageActivity.this).load(extrasUrl).into(fullScreenImageView);

        mAttacher = new PhotoViewAttacher(fullScreenImageView);
    }

}

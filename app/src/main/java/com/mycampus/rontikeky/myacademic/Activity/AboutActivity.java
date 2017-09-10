package com.mycampus.rontikeky.myacademic.Activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.R;

public class AboutActivity extends AppCompatActivity {

    TextView androidVersion;
    String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        androidVersion = (TextView)findViewById(R.id.versionAndroid);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        androidVersion.setText("Versi "+ version);
    }
}

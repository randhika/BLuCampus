package com.mycampus.rontikeky.myacademic.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mycampus.rontikeky.myacademic.Adapter.ImageAdapter;
import com.mycampus.rontikeky.myacademic.R;

public class MainDashbaoard extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashbaoard);

        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                switch (position) {

                    case 0:
                        Log.d("TEST", String.valueOf(position));
                        break;

                    case 1:
                        Log.d("TEST", String.valueOf(position));
                        break;

                    case 2:
                        Log.d("TEST", String.valueOf(position));
                        break;

                    case 3:
                        Log.d("TEST", String.valueOf(position));
                        break;

                    case 4:
                        Log.d("TEST", String.valueOf(position));
                        break;

                    case 5:
                        Log.d("TEST", String.valueOf(position));
                        break;
                }
            }
        });
    }
}

package com.mycampus.rontikeky.myacademic.Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.R;

/**
 * Created by Anggit on 05/10/2017.
 */

public class ImageAdapter extends BaseAdapter{

    Context context;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;

        if (view == null) {
            view = new View(context);
            LayoutInflater Inf = LayoutInflater.from(context);
            view = Inf.inflate(R.layout.home_grid, null);
        } else {
            view1 = view;
        }

        ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        iv.setImageResource(mThumbIds[i]);
        tv.setText(labels[i]);
        return view;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.home_event,R.drawable.home_bus, R.drawable.home_campus,
             R.drawable.home_lecture,
            R.drawable.home_support, R.drawable.home_wallet
    };

    String[] labels = { "Acara", "BAAK", "BLuDrive", "BLuPay",
            "PMB", "Gaya Hidup" };
}

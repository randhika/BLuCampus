package com.mycampus.rontikeky.myacademic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.Activity.SeminarDetial;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;

import java.util.List;

/**
 * Created by Anggit on 07/10/2017.
 */

public class PresenceAdapter extends RecyclerView.Adapter<PresenceAdapter.ViewHolder>{

    Context context;
    FontHandler fontHandler;
    PrefHandler prefHandler;

    public PresenceAdapter(Context context) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View v) {
            super(v);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(v.getContext(), SeminarDetial.class);
//                    i.putExtra("slug",slug.getText().toString());
//                    v.getContext().startActivity(i);
                }
            });

        }

    }

    @Override
    public PresenceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PresenceAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}

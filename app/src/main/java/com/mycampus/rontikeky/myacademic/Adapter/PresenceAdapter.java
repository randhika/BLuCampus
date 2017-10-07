package com.mycampus.rontikeky.myacademic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.Activity.SeminarDetial;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.Config.PrefHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.PresenceResponse;

import java.util.List;

/**
 * Created by Anggit on 07/10/2017.
 */

public class PresenceAdapter extends RecyclerView.Adapter<PresenceAdapter.ViewHolder>{

    List<PresenceResponse> presenceResponses;
    Context context;
    FontHandler fontHandler;
    PrefHandler prefHandler;
    int count = 0;
    int val = 0;

    public PresenceAdapter(Context context, List<PresenceResponse> presenceResponses) {
        this.presenceResponses = presenceResponses;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView view_count, view_username, view_name, view_status, view_id;
        RadioGroup view_presence_group;
        RadioButton view_hadir, view_tidak;

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

            view_count = (TextView)v.findViewById(R.id.countNumber);
            view_username = (TextView)v.findViewById(R.id.usernamePresence);
            view_name = (TextView)v.findViewById(R.id.namePresence);
            view_presence_group = (RadioGroup)v.findViewById(R.id.radioPresence);
            view_id = (TextView)v.findViewById(R.id.id_user);
            view_status = (TextView)v.findViewById(R.id.presence_status);
            view_hadir = (RadioButton)v.findViewById(R.id.hadir);
            view_tidak = (RadioButton)v.findViewById(R.id.tidak);

        }

    }

    @Override
    public PresenceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_presence,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PresenceAdapter.ViewHolder holder, int position) {


        try{

            PresenceResponse model = presenceResponses.get(position);

            fontHandler = new FontHandler(context);
            Typeface custom_font = fontHandler.getFont();

            holder.view_count.setTypeface(custom_font);
            holder.view_username.setTypeface(custom_font);
            holder.view_name.setTypeface(custom_font);

            int status = model.pivot.getStatusUser();

            switch (status){
                case 0 :
                    holder.view_presence_group.clearCheck();
                    break;
                case 1 :
                    holder.view_hadir.setChecked(true);
                    break;
                case 2 :
                    holder.view_tidak.setChecked(true);
                    break;
                default:
                    holder.view_presence_group.clearCheck();
                    break;
            }

            holder.view_count.setText(String.valueOf(++count));
            holder.view_username.setText(String.valueOf(model.getUsername()));
            holder.view_name.setText(model.getNama());
            holder.view_status.setText(String.valueOf(model.pivot.getStatusUser()));
            holder.view_id.setText(String.valueOf(model.pivot.getIdUser()));

            Log.d("TEST", String.valueOf(model.getNama()));
        }catch (Exception e){
            Log.d("EXCEPTION", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return presenceResponses.size();
    }

}

package com.mycampus.rontikeky.myacademic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mycampus.rontikeky.myacademic.Activity.InfoDetail;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.InfoResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Anggit on 18/07/2017.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] judul, isi, tanggal, slug_url, foto_url;
    private List<InfoResponse.Datum> list;

    Context context;

    FontHandler fontHandler;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView judul_view, isi_view, tanggal_view, slug_view, isi_view_tag;
        //private final  ImageView foto_view;


        public ViewHolder(View v) {
            super(v);


            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                    Intent i = new Intent(v.getContext(), InfoDetail.class);
                    i.putExtra("slug_url",slug_view.getText().toString());
                    i.putExtra("html_tag",isi_view_tag.getText().toString());
                    i.putExtra("judul",judul_view.getText().toString());
                    v.getContext().startActivity(i);
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(slug_view.getText().toString()));
//                    v.getContext().startActivity(browserIntent);
                }
            });

            judul_view = (TextView) v.findViewById(R.id.judul_info);
            isi_view = (TextView) v.findViewById(R.id.isi_info);
            tanggal_view = (TextView) v.findViewById(R.id.tanggal_info);
            slug_view = (TextView)v.findViewById(R.id.slug_url);
            isi_view_tag = (TextView)v.findViewById(R.id.isi_html_acara);
            //foto_view = (ImageView)v.findViewById(R.id.imgInfo);

            Log.d("xxxxxxx",isi_view_tag.getText().toString());
        }

        public TextView getJudulInfo() {
            return judul_view;
        }

        public TextView getIsiInfo() {
            return isi_view;
        }

        public TextView getTanngalInfo() {
            return tanggal_view;
        }

        public TextView getSlugView() {
            return slug_view;
        }

        public TextView getIsi_view_tag(){
            return isi_view_tag;
        }

//        public ImageView getFoto_view(){
//            return foto_view;
//        }
    }


    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param //dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(List<InfoResponse.Datum> infoResponses,Context context){
        this.list = infoResponses;
        this.context = context;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view, viewGroup, false);
        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        InfoResponse.Datum model = list.get(position);

        // Get element from your dataset at this position and replace the contents of the view
        // with that element


        try {
            Calendar c = Calendar.getInstance();


            String tgl = model.getTanggalInfo();


            SimpleDateFormat tm = new SimpleDateFormat("kk:mm");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

            String formattedTime = tm.format(c.getTime());
            String formattedDate = df.format(c.getTime());
            String formmatedDate2 = vd.format(df.parse(tgl));

            Log.d("DATE",formmatedDate2);

            Date timeFormated = tm.parse(formattedTime);
            Date tanggalFormatted = df.parse(tgl);
            Date dateFormmated = df.parse(formattedDate);




        fontHandler = new FontHandler(context);
        Typeface custom_font = fontHandler.getFont();
        Typeface custom_font_bold = fontHandler.getFontBold();

        viewHolder.getIsiInfo().setTypeface(custom_font);
        viewHolder.getJudulInfo().setTypeface(custom_font_bold);

        viewHolder.judul_view.setText(model.getJudulInfo());
        viewHolder.isi_view.setText(model.isiInfoDepan);
        viewHolder.tanggal_view.setText(formmatedDate2);
        viewHolder.slug_view.setText(model.getSlugUrlInfo());
        viewHolder.isi_view_tag.setText(model.getIsiInfo());

        //Glide.with(context).load(foto_url[position]).into(viewHolder.getFoto_view());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Here you apply the animation when the view is bound
        setAnimation(viewHolder.itemView, position);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateList(List<InfoResponse.Datum> list){
        this.list = list;
        notifyDataSetChanged();
    }
}

package com.mycampus.rontikeky.myacademic.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mycampus.rontikeky.myacademic.Activity.SeminarDetial;
import com.mycampus.rontikeky.myacademic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Anggit on 20/08/2017.
 */
public class EventRegisteredUserAdapter extends RecyclerView.Adapter<EventRegisteredUserAdapter.ViewHolder>{

    private static final String TAG = "EventRegisteredUserAdapter";

    private String[] judul,isi, slug, image, tanggal, jam_acara, tempat_acara,  daftar_acara, contact_person_acara;
    private int[] id,biaya_acara, jumlah_peserta_acara, jumlah_peserta_tersisa_acara;
    Context context,contex2;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView,textView2,textView3,slug,tanggal, jam_view, tempat, biaya_view, jumlah_peserta_view,daftar_view,terdaftar_text,empty,tangga_valid;
        private final ImageView imageUrl,terdaftar_icon;


        public ViewHolder(View v) {
            super(v);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), SeminarDetial.class);
                    i.putExtra("slug",slug.getText().toString());
                    v.getContext().startActivity(i);
                }
            });
            textView = (TextView) v.findViewById(R.id.id);
            textView2 = (TextView) v.findViewById(R.id.judul);
            textView3 = (TextView) v.findViewById(R.id.isi_acara);
            slug = (TextView)v.findViewById(R.id.slug_seminar);
            tanggal = (TextView)v.findViewById(R.id.tanggal_acara);
            tangga_valid = (TextView)v.findViewById(R.id.tanggal_acara_valid);
            jam_view = (TextView)v.findViewById(R.id.jam_acara);
            tempat = (TextView)v.findViewById(R.id.tempat);
            biaya_view = (TextView)v.findViewById(R.id.biaya);
            jumlah_peserta_view = (TextView)v.findViewById(R.id.available);
            daftar_view = (TextView)v.findViewById(R.id.daftarIndicator);
            terdaftar_icon = (ImageView) v.findViewById(R.id.check);
            terdaftar_text = (TextView)v.findViewById(R.id.textCheck);
            imageUrl = (ImageView)v.findViewById(R.id.imgSeminar);
            empty = (TextView)v.findViewById(R.id.single_empty);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getTextView2() {
            return textView2;
        }

        public TextView getTextView3() {
            return textView3;
        }

        public  TextView getSlug1(){
            return slug;
        }

        public TextView getTanggal1(){
            return tanggal;
        }

        public TextView getTangga_valid(){
            return tangga_valid;
        }

        public ImageView getImageSeminar(){
            return imageUrl;
        }

        public TextView getJam_view1(){
            return jam_view;
        }

        public TextView getTempat(){
            return tempat;
        }
        public TextView getBiaya_view(){
            return biaya_view;
        }

        public TextView getJumlah_peserta_view(){
            return jumlah_peserta_view;
        }

        public TextView getDaftar_view(){
            return daftar_view;
        }

        public ImageView getTerdaftar_icon(){
            return terdaftar_icon;
        }

        public TextView getTerdaftar_text(){
            return terdaftar_text;
        }

        public TextView getCardView(){
            return empty;
        }

    }


    public EventRegisteredUserAdapter(String[] judul,String[] isi, int[] id, String[] slug, String[] url_image, String[] tanggal, String[] jam,String[] tempat,int[] biaya, int[] jumlah_peserta, int[] jumlah_perserta_tersisa, String[] contact_person, String[] daftar, Context context) {
        this.id = id;
        this.judul = judul;
        this.isi = isi;
        this.slug = slug;
        this.image = url_image;
        this.tanggal = tanggal;
        this.jam_acara = jam;
        this.tempat_acara = tempat;
        this.jumlah_peserta_acara = jumlah_peserta;
        this.jumlah_peserta_tersisa_acara = jumlah_perserta_tersisa;
        this.biaya_acara = biaya;
        this.contact_person_acara = contact_person;
        this.daftar_acara = daftar;
        this.context = context;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.single_event_user, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element

        Log.d("TEST", String.valueOf(getItemCount()));

        try {

            String s = String.format("%,d", biaya_acara[position]);

            if (getItemCount() == 0){
                Log.d("TEST 2","KOSONG");
            }else{
                Log.d("TEST 3", "ADA");
            }

            String isi_single = isi[position];
            if (isi_single.length() > 100){
                viewHolder.getTextView3().setText(isi_single.substring(0,100)+" ...");
            }else{
                viewHolder.getTextView3().setText(isi[position]);
            }

            if (daftar_acara[position].equalsIgnoreCase("true")){
                viewHolder.getTerdaftar_icon().setVisibility(View.VISIBLE);
                viewHolder.getTerdaftar_text().setVisibility(View.VISIBLE);
            }else{
                viewHolder.getTerdaftar_icon().setVisibility(View.GONE);
                viewHolder.getTerdaftar_text().setVisibility(View.GONE);
            }

            Calendar c = Calendar.getInstance();

            String jam = jam_acara[position];
            String subjam = jam.substring(6,11);

            String tgl = tanggal[position];

            SimpleDateFormat tm = new SimpleDateFormat("kk:mm");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

            String formattedTime = tm.format(c.getTime());
            String formattedDate = df.format(c.getTime());
            String formmatedDate2 = vd.format(df.parse(tgl));

            Log.d("DATE",formmatedDate2);

            Date jamFormmated = tm.parse(subjam);
            Date timeFormated = tm.parse(formattedTime);
            Date tanggalFormatted = df.parse(tgl);
            Date dateFormmated = df.parse(formattedDate);

            if (dateFormmated.compareTo(tanggalFormatted) > 0){
                Log.d("WAKTU 4 : ","TANGGAL EVENT TELAH BERAKHIR");
                viewHolder.getTerdaftar_icon().setVisibility(View.VISIBLE);
                viewHolder.getTerdaftar_text().setVisibility(View.VISIBLE);
                viewHolder.getTerdaftar_text().setText("Sudah Berakhir");
                viewHolder.getTerdaftar_text().setBackgroundColor(Color.parseColor("#FF0000"));
            }else{
//                if (timeFormated.compareTo(jamFormmated) > 0){
//                    Log.d("WAKTU 4 : ","WAKTU EVENT TELAH BERAKHIR");
//                    Log.d("WAKTU 4 : ",timeFormated.toString());
//                    Log.d("WAKTU 4 : ",jamFormmated.toString());
//                }else{
//                    Log.d("WAKTU 4 : ","WAKTU EVENT BELUM BERAKHIR");
//                    Log.d("WAKTU 5 : ",timeFormated.toString());
//                    Log.d("WAKTU 5 : ",jamFormmated.toString());
//                }
                viewHolder.getTerdaftar_text().setBackgroundColor(Color.parseColor("#1BA0E1"));
                Log.d("WAKTU 4 : ","TANGGAL EVENT BELUM BERAKHIR");
            }

            Log.d("WAKTU 1 : ",subjam);
            Log.d("WAKTU 3 : ",formattedTime);

            int kursi_tersisa = jumlah_peserta_acara[position]-jumlah_peserta_tersisa_acara[position];
            if (jumlah_peserta_tersisa_acara[position] == 0){
                viewHolder.getJumlah_peserta_view().setText("Sudah Penuh");
                viewHolder.getJumlah_peserta_view().setTextColor(Color.parseColor("#FF0000"));
            }else{
                viewHolder.getJumlah_peserta_view().setText(Integer.toString(kursi_tersisa) + " dari " + Integer.toString(jumlah_peserta_acara[position]));
                viewHolder.getJumlah_peserta_view().setTextColor(Color.parseColor("#000000"));
            }

            viewHolder.getTextView().setText(Integer.toString(id[position]));
            viewHolder.getTextView2().setText(judul[position]);
            viewHolder.getTempat().setText(tempat_acara[position]);
            viewHolder.getSlug1().setText(slug[position]);
            viewHolder.getTanggal1().setText(tanggal[position]);
            viewHolder.getTangga_valid().setText(formmatedDate2);
            viewHolder.getJam_view1().setText(", "+jam_acara[position]);
            viewHolder.getBiaya_view().setText("Rp." + s);
            Glide.with(context).load(image[position]).into(viewHolder.imageUrl);
            viewHolder.getDaftar_view().setText(daftar_acara[position]);

            // Here you apply the animation when the view is bound
            setAnimation(viewHolder.itemView, position);
        }catch (Exception e){
            Log.d("DEBUG ADAPTER",e.toString());
        }

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return id == null ? 0 : id.length;
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

}

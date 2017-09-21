package com.mycampus.rontikeky.myacademic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
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
import com.mycampus.rontikeky.myacademic.Activity.WorkshopDetail;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.WorkshopResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Anggit on 12/08/2017.
 */
public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.ViewHolder> {
    private static final String TAG = "WorkshopAdapter";

    private String[] judul,isi, slug, image, tanggal, jam_workshop, tempat_workshop,  daftar_acara, contact_person_acara;
    private int[] id,biaya_acara, jumlah_peserta_acara, jumlah_peserta_tersisa_acara;
    private List<WorkshopResponse.Datum> list;
    Context context;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView,textView2,textView3,slug,tanggal, jam_view,tempat_view, biaya_view, jumlah_peserta_view,daftar_view,terdaftar_text,tanggal_valid;
        private final ImageView imageUrl,terdaftar_icon;
        private final CardView cardView;


        public ViewHolder(View v) {
            super(v);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String nil_token = token.getText().toString();
                    Log.d(TAG, "Element " + tanggal.getText().toString() + " clicked.");
                    Intent i = new Intent(v.getContext(), WorkshopDetail.class);
                    i.putExtra("slug",slug.getText().toString());
                    v.getContext().startActivity(i);
                }
            });
            textView = (TextView) v.findViewById(R.id.id_workshop);
            textView2 = (TextView) v.findViewById(R.id.judul_workshop);
            textView3 = (TextView) v.findViewById(R.id.isi_workshop);
            slug = (TextView)v.findViewById(R.id.slug_workshop);
            tanggal = (TextView)v.findViewById(R.id.tanggal_acara);
            tanggal_valid = (TextView)v.findViewById(R.id.tanggal_acara_valid);
            tempat_view = (TextView)v.findViewById(R.id.tempat);
            jam_view = (TextView)v.findViewById(R.id.jam_acara);
            biaya_view = (TextView)v.findViewById(R.id.biaya);
            jumlah_peserta_view = (TextView)v.findViewById(R.id.available);
            daftar_view = (TextView)v.findViewById(R.id.daftarIndicator);
            terdaftar_icon = (ImageView) v.findViewById(R.id.check);
            terdaftar_text = (TextView)v.findViewById(R.id.textCheck);
            imageUrl = (ImageView)v.findViewById(R.id.imgWorkshop);
            cardView = (CardView)v.findViewById(R.id.single_cvw);
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

        public TextView getTanggal_valid(){
            return tanggal_valid;
        }

        public TextView getJam_view2(){
            return jam_view;
        }

        public TextView getTempat_view(){
            return tempat_view;
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


        public ImageView getImageSeminar(){
            return imageUrl;
        }

        public CardView getCardView() { return cardView; }
    }


    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param //dataSet String[] containing the data to populate views to be used by RecyclerView.
     */

    public WorkshopAdapter(List<WorkshopResponse.Datum> list, Context context){
        this.list = list;
        this.context = context;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_workshop, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        WorkshopResponse.Datum model = list.get(position);

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        try {

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/droid/DroidSerif.ttf");
            Typeface custom_font2 = Typeface.createFromAsset(context.getAssets(), "fonts/Asimov.otf");

            viewHolder.getTextView2().setTypeface(custom_font2);
            viewHolder.getTextView3().setTypeface(custom_font);
            viewHolder.getTanggal1().setTypeface(custom_font);
            viewHolder.getJam_view2().setTypeface(custom_font);
            viewHolder.getTempat_view().setTypeface(custom_font);
            viewHolder.getBiaya_view().setTypeface(custom_font);
            viewHolder.getJumlah_peserta_view().setTypeface(custom_font);

            String isi_single = model.isiAcara;

            String s = String.format("%,d", model.biayaAcara);

            if (isi_single.length() > 100){
                viewHolder.getTextView3().setText(isi_single.substring(0,100)+" ...");
            }else{
                viewHolder.getTextView3().setText(model.isiAcara);
            }

            if (model.daftar.equalsIgnoreCase("true")){
                viewHolder.getTerdaftar_icon().setVisibility(View.VISIBLE);
                viewHolder.getTerdaftar_text().setVisibility(View.VISIBLE);
            }else{
                viewHolder.getTerdaftar_icon().setVisibility(View.GONE);
                viewHolder.getTerdaftar_text().setVisibility(View.GONE);
            }

            Calendar c = Calendar.getInstance();

            String jam = model.jamAcara;
            String subjam = jam.substring(6,11);

            String tgl = model.tanggalAcara;
            String tglBatas = model.batasAkhirDaftar.substring(0,10);

            SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vd = new SimpleDateFormat("dd-MM-yyyy");

            String formattedTime = tm.format(c.getTime());
            String formattedDate = df.format(c.getTime());
            String formmatedDate2 = vd.format(df.parse(tgl));

            Log.d("DATE",tglBatas);

            Date jamFormmated = tm.parse(subjam);
            Date timeFormated = tm.parse(formattedTime);

            Date tanggalFormatted = df.parse(tgl);
            Date tanggalFormattedBatas = df.parse(tglBatas);
            Date dateFormmated = df.parse(formattedDate);

            //BATAS JAM
            String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
            String timeToCompare = model.batasAkhirDaftar.substring(10,16);
            Log.d("BATAS",timeToCompare);
            //boolean x = currentTime.equals(timeToCompare);

            Date jamFor = tm.parse(currentTime);
            Date jamFor2 = tm.parse(timeToCompare);



            if (dateFormmated.compareTo(tanggalFormattedBatas) > 0){
                Log.d("WAKTU 1 : ","TANGGAL EVENT TELAH BERAKHIR");
                viewHolder.getTerdaftar_text().setVisibility(View.VISIBLE);
                viewHolder.getTerdaftar_text().setText("Pendaftaran ditutup");
                viewHolder.getTerdaftar_text().setBackgroundColor(Color.parseColor("#FF0000"));
            }else{
                if (dateFormmated.compareTo(tanggalFormattedBatas) == 0){
                    if (jamFor.compareTo(jamFor2) > 0){
                        Log.d("WAKTU x : ","TANGGAL EVENT TELAH BERAKHIR");
                        viewHolder.getTerdaftar_text().setVisibility(View.VISIBLE);
                        viewHolder.getTerdaftar_text().setText("Pendaftaran ditutup");
                        viewHolder.getTerdaftar_text().setBackgroundColor(Color.parseColor("#FF0000"));
                    }else{
                        Log.d("WAKTU y : ","TANGGAL EVENT BELUM BERAKHIR");
                    }
                }
                Log.d("WAKTU 2 : ","TANGGAL EVENT BELUM BERAKHIR");
            }

            if (dateFormmated.compareTo(tanggalFormatted) > 0){
                Log.d("WAKTU 4 : ","TANGGAL EVENT TELAH BERAKHIR");
                viewHolder.getTerdaftar_icon().setVisibility(View.VISIBLE);
                viewHolder.getTerdaftar_text().setVisibility(View.VISIBLE);
                viewHolder.getTerdaftar_text().setText("Sudah Berakhir");
                viewHolder.getTerdaftar_text().setBackgroundColor(Color.parseColor("#FF0000"));
                viewHolder.getCardView().setVisibility(View.GONE);

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
                viewHolder.getCardView().setVisibility(View.VISIBLE);
            }

            Log.d("WAKTU 1 : ",subjam);
            Log.d("WAKTU 3 : ",formattedTime);

            int kursi_tersisa = model.jumlahPeserta-model.jumlahPesertaSisa;
            if (model.jumlahPesertaSisa == 0){
                viewHolder.getJumlah_peserta_view().setText("Sudah Penuh");
                viewHolder.getJumlah_peserta_view().setTextColor(Color.parseColor("#FF0000"));
            }else{
                viewHolder.getJumlah_peserta_view().setText(Integer.toString(kursi_tersisa) + " dari " + Integer.toString(model.jumlahPeserta));
                viewHolder.getJumlah_peserta_view().setTextColor(Color.parseColor("#000000"));
            }

            viewHolder.getTextView().setText(Integer.toString(model.id));
            viewHolder.getTextView2().setText(model.judulAcara);
            viewHolder.getSlug1().setText(model.slug);
            viewHolder.getTanggal1().setText(model.tanggalAcara);
            viewHolder.getTanggal_valid().setText(formmatedDate2);
            viewHolder.getTempat_view().setText(model.tempatAcara);
            viewHolder.getJam_view2().setText(", "+model.jamAcara);
            viewHolder.getBiaya_view().setText("Rp." + s);
            Glide.with(context).load(model.fotoAcara).placeholder(R.drawable.nopicture).into(viewHolder.imageUrl);
            //viewHolder.getDaftar_view().setText(daftar_acara[position]);

            // Here you apply the animation when the view is bound
            setAnimation(viewHolder.itemView, position);
        }catch (Exception e){
            Log.d("DEBUG ADAPTER",e.getMessage());
        }
        Log.d("LIST", String.valueOf(list.size()));
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
}

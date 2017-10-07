package com.mycampus.rontikeky.myacademic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mycampus.rontikeky.myacademic.Activity.PresenceActivity;
import com.mycampus.rontikeky.myacademic.Activity.SeminarDetial;
import com.mycampus.rontikeky.myacademic.Config.FontHandler;
import com.mycampus.rontikeky.myacademic.R;
import com.mycampus.rontikeky.myacademic.Response.SeminarResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Anggit on 07/10/2017.
 */

public class EOAdapter extends RecyclerView.Adapter<EOAdapter.ViewHolder>{
    private static final String TAG = "EOAdapter";

    private String[] judul,isi, slug, image, tanggal, jam_acara, tempat_acara,  daftar_acara, contact_person_acara;
    private int[] id,biaya_acara, jumlah_peserta_acara, jumlah_peserta_tersisa_acara;
    Context context;
    private List<SeminarResponse.Datum> list;
    public int currentSize;

    FontHandler fontHandler;

    public int value = 0;

    //Share
    private ShareActionProvider mShareActionProvider;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView,textView2,textView3,slug,tanggal, jam_view, tempat, biaya_view, jumlah_peserta_view,daftar_view,terdaftar_text,tanggal_valid;
        private final ImageView imageUrl,terdaftar_icon;
        private final Button btnShare;
        private final CardView cardView;



        public ViewHolder(View v) {
            super(v);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + tanggal.getText().toString() + " clicked.");
                    Intent i = new Intent(v.getContext(), PresenceActivity.class);
                    i.putExtra("slug",slug.getText().toString());
                    i.putExtra("title",textView2.getText().toString());
                    v.getContext().startActivity(i);
                }
            });
            textView = (TextView) v.findViewById(R.id.id);
            textView2 = (TextView) v.findViewById(R.id.judul);
            textView3 = (TextView) v.findViewById(R.id.isi_acara);
            slug = (TextView)v.findViewById(R.id.slug_seminar);
            tanggal = (TextView)v.findViewById(R.id.tanggal_acara);
            tanggal_valid = (TextView)v.findViewById(R.id.tanggal_acara_valid);
            jam_view = (TextView)v.findViewById(R.id.jam_acara);
            tempat = (TextView)v.findViewById(R.id.tempat);
            biaya_view = (TextView)v.findViewById(R.id.biaya);
            jumlah_peserta_view = (TextView)v.findViewById(R.id.available);
            daftar_view = (TextView)v.findViewById(R.id.daftarIndicator);
            terdaftar_icon = (ImageView) v.findViewById(R.id.check);
            terdaftar_text = (TextView)v.findViewById(R.id.textCheck);
            imageUrl = (ImageView)v.findViewById(R.id.imgSeminar);
            btnShare = (Button)v.findViewById(R.id.share);
            cardView = (CardView)v.findViewById(R.id.single_cv);

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TEST","Ada Pesan");
                    String fileName = imageUrl.getDrawable().toString();//Name of an image
                    String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    String myDir = externalStorageDirectory + "/saved_images/"; // the file will be in saved_images
                    Uri uri = Uri.parse("file:///" + myDir + fileName);
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/html");
                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Mail");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, textView2.getText().toString());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    v.getContext().startActivity(Intent.createChooser(shareIntent, "Share deal"));
                }
            });
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

        public CardView getCardView() { return cardView; }
    }


    public EOAdapter(List<SeminarResponse.Datum> list,Context context){
        this.list = list;
        this.context = context;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_seminar, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        SeminarResponse.Datum model = list.get(position);
        Log.d("AA",model.fotoAcara);
        try {



            fontHandler = new FontHandler(context);
            Typeface custom_font = fontHandler.getFont();
            Typeface custom_font_bold = fontHandler.getFontBold();

            viewHolder.getTextView2().setTypeface(custom_font);
            viewHolder.getTextView3().setTypeface(custom_font);
            viewHolder.getTanggal1().setTypeface(custom_font);
            viewHolder.getJam_view1().setTypeface(custom_font);
            viewHolder.getTempat().setTypeface(custom_font);
            viewHolder.getBiaya_view().setTypeface(custom_font);
            viewHolder.getJumlah_peserta_view().setTypeface(custom_font);
            viewHolder.getTanggal_valid().setTypeface(custom_font);

            String isi_single = model.isiAcara;
            String s = String.format("%,d", model.biayaAcara);
            Log.d("AGAL",s);
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

            int kursi_tersisa = model.jumlahPeserta - model.jumlahPesertaSisa;
            if (model.jumlahPesertaSisa == 0){
                viewHolder.getJumlah_peserta_view().setText("Sudah Penuh");
                viewHolder.getJumlah_peserta_view().setTextColor(Color.parseColor("#FF0000"));
            }else{
                viewHolder.getJumlah_peserta_view().setText(Integer.toString(kursi_tersisa) + " dari " + Integer.toString(model.jumlahPeserta));
                viewHolder.getJumlah_peserta_view().setTextColor(Color.parseColor("#000000"));
            }

            viewHolder.tempat.setText(model.tempatAcara);

            viewHolder.getTextView().setText(Integer.toString(model.id));
            viewHolder.getTextView2().setText(model.judulAcara);
            viewHolder.getTempat().setText(model.tempatAcara);
            viewHolder.getSlug1().setText(model.slug);
            viewHolder.getTanggal1().setText(model.tanggalAcara);
            viewHolder.getJam_view1().setText(", "+model.jamAcara);
            viewHolder.getBiaya_view().setText("Rp." + s);
            viewHolder.getDaftar_view().setText(model.daftar);
            viewHolder.getTanggal_valid().setText(formmatedDate2);
            Glide.with(context).load(model.fotoAcara).placeholder(R.drawable.nopicture).into(viewHolder.imageUrl);

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
        return list.size();
    }

    public int getValue(){ return value; }

    public void updateItemCount(int size){ currentSize = size-1;}

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

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

}

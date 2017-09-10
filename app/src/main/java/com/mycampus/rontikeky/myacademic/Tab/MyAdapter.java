package com.mycampus.rontikeky.myacademic.Tab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mycampus.rontikeky.myacademic.Fragment.HomeFragment;
import com.mycampus.rontikeky.myacademic.Fragment.SeminarFragment;
import com.mycampus.rontikeky.myacademic.Fragment.WorkshopFragment;
import com.mycampus.rontikeky.myacademic.R;

/**
 * Created by Anggit on 18/07/2017.
 */
public class MyAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] titles ={"Info","Seminar","Workshop"};
    int[] icon = new int[]{R.drawable.ic_check_24dp, R.drawable.ic_account_circle_24dp,R.drawable.ic_event_available_24dp};
    int[] judul = new int[]{R.string.info, R.string.seminar,R.string.workshop};
    private int heightIcon;

    public MyAdapter(FragmentManager fm, Context c){
        super(fm);
        mContext = c;
        double scale = c.getResources().getDisplayMetrics().density;
        heightIcon=(int)(24*scale+0.5f);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag= null;

        if(position ==0){
            frag = new HomeFragment();
        }else if(position == 1){
            frag = new SeminarFragment();
        }else if(position == 2){
            frag = new WorkshopFragment();
        }

        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public CharSequence getPageTitle(int position){
        //COMMENT BELOW USED TO MAKE ICON AS REPRESENTATION OF TAB
//        Drawable d = mContext.getResources().getDrawable(icon[position]);
//        d.setBounds(0,0,heightIcon,heightIcon);
//
////        String f = mContext.getString(judul[position]);
////        f.setBounds(0, 0, heightIcon, heightIcon);
//
//        ImageSpan is = new ImageSpan(d);
//
//        SpannableString sp = new SpannableString(" ");
//        sp.setSpan(is,0,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //return sp;
        return titles[position];
    }

}
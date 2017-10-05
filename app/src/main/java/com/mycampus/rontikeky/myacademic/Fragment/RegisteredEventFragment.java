package com.mycampus.rontikeky.myacademic.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycampus.rontikeky.myacademic.R;

/**
 * Created by Anggit on 04/10/2017.
 */

public class RegisteredEventFragment extends Fragment{

    public RegisteredEventFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_registered, container, false);
    }
}

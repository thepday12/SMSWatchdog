package com.tokimthep.smswatchdog.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tokimthep.smswatchdog.R;


public class SplashScreenFragment extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static SplashScreenFragment newInstance() {
        return new SplashScreenFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }



}

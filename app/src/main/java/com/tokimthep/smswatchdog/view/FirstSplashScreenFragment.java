package com.tokimthep.smswatchdog.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokimthep.smswatchdog.R;
import com.tokimthep.smswatchdog.view.scan.ScanActivity;
import com.tokimthep.smswatchdog.view.utils.MySharedPreferences;


public class FirstSplashScreenFragment extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static FirstSplashScreenFragment newInstance() {
        return new FirstSplashScreenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btContinue = view.findViewById(R.id.btContinue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreferences.write(MySharedPreferences.IS_FIRST, false);
                startActivity(new Intent(getContext(),ScanActivity.class));
                getActivity().finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_splash_screen, container, false);
    }



}

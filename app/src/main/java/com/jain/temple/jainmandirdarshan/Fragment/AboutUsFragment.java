package com.jain.temple.jainmandirdarshan.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jain.temple.jainmandirdarshan.R;

/**
 * Created by admin on 8/23/2017.
 */

public class AboutUsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView= inflater.inflate(R.layout.fragment_about_us,container,false);
        
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {

    }
}

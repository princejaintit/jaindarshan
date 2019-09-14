package com.jain.temple.jainmandirdarshan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.jain.temple.jainmandirdarshan.R;

/**
 * Created by admin on 8/23/2017.
 */

public class NamokarActivity extends BaseActivity {
    private TextView next_bt;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_namokar_mantra);

        initView();
    }

    private void initView() {

       Intent intent= getIntent();
       boolean fromSplash= intent.getBooleanExtra("fromSplash",false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       TextView toolbar_title= toolbar.findViewById(R.id.toolbar_title);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;

        next_bt= (TextView) findViewById(R.id.next_bt);

        if (fromSplash) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            toolbar_title.setVisibility(View.VISIBLE);
            toolbar_title.setText(getString(R.string.namokar_mahamantra));
            next_bt.setVisibility(View.VISIBLE);
        }
        else{
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar_title.setVisibility(View.GONE);
            next_bt.setVisibility(View.GONE);


        }

        next_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NamokarActivity.this,HomeActivity.class));
                finish();
            }
        });
    }
    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

}

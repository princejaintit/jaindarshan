package com.jain.temple.jainmandirdarshan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.TirthankarModel;

/**
 * Created by admin on 8/7/2017.
 */

public class TirthankarDetailActivity extends BaseActivity {
    private TextView name,father_name,mother_name,birth_place,birth_tithi,diksha_tithi,keval_gyan_tithi,nakshatra,nirvan_place,neerwan_tithi;
    private TextView sign_tv;
    private CollapsingToolbarLayout collapsingToolbarLayout ;

    private String LOG_TAG="activity";
    private ImageView tirthankar_sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_tirthankar);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        name= (TextView) findViewById(R.id.name);
        father_name= (TextView) findViewById(R.id.father_name);
        mother_name= (TextView) findViewById(R.id.mother_name);
        birth_place= (TextView) findViewById(R.id.birth_place);
        birth_tithi= (TextView) findViewById(R.id.birth_tithi);
        diksha_tithi= (TextView) findViewById(R.id.diksha_tithi);
        keval_gyan_tithi= (TextView) findViewById(R.id.keval_gyan_tithi);
        nakshatra= (TextView) findViewById(R.id.nakshatra);
        nirvan_place= (TextView) findViewById(R.id.nirvan_place);
        neerwan_tithi= (TextView) findViewById(R.id.neerwan_tithi);

        sign_tv= (TextView) findViewById(R.id.sign_tv);

        tirthankar_sign= (ImageView) findViewById(R.id.tirthankar_sign);


       Intent intent= getIntent();

       TirthankarModel tirthankarModel= intent.getParcelableExtra("model");

        actionBar.setTitle(tirthankarModel.getName_hindi());

        name.setText(tirthankarModel.getName_hindi()+" जी भगवान ");
        father_name.setText(tirthankarModel.getFather_name_hindi());
        mother_name.setText(tirthankarModel.getMother_name_hindi());
        birth_place.setText(tirthankarModel.getBirthplace_hindi());
        birth_tithi.setText(tirthankarModel.getBirthtithi());
        diksha_tithi.setText(tirthankarModel.getDikshaTithi());
        keval_gyan_tithi.setText(tirthankarModel.getKevalgyanTithi());
        nakshatra.setText(tirthankarModel.getNakshatra());
        nirvan_place.setText(tirthankarModel.getNeervan_place_hindi());
        neerwan_tithi.setText(tirthankarModel.getNeervanTithi());

        sign_tv.setText(tirthankarModel.getSign_hindi());


        dynamicToolbarColor();

       toolbarTextAppernce();



        switch (tirthankarModel.getPosition()){
            case 1:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t1));
                break;
            case 2:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t2));
                break;
            case 3:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t3));
                break;
            case 4:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t4));
                break;
            case 5:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t5));
                break;
            case 6:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t6));
                break;
            case 7:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t7));
                break;
            case 8:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t8));
                break;
            case 9:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t9));
                break;
            case 10:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t10));
                break;
            case 11:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t11));
                break;
            case 12:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t12));
                break;
            case 13:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t13));
                break;
            case 14:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t14));
                break;
            case 15:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t15));
                break;
            case 16:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t16));
                break;
            case 17:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t17));
                break;
            case 18:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t18));
                break;
            case 19:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t19));
                break;
            case 20:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t20));
                break;
            case 21:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t21));
                break;
            case 22:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t22));
                break;
            case 23:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t23));
                break;
            case 24:
                tirthankar_sign.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.t24));
                break;

        }

    }





    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.place_holder);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(ContextCompat.getColor(TirthankarDetailActivity.this,R.color.colorPrimary)));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(ContextCompat.getColor(TirthankarDetailActivity.this,R.color.calendar_color)));
            }
        });
    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
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

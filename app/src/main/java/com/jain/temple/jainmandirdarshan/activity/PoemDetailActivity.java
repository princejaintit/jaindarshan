package com.jain.temple.jainmandirdarshan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.SliderPoemAdapter;
import com.jain.temple.jainmandirdarshan.model.PoemModel;

import java.util.ArrayList;

/**
 * Created by admin on 9/7/2017.
 */

public class PoemDetailActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager flipper_poem_detail;
    private ImageView pre_artical,next_artical;
    private int articalPosition;
    private int arrSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_poem_detail);

        inItView();
    }

    private void inItView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar()/*/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);



        next_artical=findViewById(R.id.next_artical);
        pre_artical=findViewById(R.id.pre_artical);

        next_artical.setOnClickListener(this);
        pre_artical.setOnClickListener(this);

        flipper_poem_detail=findViewById(R.id.flipper_poem_detail);


       Intent intent= getIntent();
       final ArrayList<PoemModel> poemModelArrayList= intent.getParcelableArrayListExtra("poemList");
        articalPosition= intent.getIntExtra("position",0);

        arrSize=poemModelArrayList.size();

        showHideView(articalPosition);


        SliderPoemAdapter sliderPagerAdapter = new SliderPoemAdapter(this, poemModelArrayList);
        flipper_poem_detail.setAdapter(sliderPagerAdapter);
        flipper_poem_detail.setCurrentItem(articalPosition);

        sliderPagerAdapter.notifyDataSetChanged();


        flipper_poem_detail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                articalPosition=position;

                showHideView(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void showHideView(int position) {
        if (position==0){
            pre_artical.setVisibility(View.INVISIBLE);

        }
        else if (position==arrSize-1){
            next_artical.setVisibility(View.INVISIBLE);

        }
        else{
            pre_artical.setVisibility(View.VISIBLE);
            next_artical.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.pre_artical:
                flipper_poem_detail.setCurrentItem(articalPosition-1);
                break;
            case R.id.next_artical:
                flipper_poem_detail.setCurrentItem(articalPosition+1);
                break;
        }

    }
}

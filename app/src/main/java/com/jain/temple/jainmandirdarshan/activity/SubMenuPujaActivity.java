package com.jain.temple.jainmandirdarshan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Fragment.FragmentDrawer;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.SubMenuAdaptor;
import com.jain.temple.jainmandirdarshan.model.PujaMainModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 8/3/2017.
 */

public class SubMenuPujaActivity extends BaseActivity {
    private RecyclerView recy_view;
    private ArrayList<PujaMainModel> modelArrayList;
    private SubMenuAdaptor subMenuAdaptor;
    private int position;
    private String LOG_TAG="activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_puja_sub_menu);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        recy_view= (RecyclerView) findViewById(R.id.recy_view);


        modelArrayList=new ArrayList<>();
        subMenuAdaptor=new SubMenuAdaptor(this,modelArrayList);

        recy_view.setLayoutManager(new GridLayoutManager(this,2));
        recy_view.setAdapter(subMenuAdaptor);

        Intent intent =getIntent() ;

        position=intent.getIntExtra("position",0);
       /*   refreshAdBanner(true,true);
        refreshAdBanner1(true,true);*/



        recy_view.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(
                this, recy_view, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PujaMainModel strotraModel= modelArrayList.get(position);

                Intent intent=new Intent(SubMenuPujaActivity.this, BhavnaActivity.class);
                intent.putExtra("pujaModelList",strotraModel.getModelArrayList());
                intent.putExtra("title",strotraModel.getName_hindi());

                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        UiUtils uiUtils=new UiUtils();

        JSONObject jsonStrotraObject= uiUtils.getPujalistCategoryWise(this);
        modelArrayList= uiUtils. parseJsonforPujaCategoryWise(jsonStrotraObject,modelArrayList);


        subMenuAdaptor.notifyDataSetChanged();
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

package com.jain.temple.jainmandirdarshan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.PoemListAdaptor;
import com.jain.temple.jainmandirdarshan.model.PoemModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 9/7/2017.
 */

public class PoemListActivity extends BaseActivity implements FragmentDrawerListener {

    private ArrayList<PoemModel> poemModelArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_poem_list);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView RecyclerView= (RecyclerView) findViewById(R.id.recy_view);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));


        poemModelArrayList=new ArrayList<>();
        PoemListAdaptor poemListAdaptor=new PoemListAdaptor(this,poemModelArrayList,this);
        RecyclerView.setAdapter(poemListAdaptor);


        UiUtils uiUtils=new UiUtils();

        JSONObject jsonObject= uiUtils.getPoemList(this);
        poemModelArrayList=  uiUtils.parseJsonforPoem(jsonObject,poemModelArrayList);

        poemListAdaptor.notifyDataSetChanged();

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
    public void onDrawerItemSelected(View view, int position) {
      PoemModel poemModel=  poemModelArrayList.get(position);

        Intent  intent=new Intent(this,PoemDetailActivity.class);
        intent.putExtra("poemList",poemModelArrayList);
        intent.putExtra("position",position);

        startActivity(intent);


    }
}

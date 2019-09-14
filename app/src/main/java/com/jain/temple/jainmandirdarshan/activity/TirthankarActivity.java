package com.jain.temple.jainmandirdarshan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Fragment.FragmentDrawer;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.TirthankarListAdaptor;
import com.jain.temple.jainmandirdarshan.model.TirthankarModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 7/27/2017.
 */

public class TirthankarActivity extends BaseActivity {
    private RecyclerView tirthankar_recy;
    private TirthankarListAdaptor tirthankarListAdaptor;
    private ArrayList<TirthankarModel> modelArrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.thirthankarlist_fragment);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);


        toolbar.setTitle(R.string.tirthankar);

        tirthankar_recy = (RecyclerView) findViewById(R.id.tirthankar_recy);


        modelArrayList = new ArrayList<>();
        tirthankar_recy.setLayoutManager(new LinearLayoutManager(this));
        tirthankarListAdaptor = new TirthankarListAdaptor(this, modelArrayList);
        tirthankar_recy.setAdapter(tirthankarListAdaptor);


        getTirthankarList();

    }

    private void getTirthankarList() {

        UiUtils uiUtils = new UiUtils();

        JSONObject jsonpujaObject = uiUtils.getTirthankarlist(this);
        modelArrayList = uiUtils.parseJsonforTirthankar(jsonpujaObject, modelArrayList);

        tirthankarListAdaptor.notifyDataSetChanged();

        tirthankar_recy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }

            }
        });

        tirthankar_recy.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(
                this, tirthankar_recy, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                TirthankarModel tirthankarModel = modelArrayList.get(position);

                Intent intent = new Intent(TirthankarActivity.this, TirthankarDetailActivity.class);
                intent.putExtra("model", tirthankarModel);

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

      /* // SQLiteDatabase db = SQLiteDatabase.openDatabase(assetManager., null, 0);
        TirthankarDataBaseHelper tirthankarDataBaseHelper = new TirthankarDataBaseHelper(getActivity());
        try {
            tirthankarDataBaseHelper. crateDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tirthankarDataBaseHelper.openDataBase();

        modelArrayList= tirthankarDataBaseHelper.getTirthankarList(modelArrayList);

        tirthankarListAdaptor.notifyDataSetChanged();

        try {
            JSONObject jsonObjectRoot=new JSONObject();
            JSONArray jsonArray=new JSONArray();



        for (int i = 0; i <modelArrayList.size() ; i++) {
           TirthankarModel tirthankarModel= modelArrayList.get(i);
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("name",tirthankarModel.getTirthankarName());
            jsonObject.put("name_hindi",tirthankarModel.getName_hindi());
            jsonObject.put("lakshan_sign",tirthankarModel.getLakshanSign());
            jsonObject.put("sign_hindi",tirthankarModel.getSign_hindi());
            jsonObject.put("birthplace_hindi",tirthankarModel.getBirthplace_hindi());
            jsonObject.put("birth_place",tirthankarModel.getBirthPlace());
            jsonObject.put("birth_thithi",tirthankarModel.getBirthtithi());
            jsonObject.put("father_name",tirthankarModel.getFatherName());
            jsonObject.put("father_name_hindi",tirthankarModel.getFather_name_hindi());
            jsonObject.put("mother_name",tirthankarModel.getMotherName());
            jsonObject.put("mother_name_hindi",tirthankarModel.getMother_name_hindi());
            jsonObject.put("diksha_thithi",tirthankarModel.getDikshaTithi());
            jsonObject.put("Kevalgyan_thithi",tirthankarModel.getKevalgyanTithi());
            jsonObject.put("nakshatra",tirthankarModel.getNakshatra());
            jsonObject.put("diksha_sathi",tirthankarModel.getDikshaSathi());
            jsonObject.put("shadhak_Jeevan",tirthankarModel.getShadak_veevan());
            jsonObject.put("age_lived",tirthankarModel.getAge_lived());
            jsonObject.put("neervan_place",tirthankarModel.getNeervan_place());
            jsonObject.put("neervan_sathi",tirthankarModel.getNeervanSathi());
            jsonObject.put("neervan_thithi",tirthankarModel.getNeervanTithi());
            jsonObject.put("colour",tirthankarModel.getColour());

            jsonObject.put("neervan_place_hindi",tirthankarModel.getNeervan_place_hindi());

            jsonArray.put(jsonObject);
        }

   //     jsonObjectRoot.set(jsonArray);

            jsonObjectRoot.put("data",jsonArray);

            Log.d("dataaaaaaaaa",jsonObjectRoot.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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

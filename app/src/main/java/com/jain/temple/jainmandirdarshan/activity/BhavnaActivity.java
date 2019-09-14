package com.jain.temple.jainmandirdarshan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.StrotraListAdaptor;
import com.jain.temple.jainmandirdarshan.model.FavouriteModel;
import com.jain.temple.jainmandirdarshan.model.StrotraModel;
import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.CONTENT_CAT;

/**
 * Created by admin on 8/2/2017.
 */

public class BhavnaActivity extends BaseActivity implements FragmentDrawerListener {
    private static final String LIST_STATE_KEY ="STATE_KEY" ;
    private RecyclerView recy_view;
    private EditText search_et;
    public static ArrayList<StrotraModel> modelArrayList;
    private StrotraListAdaptor strotraListAdaptor;
    private int position;
    private int image;
    private ArrayList<FavouriteModel> favouriteModelArrayList;
    private LinearLayoutManager layoutManager;
    public static int index = -1;
    public static int top = -1;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bhavna);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        search_et = (EditText) findViewById(R.id.search_et);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        recy_view = (RecyclerView) findViewById(R.id.recy_view);


        appDatabase= AppDatabase.getAppDatabase(getApplicationContext());
      /*  strotraListAdaptor=new AudioListAdaptor(this,modelArrayList);*/
        layoutManager= new LinearLayoutManager(this);

        recy_view.setLayoutManager(layoutManager);


        Intent intent = getIntent();

        position = intent.getIntExtra("position", 0);



        modelArrayList = new ArrayList<>();



        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }

            }
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                strotraListAdaptor.getFilter().filter(s);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        UiUtils uiUtils = new UiUtils();


        switch (position) {
            case 2:
                actionBar.setTitle(R.string.strotra);
                JSONObject jsonStrotraObject = uiUtils.getStrotralist(this);
                modelArrayList = uiUtils.parseJsonforStrotra(jsonStrotraObject, modelArrayList);
                image = R.drawable.strot_bhakti_cat;
                break;
            case 3:
                actionBar.setTitle(R.string.chalisa);
                JSONObject jsonChalisaObject = uiUtils.getChalisalist(this);
                modelArrayList = uiUtils.parseJsonforChalisa(jsonChalisaObject, modelArrayList);
                image = R.drawable.chalisa_cat;
                break;
            case 4:
                actionBar.setTitle(R.string.puja);
                JSONObject jsonpujaObject = uiUtils.getPujalist(this);
                modelArrayList = uiUtils.parseJsonforPuja(jsonpujaObject, modelArrayList);
                image = R.drawable.pooja_cat;
                break;
            case 5:
                actionBar.setTitle(R.string.bhavna);
                JSONObject jsonBhavnaObject = uiUtils.getBhavnalist(this);
                modelArrayList = uiUtils.parseJsonforBhavna(jsonBhavnaObject, modelArrayList);
                image = R.drawable.bhakti_cat;
                break;
            case 6:
                actionBar.setTitle(R.string.stuti);
                JSONObject jsonstutiObject = uiUtils.getStutiList(this);
                modelArrayList = uiUtils.parseJsonforStuti(jsonstutiObject, modelArrayList);
                image = R.drawable.stuti_cat;
                break;
            case 8:
                actionBar.setTitle(R.string.aarti);
                JSONObject jsonAartiObject = uiUtils.getAartiList(this);
                modelArrayList = uiUtils.parseJsonforAarti(jsonAartiObject, modelArrayList);
                image = R.drawable.aarti_cat;
                break;
            case 9:
                actionBar.setTitle(R.string.bhajan);
                JSONObject jsonBhajanObject = uiUtils.getBhajanList(this);
                modelArrayList = uiUtils.parseJsonforBhajan(jsonBhajanObject, modelArrayList);
                image = R.drawable.bhajan_cat;
                break;

            default:
                modelArrayList = intent.getParcelableArrayListExtra("pujaModelList");
                actionBar.setTitle(intent.getStringExtra("title"));
                image = R.drawable.pooja_cat;
                break;

        }

        strotraListAdaptor = new StrotraListAdaptor(this, modelArrayList, image, this);
        recy_view.setAdapter(strotraListAdaptor);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



    @Override
    public void onResume() {
        int i=0;
        super.onResume();

        favouriteModelArrayList = getAllFavoriteContentList();
        for (StrotraModel strotraModel : modelArrayList) {

            for (FavouriteModel favouriteModel : favouriteModelArrayList) {
                if (favouriteModel.getTitle().equalsIgnoreCase(strotraModel.getName_hindi())) {
                    strotraModel.setFavourite(true);
                    break;
                }
                else{
                    strotraModel.setFavourite(false);
                }

            }
            modelArrayList.set(i,strotraModel);

            i++;
        }



        strotraListAdaptor.notifyDataSetChanged();

        if(index != -1)
        {
            layoutManager.scrollToPositionWithOffset( index, top);
        }
    }

    private ArrayList<FavouriteModel> getAllFavoriteContentList() {
        ArrayList<FavouriteModel> modelArrayList=new ArrayList<>();
       /* DataBaseHelper db = new DataBaseHelper(this);
        modelArrayList = db.getAllFavoriteContentList(CONTENT_CAT);
        db.close();*/

       ArrayList<MyFavourateTempleEntity> myFavourateTempleEntities= (ArrayList<MyFavourateTempleEntity>) appDatabase.favouriteDao().getAllFavoriteContentList(CONTENT_CAT);

        for (MyFavourateTempleEntity myFavourateTempleEntity:myFavourateTempleEntities) {
            FavouriteModel favouriteModel=new FavouriteModel();
            favouriteModel.setTitle(myFavourateTempleEntity.getTITLE());
            favouriteModel.setDescription(myFavourateTempleEntity.getDESCRIPTION());
            favouriteModel.setContent_category(myFavourateTempleEntity.getCATEGORY());

            modelArrayList.add(favouriteModel);

        }
        return modelArrayList;
    }

    private void deleteFavoriteContent(StrotraModel strotraModel, int Position1) {
      /*  DataBaseHelper db = new DataBaseHelper(this);
        db.deleteFavoriteContent(strotraModel.getName_hindi());*/

      appDatabase.favouriteDao().deleteFavoriteContent(strotraModel.getName_hindi());

        strotraModel.setFavourite(false);
        modelArrayList.set(Position1, strotraModel);

        strotraListAdaptor.notifyDataSetChanged();
    }

    private void saveFavourite(StrotraModel strotraModel, int position) {
       /* DataBaseHelper db = new DataBaseHelper(this);
        db.AddFavouriteContent(strotraModel);*/
            MyFavourateTempleEntity myFavourateTempleEntity=new MyFavourateTempleEntity();
            myFavourateTempleEntity.setCATEGORY(CONTENT_CAT);
        myFavourateTempleEntity.setCONTENT_CATEGORY(strotraModel.getCategory());
        myFavourateTempleEntity.setDESCRIPTION(strotraModel.getDetail());
        myFavourateTempleEntity.setTITLE(strotraModel.getName_hindi());


       appDatabase.favouriteDao().AddFavouriteContent(myFavourateTempleEntity);
        strotraModel.setFavourite(true);
        modelArrayList.set(position, strotraModel);

        strotraListAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onPause()
    {

        super.onPause();
        //read current recyclerview position
        index = layoutManager.findFirstVisibleItemPosition();
        View v = recy_view.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recy_view.getPaddingTop());
    }



    @Override
    public void onDrawerItemSelected(View view, int position) {
        StrotraModel strotraModel = modelArrayList.get(position);
        switch (view.getId()) {
            case R.id.favourite_iv:

                if (strotraModel.isFavourite()) {
                    deleteFavoriteContent(strotraModel, position);
                } else {
                    saveFavourite(strotraModel, position);
                }
                break;
            case R.id.layout:
                lanchActivity(strotraModel);
                break;
            case R.id.strotra_image:
                lanchActivity(strotraModel);
                break;
        }


    }

    private void lanchActivity(StrotraModel strotraModel) {
        Intent intent = new Intent(BhavnaActivity.this, FullStrotraActivity.class);
        intent.putExtra("strotraModel", strotraModel);
        startActivity(intent);
    }
}

package com.jain.temple.jainmandirdarshan.activity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.StrotraModel;
import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.CONTENT_CAT;

/**
 * Created by admin on 7/28/2017.
 */

public class FullStrotraActivity extends BaseActivity {
    private TextView strotra_detail;
    private static String LOG_TAG = "EXAMPLE";
    private Menu menu;
    private boolean isFavorite;
    private StrotraModel strotraModel;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acticity_full_strotra);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
         strotraModel = intent.getParcelableExtra("strotraModel");
        appDatabase= AppDatabase.getAppDatabase(getApplicationContext());


        actionBar.setTitle(strotraModel.getName_hindi());

        strotra_detail = (TextView) findViewById(R.id.strotra_detail);
        strotra_detail.setText(Html.fromHtml(strotraModel.getDetail()));
        strotra_detail.setMovementMethod(new ScrollingMovementMethod());

    //    refreshAd(true,false);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.detail_option_menu, menu);
        menu.findItem(R.id.action_location).setVisible(false);
        menu.findItem(R.id.action_navigation).setVisible(false);
        getFavourite();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wishList:
                if (isFavorite) {
                    deleteFavorite();
                } else {
                    saveFavourite();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getFavourite() {
       // DataBaseHelper db = new DataBaseHelper(this);
        // FavouriteModel favouriteModel = db.getFavourite(nearPlaceLatLngModel.getPlaceId());
      MyFavourateTempleEntity myFavourateTempleEntity= appDatabase.favouriteDao().isFavouriteContent(strotraModel.getName_hindi());
      //  boolean isFavourite = db.isFavouriteContent(strotraModel.getName_hindi());

        if (myFavourateTempleEntity!=null) {
            isFavorite = true;
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
        } else {
            isFavorite = false;
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }

        /*if (isFavourite) {
            isFavorite = true;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
        } else {
            isFavorite = false;
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }*/
    }

    private void deleteFavorite() {
       /* DataBaseHelper db = new DataBaseHelper(this);
        db.deleteFavoriteContent(strotraModel.getName_hindi());*/
        appDatabase.favouriteDao().deleteFavoriteContent(strotraModel.getName_hindi());


        isFavorite = false;
        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
    }

    private void saveFavourite() {
      /*  DataBaseHelper db = new DataBaseHelper(this);
        db.AddFavouriteContent(strotraModel);*/

        MyFavourateTempleEntity myFavourateTempleEntity=new MyFavourateTempleEntity();
        myFavourateTempleEntity.setCATEGORY(CONTENT_CAT);
        myFavourateTempleEntity.setCONTENT_CATEGORY(strotraModel.getCategory());
        myFavourateTempleEntity.setDESCRIPTION(strotraModel.getDetail());
        myFavourateTempleEntity.setTITLE(strotraModel.getName_hindi());

        appDatabase.favouriteDao().AddFavouriteContent(myFavourateTempleEntity);

        isFavorite = true;
        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp));
    }

}

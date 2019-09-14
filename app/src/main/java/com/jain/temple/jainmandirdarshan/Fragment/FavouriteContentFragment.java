package com.jain.temple.jainmandirdarshan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.FullStrotraActivity;
import com.jain.temple.jainmandirdarshan.activity.HomeActivity;
import com.jain.temple.jainmandirdarshan.adaptor.FavouriteContentAdaptor;
import com.jain.temple.jainmandirdarshan.model.FavouriteModel;
import com.jain.temple.jainmandirdarshan.model.StrotraModel;
import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;

import java.util.ArrayList;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.CONTENT_CAT;

/**
 * Created by admin on 10/4/2017.
 */

public class FavouriteContentFragment extends Fragment implements ListenerData {
    private RecyclerView favorite_recy;
    private FavouriteContentAdaptor favouriteContentAdaptor;
    private ArrayList<FavouriteModel> modelArrayList;
    private LinearLayout no_record_layout;
    private TextView search_tv,no_fav_tv;
    private AppDatabase appDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_content, container, false);

        inItView(rootView);

        return rootView;
    }

    private void inItView(View rootView) {
        favorite_recy=rootView.findViewById(R.id.favorite_content_recy);
        favorite_recy.setLayoutManager(new LinearLayoutManager(getContext()));

        search_tv=rootView.findViewById(R.id.search_tv);
        no_record_layout=rootView.findViewById(R.id.no_record_layout);
        no_fav_tv=rootView.findViewById(R.id.no_fav_tv);

        appDatabase=AppDatabase.getAppDatabase(getActivity());
        no_fav_tv.setText(getString(R.string.no_favourite_content_found));

        modelArrayList = new ArrayList<>();

        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        modelArrayList.clear();
        modelArrayList = getAllFavoriteList();

        favouriteContentAdaptor=new FavouriteContentAdaptor(modelArrayList,this);
        favorite_recy.setAdapter(favouriteContentAdaptor);

        if (modelArrayList.size() > 0) {
            no_record_layout.setVisibility(View.GONE);
            favorite_recy.setVisibility(View.VISIBLE);
        } else {
            no_record_layout.setVisibility(View.VISIBLE);
            favorite_recy.setVisibility(View.GONE);
        }
        favouriteContentAdaptor.notifyDataSetChanged();

    }



    private void lanchActivity(StrotraModel strotraModel) {
        Intent intent = new Intent(getActivity(), FullStrotraActivity.class);
        intent.putExtra("strotraModel", strotraModel);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(View view, Object object) {
        FavouriteModel favouriteModel= (FavouriteModel) object;
        switch (view.getId()){
            case R.id.title_tv:
                StrotraModel strotraModel=new StrotraModel();
                strotraModel.setName_hindi(favouriteModel.getTitle());
                strotraModel.setDetail(favouriteModel.getDescription());
                lanchActivity(strotraModel);
                break;
            case R.id.clear_iv:
                deleteFavoriteContent(favouriteModel);
                break;
        }

    }

    public ArrayList<FavouriteModel> getAllFavoriteList() {
        /*DataBaseHelper db = new DataBaseHelper(getActivity());
        modelArrayList=db.getAllFavoriteContentList(CONTENT_CAT);
        db.close();*/
      ArrayList<MyFavourateTempleEntity> myFavourateTempleEntities= (ArrayList<MyFavourateTempleEntity>) appDatabase.favouriteDao().getAllFavoriteContentList(CONTENT_CAT);

        for (MyFavourateTempleEntity myFavourateTempleEntity:myFavourateTempleEntities) {
            FavouriteModel favouriteModel = new FavouriteModel();

            favouriteModel.setTitle(myFavourateTempleEntity.getTITLE());
            favouriteModel.setDescription(myFavourateTempleEntity.getDESCRIPTION());
            favouriteModel.setContent_category(myFavourateTempleEntity.getCONTENT_CATEGORY());

            modelArrayList.add(favouriteModel);

        }
        return modelArrayList;
    }

    private void deleteFavoriteContent(FavouriteModel favouriteModel) {
        /*DataBaseHelper db = new DataBaseHelper(getActivity());
        db.deleteFavoriteContent(favouriteModel.getTitle());*/
        appDatabase.favouriteDao().deleteFavoriteContent(favouriteModel.getTitle());


        modelArrayList.remove(favouriteModel);

        if (modelArrayList.size() > 0) {
            no_record_layout.setVisibility(View.GONE);
            favorite_recy.setVisibility(View.VISIBLE);
        } else {
            no_record_layout.setVisibility(View.VISIBLE);
            favorite_recy.setVisibility(View.GONE);
        }
        favouriteContentAdaptor.notifyDataSetChanged();
    }
}

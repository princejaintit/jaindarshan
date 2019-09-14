package com.jain.temple.jainmandirdarshan.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.FavouriteActivity;
import com.jain.temple.jainmandirdarshan.activity.FindTempleActivity;
import com.jain.temple.jainmandirdarshan.activity.TempleContactDetailActivity;
import com.jain.temple.jainmandirdarshan.adaptor.FavoriteListAdaptor;
import com.jain.temple.jainmandirdarshan.model.FavouriteModel;
import com.jain.temple.jainmandirdarshan.model.NearPlaceLatLngModel;
import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import java.util.ArrayList;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.TEMPLE_CAT;

/**
 * Created by admin on 7/25/2017.
 */

public class FavoriteTempleFragment extends Fragment implements ListenerData {
    private RecyclerView favorite_recy;
    ArrayList<FavouriteModel> modelArrayList;
    private FavoriteListAdaptor favoriteListAdaptor;
    private FavouriteModel favouriteModel;
    private LinearLayout no_record_layout;
    private TextView search_tv;
    private AppDatabase appDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        favorite_recy = (RecyclerView) rootView.findViewById(R.id.favorite_recy);

        favorite_recy.setLayoutManager(new LinearLayoutManager(getContext()));

        no_record_layout = rootView.findViewById(R.id.no_record_layout);
        search_tv = rootView.findViewById(R.id.search_tv);

        modelArrayList = new ArrayList<>();
        appDatabase=AppDatabase.getAppDatabase(getActivity());


        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FindTempleActivity.class));
            }
        });
    }

    private ArrayList<FavouriteModel> getAllFavoriteTempleList() {
        /*DataBaseHelper db = new DataBaseHelper(getActivity());
        modelArrayList = db.getAllFavoriteTempleList(TEMPLE_CAT);
        db.close();*/

        ArrayList<MyFavourateTempleEntity> myFavourateTempleEntities= (ArrayList<MyFavourateTempleEntity>) appDatabase.favouriteDao().getAllFavoriteTempleList(TEMPLE_CAT);
        for (MyFavourateTempleEntity myFavourateTempleEntity:myFavourateTempleEntities) {
            FavouriteModel favouriteModel=new FavouriteModel();

            favouriteModel.setPlaceId(myFavourateTempleEntity.getPLACE_ID());
            favouriteModel.setTempleName(myFavourateTempleEntity.getNAME());
            favouriteModel.setTempleAddress(myFavourateTempleEntity.getADDRESS());
            favouriteModel.setTempleContactNo(myFavourateTempleEntity.getCONTACT_NO());
            favouriteModel.setTempleRating(myFavourateTempleEntity.getRATING());
            favouriteModel.setPicRefrence(myFavourateTempleEntity.getT_PIC());
            favouriteModel.setLatLng(new LatLng(Double.parseDouble(myFavourateTempleEntity.getLATITUDE()), Double.parseDouble(myFavourateTempleEntity.getLONGITUDE())));

            modelArrayList.add(favouriteModel);
        }
        return modelArrayList;
    }

    @Override
    public void onItemSelected(View view, Object o) {
        this.favouriteModel = (FavouriteModel) o;
        switch (view.getId()) {
            case R.id.delete_iv:
                favouriteModel.setCategory(TEMPLE_CAT);
                deleteFavoriteTemple(favouriteModel);
                break;
            case R.id.contact_info_tv:
                callDetailActivity(favouriteModel);
                break;
            case R.id.temple_img:
                callDetailActivity(favouriteModel);
                break;
            case R.id.call_iv:
                boolean check = new UiUtils().isCallPermissionGrantedFragment(getActivity(), this);
                if (check) {
                    launchCallActiivty();
                }
                break;
            case R.id.share_iv:
                new UiUtils().callShareTempleInfo(getActivity(), favouriteModel);
                break;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        modelArrayList.clear();

        modelArrayList = getAllFavoriteTempleList();
        favoriteListAdaptor = new FavoriteListAdaptor(getActivity(), modelArrayList, this);

        favorite_recy.setAdapter(favoriteListAdaptor);

        if (modelArrayList.size() > 0) {
            no_record_layout.setVisibility(View.GONE);
            favorite_recy.setVisibility(View.VISIBLE);
        } else {
            no_record_layout.setVisibility(View.VISIBLE);
            favorite_recy.setVisibility(View.GONE);
        }
        favoriteListAdaptor.notifyDataSetChanged();

    }

    private void launchCallActiivty() {
        ((FavouriteActivity) getActivity()).showsnackBarMessage("No does not exit");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            launchCallActiivty();
        }
    }

    private void callDetailActivity(FavouriteModel favouriteModel) {
        NearPlaceLatLngModel nearPlaceLatLngModel = new NearPlaceLatLngModel();

        nearPlaceLatLngModel.setLatLng(favouriteModel.getLatLng());
        nearPlaceLatLngModel.setTempleName(favouriteModel.getTempleName());
        nearPlaceLatLngModel.setPlaceId(favouriteModel.getPlaceId());
        nearPlaceLatLngModel.setTempleAddress(favouriteModel.getTempleAddress());

        startActivity(new Intent(getActivity(), TempleContactDetailActivity.class).putExtra("nearPlaceLatLngModel", nearPlaceLatLngModel));
    }

    private void deleteFavoriteTemple(FavouriteModel favouriteModel) {
        /*DataBaseHelper db = new DataBaseHelper(getActivity());
        db.deleteFavoriteTemple(favouriteModel);*/

        appDatabase.favouriteDao().deleteFavoriteTemple(favouriteModel.getPlaceId());

        modelArrayList.remove(favouriteModel);
        favoriteListAdaptor.notifyDataSetChanged();
    }
}

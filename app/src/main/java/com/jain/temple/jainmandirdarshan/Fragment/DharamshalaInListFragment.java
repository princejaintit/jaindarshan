package com.jain.temple.jainmandirdarshan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.TempleContactDetailActivity;
import com.jain.temple.jainmandirdarshan.adaptor.TempleListAdaptor;
import com.jain.temple.jainmandirdarshan.model.MapDataModel;
import com.jain.temple.jainmandirdarshan.model.NearPlaceLatLngModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 7/6/2017.
 */

public class DharamshalaInListFragment extends Fragment implements AdaptorClickInterface {
    private RecyclerView temple_list_recy;
    private static DharamshalaInListFragment templeInListFragment;
    private TempleListAdaptor templeListAdaptor;
    private ArrayList<NearPlaceLatLngModel> placeLatLngModelArrayList;
    private MapDataModel mapDataModel;
    private LinearLayout no_record_layout;

    public DharamshalaInListFragment() {
    }

    public static DharamshalaInListFragment newInstance() {

        if (templeInListFragment == null) {

            templeInListFragment = new DharamshalaInListFragment();
        }

        return templeInListFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_temple, container, false);

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        temple_list_recy = (RecyclerView) rootView.findViewById(R.id.temple_list_recy);

        no_record_layout = rootView.findViewById(R.id.no_record_layout);
        temple_list_recy.setLayoutManager(new LinearLayoutManager(getActivity()));


        Bundle bundle = getArguments();
        if (bundle != null) {
            mapDataModel = bundle.getParcelable("data");

            showOnList(mapDataModel);
        }


    }


    public void showOnList(MapDataModel mapDataModel) {
        // placeLatLngModelArrayList= mapDataModel.getNearPlaceLatLngModelList();

        HashMap<Integer, NearPlaceLatLngModel> destLatLngModelArrayList = mapDataModel.getDestinationNearPlaceLatLngModelList();

        if (destLatLngModelArrayList != null) {
            HashMap<Integer, NearPlaceLatLngModel> sourceLatLngModelArrayList = mapDataModel.getSourceNearPlaceLatLngModelList();
            if (sourceLatLngModelArrayList != null) {
                destLatLngModelArrayList.putAll(sourceLatLngModelArrayList);
            }

            templeListAdaptor = new TempleListAdaptor(destLatLngModelArrayList, getActivity(), this);
            temple_list_recy.setAdapter(templeListAdaptor);


            if (destLatLngModelArrayList.size() > 0) {
                temple_list_recy.setVisibility(View.VISIBLE);
                no_record_layout.setVisibility(View.GONE);
            } else {
                temple_list_recy.setVisibility(View.GONE);
                no_record_layout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void getitemClickPosition(Object object) {
        NearPlaceLatLngModel nearPlaceLatLngModel = (NearPlaceLatLngModel) object;

        Intent intent = new Intent(getActivity(), TempleContactDetailActivity.class);
        intent.putExtra("nearPlaceLatLngModel", nearPlaceLatLngModel);
        intent.putExtra("destinationLatLng", mapDataModel.getDestlatLng());
        intent.putExtra("sourceLatLng", mapDataModel.getSourcelatLng());

        startActivity(intent);

    }
    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

    }



    @Override
    public void onDestroy() {


        super.onDestroy();

    }
}

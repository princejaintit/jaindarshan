package com.jain.temple.jainmandirdarshan.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener;
import com.jain.temple.jainmandirdarshan.MediaPlayer.PlayerActivity;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.AudioListActivity;
import com.jain.temple.jainmandirdarshan.adaptor.FavouriteAudioAdaptor;
import com.jain.temple.jainmandirdarshan.model.AudioModel;
import com.jain.temple.jainmandirdarshan.roomORM.database.AppDatabase;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;

import java.util.ArrayList;

import static com.jain.temple.jainmandirdarshan.util.AppConfig.AUDIO_CAT;

/**
 * Created by admin on 10/5/2017.
 */

public class FavoriteAudioFragment extends Fragment implements FragmentDrawerListener {
    private RecyclerView favorite_recy;
    private FavouriteAudioAdaptor favouriteAudioAdaptor;
    private ArrayList<AudioModel> audioModelArrayList;
    private LinearLayout no_record_layout;
    private TextView search_tv,no_fav_tv;
    private ImageView val_iv;
    private AppDatabase appDatabase  ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_content, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        favorite_recy=rootView.findViewById(R.id.favorite_content_recy);
        favorite_recy.setLayoutManager(new LinearLayoutManager(getContext()));

        search_tv=rootView.findViewById(R.id.search_tv);
        no_record_layout=rootView.findViewById(R.id.no_record_layout);
        no_fav_tv=rootView.findViewById(R.id.no_fav_tv);

        val_iv=rootView.findViewById(R.id.val_iv);

        val_iv.setVisibility(View.VISIBLE);
        appDatabase= AppDatabase.getAppDatabase(getActivity());


        no_fav_tv.setText(getString(R.string.no_favourite_audio_found));

        audioModelArrayList = new ArrayList<>();

        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AudioListActivity.class));
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        audioModelArrayList.clear();
        audioModelArrayList = getAllFavoriteList();

        favouriteAudioAdaptor=new FavouriteAudioAdaptor(audioModelArrayList,this);
        favorite_recy.setAdapter(favouriteAudioAdaptor);

        if (audioModelArrayList.size() > 0) {
            no_record_layout.setVisibility(View.GONE);
            favorite_recy.setVisibility(View.VISIBLE);
        } else {
            no_record_layout.setVisibility(View.VISIBLE);
            favorite_recy.setVisibility(View.GONE);
        }
        favouriteAudioAdaptor.notifyDataSetChanged();

    }

    public ArrayList<AudioModel> getAllFavoriteList() {
       // DataBaseHelper db = new DataBaseHelper(getActivity());
      ArrayList<MyFavourateTempleEntity>  audioModelArrayList1= (ArrayList<MyFavourateTempleEntity>) appDatabase.favouriteDao().getAllFavoriteAudioList(AUDIO_CAT);

        for (MyFavourateTempleEntity entity:audioModelArrayList1) {
            AudioModel audioModel=new AudioModel();
            audioModel.setCategory(entity.getCONTENT_CATEGORY());
            audioModel.setDetail(entity.getDESCRIPTION());
            audioModel.setTitle_h(entity.getTITLE());
           // audioModel.setTitle_e(entity.);
            audioModel.setUrl_refrence(entity.getURL_REFRENCE());

            audioModelArrayList.add(audioModel);

        }
        /*audioModelArrayList=db.getAllFavoriteAudioList(AUDIO_CAT);
        db.close();*/
        return audioModelArrayList;
    }

    private void deleteFavoriteContent(AudioModel audioModel) {
     //   DataBaseHelper db = new DataBaseHelper(getActivity());
        appDatabase.favouriteDao().deleteFavoriteAudio(audioModel.getTitle_h());
      //  db.deleteFavoriteAudio(audioModel.getTitle_h());

        audioModelArrayList.remove(audioModel);

        if (audioModelArrayList.size() > 0) {
            no_record_layout.setVisibility(View.GONE);
            favorite_recy.setVisibility(View.VISIBLE);
        } else {
            no_record_layout.setVisibility(View.VISIBLE);
            favorite_recy.setVisibility(View.GONE);
        }
        favouriteAudioAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        AudioModel audioModel = audioModelArrayList.get(position);
        switch (view.getId()) {
            case R.id.play_iv:
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("audioModel", audioModel);
                startActivity(intent);
                break;
            case R.id.delete_tv:
                deleteFavoriteContent(audioModel);
                break;
        }


    }
}

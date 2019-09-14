package com.jain.temple.jainmandirdarshan.adaptor;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.AudioModel;
import com.jain.temple.jainmandirdarshan.roomORM.entity.MyFavourateTempleEntity;

import java.util.ArrayList;

/**
 * Created by admin on 10/5/2017.
 */

public class FavouriteAudioAdaptor extends RecyclerView.Adapter<FavouriteAudioAdaptor.DataObjectViewHolder>{
    private ArrayList<AudioModel> audioModelArrayList;
    private FragmentDrawerListener fragmentDrawerListener;

    public FavouriteAudioAdaptor(ArrayList<AudioModel> audioModelArrayList, FragmentDrawerListener fragmentDrawerListener) {
        this.audioModelArrayList = audioModelArrayList;
        this.fragmentDrawerListener = fragmentDrawerListener;
    }



    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_list_adaptor, parent, false);
        return new FavouriteAudioAdaptor.DataObjectViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final DataObjectViewHolder holder, final int position) {
        AudioModel audioModel=  audioModelArrayList.get(position);
        holder.audio_tv.setText(audioModel.getTitle_h());

        holder.audio_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onDrawerItemSelected( holder.play_iv,position);
            }
        });

        holder.play_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onDrawerItemSelected( holder.play_iv,position);
            }
        });

        holder.delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onDrawerItemSelected( holder.delete_tv,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioModelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView audio_tv;
        private ImageView play_iv,delete_tv;

        public DataObjectViewHolder(View itemView) {
            super(itemView);

            audio_tv=itemView.findViewById(R.id.audio_tv);
            delete_tv=itemView.findViewById(R.id.delete_tv);
            play_iv=itemView.findViewById(R.id.play_iv);
        }
    }
}

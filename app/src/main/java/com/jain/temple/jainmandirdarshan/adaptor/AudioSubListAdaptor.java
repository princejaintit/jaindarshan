package com.jain.temple.jainmandirdarshan.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.AudioModel;

import java.util.ArrayList;

/**
 * Created by admin on 9/8/2017.
 */

public class AudioSubListAdaptor extends RecyclerView.Adapter<AudioSubListAdaptor.DataObjectViewHolder> {
    private Context activity;
    private ArrayList<AudioModel>  audioModelArrayList;
    private ListenerData fragmentDrawerListener;

    public AudioSubListAdaptor(ArrayList<AudioModel> audioModelArrayList, ListenerData fragmentDrawerListener) {
        this.audioModelArrayList = audioModelArrayList;
        this.fragmentDrawerListener=fragmentDrawerListener;
    }

    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.activity=parent.getContext();
      View itemView=  LayoutInflater.from(activity).inflate(R.layout.audio_sub_cell,parent,false);
        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataObjectViewHolder holder, final int position) {
        final AudioModel audioModel=audioModelArrayList.get(position);

        holder.sub_title.setText(audioModel.getTitle_h());

        holder.play_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onItemSelected( holder.play_iv,audioModel);
            }
        });

        holder.sub_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onItemSelected( holder.play_iv,audioModel);
            }
        });

        holder.download_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onItemSelected( holder.download_iv,audioModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioModelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView sub_title;
        private ImageView play_iv,download_iv;

        public DataObjectViewHolder(View itemView) {
            super(itemView);

            sub_title=itemView.findViewById(R.id.sub_title);
            download_iv=itemView.findViewById(R.id.download_iv);
            play_iv=itemView.findViewById(R.id.play_iv);
        }
    }
}

package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.MediaPlayer.PlayerActivity;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.AudioMainModel;
import com.jain.temple.jainmandirdarshan.model.AudioModel;

import java.util.ArrayList;

/**
 * Created by admin on 9/6/2017.
 */

public class AudioListAdaptor extends RecyclerView.Adapter<AudioListAdaptor.DataObjectViewHolder> implements ListenerData {
    private Activity activity;
    private ArrayList<AudioMainModel> mainModelArrayList;
    private ArrayList<AudioModel> audioModelArrayList;
    private AdaptorClickInterface adaptorClickInterface;


    public AudioListAdaptor(Activity activity, ArrayList<AudioMainModel> modelArrayList, AdaptorClickInterface adaptorClickInterface) {
        this.activity = activity;
        this.mainModelArrayList = modelArrayList;
        this.adaptorClickInterface = adaptorClickInterface;
    }

    @Override
    public AudioListAdaptor.DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_cell, parent, false);
        return new AudioListAdaptor.DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AudioListAdaptor.DataObjectViewHolder holder, final int position) {
        final AudioMainModel audioMainModel = mainModelArrayList.get(position);

        holder.audio_name_h_tv.setText(audioMainModel.getTitle());
        holder.audio_count_tv.setText("("+audioMainModel.getAudioModelArrayList().size()+")");

        if (audioMainModel.isHasMore()) {
            holder.sub_list_layout.setVisibility(View.VISIBLE);
            holder.more_iv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_expand_less_black_36dp));
        } else {

            holder.sub_list_layout.setVisibility(View.GONE);
            holder.more_iv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_expand_more_black_36dp));


        }

        audioModelArrayList = audioMainModel.getAudioModelArrayList();
        AudioSubListAdaptor audioSubListAdaptor = new AudioSubListAdaptor(audioModelArrayList, this);
        holder.sub_recy.setAdapter(audioSubListAdaptor);

        holder.whole_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < mainModelArrayList.size(); i++) {
                    AudioMainModel audioMainModel = mainModelArrayList.get(i);
                    if (i == position) {
                        if (audioMainModel.isHasMore()) {
                            holder.sub_list_layout.setVisibility(View.GONE);
                            audioMainModel.setHasMore(false);
                            holder.more_iv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_expand_more_black_36dp));
                        } else {
                            holder.sub_list_layout.setVisibility(View.VISIBLE);
                            audioMainModel.setHasMore(true);
                            holder.more_iv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_expand_less_black_36dp));

                        }

                    } else {
                        audioMainModel.setHasMore(false);
                    }
                    mainModelArrayList.set(i,audioMainModel);

                }



                notifyDataSetChanged();

            }

        });



    }

    @Override
    public int getItemCount() {
        return mainModelArrayList.size();
    }

/*    @Override
    public void onDrawerItemSelected(View view, int position) {
       AudioModel audioModel= audioModelArrayList.get(position);
        switch (view.getId()){
            case R.id.play_iv:
                activity.startActivity(new Intent(activity, PlayerActivity.class).putExtra("audioModel",audioModel));
                break;
            case  R.id.download_iv:
                adaptorClickInterface.getitemClickPosition(audioModel);
                break;
        }

    }*/


    @Override
    public void onItemSelected(View view, Object o) {
        AudioModel audioModel = (AudioModel) o;
        switch (view.getId()) {
            case R.id.play_iv:
                activity.startActivity(new Intent(activity, PlayerActivity.class).putExtra("audioModel", audioModel));
                break;
            case R.id.download_iv:
                adaptorClickInterface.getitemClickPosition(audioModel);
                break;
        }
    }


    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView audio_name_h_tv,audio_count_tv;
        private RecyclerView sub_recy;
        private ImageView more_iv;
        private LinearLayout sub_list_layout, whole_layout;

        public DataObjectViewHolder(View itemView) {
            super(itemView);

            audio_name_h_tv = itemView.findViewById(R.id.audio_name_h_tv);
            audio_count_tv=itemView.findViewById(R.id.audio_count_tv);
            more_iv = itemView.findViewById(R.id.more_iv);
            sub_list_layout = itemView.findViewById(R.id.sub_list_layout);
            whole_layout = itemView.findViewById(R.id.whole_layout);

            sub_recy = itemView.findViewById(R.id.sub_recy);
            sub_recy.setLayoutManager(new LinearLayoutManager(activity));

        }
    }


}

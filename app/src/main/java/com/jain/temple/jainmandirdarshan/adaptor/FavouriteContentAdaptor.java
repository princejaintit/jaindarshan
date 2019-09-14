package com.jain.temple.jainmandirdarshan.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.FavouriteModel;

import java.util.ArrayList;

/**
 * Created by admin on 10/4/2017.
 */

public class FavouriteContentAdaptor extends RecyclerView.Adapter<FavouriteContentAdaptor.DataObjectViewHolder> {
    private ArrayList<FavouriteModel> modelArrayList;
    private ListenerData listenerData;

    public FavouriteContentAdaptor(ArrayList<FavouriteModel> modelArrayList, ListenerData listenerData) {
        this.modelArrayList = modelArrayList;
        this.listenerData = listenerData;
    }

    @Override
    public FavouriteContentAdaptor.DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=  LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_content_cell,parent,false);
        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataObjectViewHolder holder, int position) {
        final FavouriteModel favouriteModel = modelArrayList.get(position);
        holder.title_tv.setText(favouriteModel.getTitle());

        holder.category_tv.setText(favouriteModel.getContent_category());

        holder.title_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerData.onItemSelected(holder.title_tv, favouriteModel);
            }
        });

        holder.clear_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerData.onItemSelected(holder.clear_iv, favouriteModel);
            }
        });
    }

    @Override
    public int getItemCount() {

        return modelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView title_tv, category_tv;
        private ImageView clear_iv;

        public DataObjectViewHolder(View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            category_tv = itemView.findViewById(R.id.category_tv);

            clear_iv = itemView.findViewById(R.id.clear_iv);
        }
    }
}

package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.PujaMainModel;
import com.jain.temple.jainmandirdarshan.model.StrotraModel;

import java.util.ArrayList;

/**
 * Created by admin on 8/3/2017.
 */

public class SubMenuAdaptor extends RecyclerView.Adapter<SubMenuAdaptor.DataObjectViewHolder> {
    private Activity activity;
    private ArrayList<PujaMainModel> strotraModelArrayList;

    public SubMenuAdaptor(Activity activity, ArrayList<PujaMainModel> strotraModelArrayList) {
        this.activity = activity;
        this.strotraModelArrayList = strotraModelArrayList;
    }

    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView=LayoutInflater.from(parent.getContext()).inflate(R.layout.submenu_puja_cell,parent,false);

        return new DataObjectViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DataObjectViewHolder holder, int position) {
        PujaMainModel pujaMainModel=strotraModelArrayList.get(position);

        holder.name_tv.setText(pujaMainModel.getName_hindi());
    }

    @Override
    public int getItemCount() {
        return strotraModelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name_tv;

        public DataObjectViewHolder(View itemView) {
            super(itemView);

            image= (ImageView) itemView.findViewById(R.id.image);
            name_tv= (TextView) itemView.findViewById(R.id.name_tv);
        }
    }
}

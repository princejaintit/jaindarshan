package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.PoemModel;

import java.util.ArrayList;

/**
 * Created by admin on 9/7/2017.
 */

public class PoemListAdaptor extends RecyclerView.Adapter<PoemListAdaptor.DataObjectViewHolder> {
    private Activity activity;
    private ArrayList<PoemModel> poemModelArrayList;
    private FragmentDrawerListener fragmentDrawerListener;

    public PoemListAdaptor(Activity activity, ArrayList<PoemModel> poemModelArrayList, FragmentDrawerListener fragmentDrawerListener) {
        this.activity = activity;
        this.poemModelArrayList = poemModelArrayList;
        this.fragmentDrawerListener=fragmentDrawerListener;
    }

    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View rootView= LayoutInflater.from(parent.getContext()).inflate(R.layout.poem_cell_item,parent,false);
       return new DataObjectViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final DataObjectViewHolder holder, final int position) {
        PoemModel poemModel= poemModelArrayList.get(position);

        holder.poem_tv.setText(poemModel.getTitle());

        holder.poem_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onDrawerItemSelected(holder.poem_tv,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return poemModelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView poem_tv;
        public DataObjectViewHolder(View itemView) {
            super(itemView);

            poem_tv=itemView.findViewById(R.id.poem_tv);
        }
    }
}

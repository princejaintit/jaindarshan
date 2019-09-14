package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel;
import com.jain.temple.jainmandirdarshan.model.DayDetail;

import java.util.ArrayList;

/**
 * Created by admin on 8/4/2017.
 */

public class DayDetailAdaptor extends RecyclerView.Adapter<DayDetailAdaptor.DataObjectViewHolder> {
    private Activity activity;
    private ArrayList<DayDetail> dayDetailArrayList;

    public DayDetailAdaptor(Activity activity, ArrayList<DayDetail> dayDetailArrayList) {
        this.activity = activity;
        this.dayDetailArrayList = dayDetailArrayList;
    }

    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView=LayoutInflater.from(parent.getContext()).inflate(R.layout.day_detail_cell,parent,false);
        return new DataObjectViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DataObjectViewHolder holder, int position) {
        DayDetail dayDetail=dayDetailArrayList.get(position);
        holder.text_name.setText(dayDetail.getDayName());
        holder.text_detail.setText(dayDetail.getDayDetail());
    }

    @Override
    public int getItemCount() {
        return dayDetailArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView text_name,text_detail;

        public DataObjectViewHolder(View itemView) {
            super(itemView);

            text_name= (TextView) itemView.findViewById(R.id.text_name);
            text_detail= (TextView) itemView.findViewById(R.id.text_detail);
        }
    }
}

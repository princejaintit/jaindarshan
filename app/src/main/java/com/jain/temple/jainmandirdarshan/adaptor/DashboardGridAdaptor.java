package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.DashboardModel;

import java.util.ArrayList;

/**
 * Created by admin on 7/21/2017.
 */

public class DashboardGridAdaptor extends RecyclerView.Adapter<DashboardGridAdaptor.DataObjectViewHolder> {
    private ArrayList<DashboardModel> dashboardModelList;
    private Activity activity;

    public DashboardGridAdaptor(Activity activity,ArrayList<DashboardModel> dashboardModelList) {
        this.dashboardModelList = dashboardModelList;
        this.activity = activity;
    }


    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_grid_adaptor_item_row,parent,false);
        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataObjectViewHolder holder, int position) {
        DashboardModel dashboardModel= dashboardModelList.get(position);

       // holder.image_bg_frame.setBackgroundColor(Color.parseColor(dashboardModel.getColor()));
        holder.image_iv.setImageResource(dashboardModel.getIcon());
        holder.title_tv.setText(dashboardModel.getName());
    }

    @Override
    public int getItemCount() {
        return dashboardModelList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout image_bg_frame;
        private ImageView image_iv;
        private TextView title_tv;


        public DataObjectViewHolder(View itemView) {
            super(itemView);

          //  image_bg_frame= (FrameLayout) itemView.findViewById(R.id.image_bg_frame);
            image_iv= (ImageView) itemView.findViewById(R.id.image_iv);
            title_tv= (TextView) itemView.findViewById(R.id.title_tv);
        }
    }
}

package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel;

import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

public class CalendarAdaptor extends RecyclerView.Adapter<CalendarAdaptor.DataObjectViewHolder> {
    private Activity activity;
    private ArrayList<CalendarDayModel> dayModelArrayList;

    public CalendarAdaptor(Activity activity, ArrayList<CalendarDayModel> dayModelArrayList) {
        this.activity = activity;
        this.dayModelArrayList = dayModelArrayList;
    }


    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView=  LayoutInflater.from(parent.getContext()).inflate(R.layout.calendat_cell,parent,false);
        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataObjectViewHolder holder, int position) {
        CalendarDayModel calendarDayModel= dayModelArrayList.get(position);

        if (calendarDayModel.getTithi_e().equals("")){
            holder.layout_cal.setVisibility(View.INVISIBLE);
        }
        else {

            holder.date_tv.setText(calendarDayModel.getDay_d());

//        holder.tithi_tv. setTypeface(Typeface.createFromAsset(activity.getAssets(),"Fonts/hindi.ttf"));

            holder.tithi_tv.setText(calendarDayModel.getTithi_eh());

            if (calendarDayModel.getSpecial().equalsIgnoreCase("Y")) {
                holder.whole_layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.calander_special_bg));
            } else {
                holder.whole_layout.setBackground(ContextCompat.getDrawable(activity, R.drawable.calendar_bg));
            }

            if (calendarDayModel.getTithi_e().equalsIgnoreCase("Ashtami")) {
                holder.tithi_tv.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
                holder.date_tv.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
            }
            if (calendarDayModel.getTithi_e().equalsIgnoreCase("Chaturdashi")) {

                holder.tithi_tv.setTextColor(ContextCompat.getColor(activity, R.color.green));
                holder.date_tv.setTextColor(ContextCompat.getColor(activity, R.color.green));

            }

            if (calendarDayModel.isTodayDate()) {

                holder.current_date.setBackground(ContextCompat.getDrawable(activity, R.drawable.circt_border_green));
            }
//        else{
//            holder.tithi_tv.setTextColor(ContextCompat.getColor(activity,R.color.colorAccent));
//        }

            if (holder.date_tv.getText().toString().equalsIgnoreCase("")) {
                holder.whole_layout.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent));
            }
        }

    }

    @Override
    public int getItemCount() {
        return dayModelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView date_tv,tithi_tv;
        private LinearLayout whole_layout;
        private LinearLayout current_date;
        private LinearLayout layout_cal;

        public DataObjectViewHolder(View itemView) {
            super(itemView);

            date_tv= (TextView) itemView.findViewById(R.id.date_tv);
            tithi_tv= (TextView) itemView.findViewById(R.id.tithi_tv);

            whole_layout= (LinearLayout) itemView.findViewById(R.id.whole_layout);

            current_date= (LinearLayout) itemView.findViewById(R.id.current_date);
            layout_cal=itemView.findViewById(R.id.layout_cal);
        }
    }
}

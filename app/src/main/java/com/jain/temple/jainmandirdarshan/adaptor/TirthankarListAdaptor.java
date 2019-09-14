package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.VolleyImageLoader;
import com.jain.temple.jainmandirdarshan.model.TirthankarModel;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by admin on 7/27/2017.
 */

public class TirthankarListAdaptor extends RecyclerView.Adapter<TirthankarListAdaptor.DataObjectViewHolder> {
    private Activity activity;
    private ArrayList<TirthankarModel> modelArrayList;
    private ImageLoader mImageLoader;

    public TirthankarListAdaptor(Activity activity, ArrayList<TirthankarModel> modelArrayList) {
        this.activity = activity;
        this.modelArrayList = modelArrayList;
        mImageLoader = VolleyImageLoader.getInstance(activity)
                .getImageLoader();
    }

    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.tirthankar_list_cell_item,parent,false);
        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataObjectViewHolder holder, int position) {
       TirthankarModel tirthankarModel= modelArrayList.get(position);

       /* mImageLoader.get(pic, ImageLoader.getImageListener(holder. tirthankar_image,
                android.R.drawable.ic_dialog_alert, android.R.drawable.ic_dialog_alert));*/
      //  holder.tirthankar_nameHindi_tv. setTypeface(Typeface.createFromAsset(activity.getAssets(),"Fonts/hindi.ttf"));
        holder.tirthankar_nameHindi_tv.setText(tirthankarModel.getName_hindi().trim()+" जी भगवान ");

        holder.tirthankar_name_tv.setText(tirthankarModel.getTirthankarName());
      // holder.tirthankar_name_tv.setText(StringEscapeUtils.unescapeJava(tirthankarModel.getTirthankarName()+"\\u092a\\u0942\\u0930\\u094d\\u0923\\u093e \\u0938\\u0902\\u0917\\u094d\\u092f\\u0915"));
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView tirthankar_name_tv,tirthankar_nameHindi_tv;
        private NetworkImageView tirthankar_image;


        public DataObjectViewHolder(View itemView) {
            super(itemView);

            tirthankar_name_tv= (TextView) itemView.findViewById(R.id.tirthankar_name_tv);
            tirthankar_nameHindi_tv= (TextView) itemView.findViewById(R.id.tirthankar_nameHindi_tv);

            tirthankar_image= (NetworkImageView) itemView.findViewById(R.id.tirthankar_image);

        }
    }
}

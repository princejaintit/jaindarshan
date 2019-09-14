package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.VolleyImageLoader;
import com.jain.temple.jainmandirdarshan.model.MapDataModel;
import com.jain.temple.jainmandirdarshan.model.NearPlaceLatLngModel;
import com.jain.temple.jainmandirdarshan.util.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 7/6/2017.
 */

public class TempleListAdaptor extends RecyclerView.Adapter<TempleListAdaptor.DataObjectViewHolder> {
    HashMap<Integer,NearPlaceLatLngModel> placeLatLngModelArrayList;
    private Activity activity;
    private ImageLoader mImageLoader;
    private AdaptorClickInterface adaptorClickInterface;

    public TempleListAdaptor(HashMap<Integer,NearPlaceLatLngModel> placeLatLngModelArrayList, Activity activity, AdaptorClickInterface adaptorClickInterface) {
        this.placeLatLngModelArrayList = placeLatLngModelArrayList;
        this.activity = activity;
        this.adaptorClickInterface=adaptorClickInterface;
        mImageLoader = VolleyImageLoader.getInstance(activity)
                .getImageLoader();

    }


    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView=  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_temple_item_row,parent,false);

        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataObjectViewHolder holder, int position) {
        final NearPlaceLatLngModel nearPlaceLatLngModel= placeLatLngModelArrayList.get(position);

        //Map.Entry m= (Map.Entry) placeLatLngModelArrayList.entrySet();

        holder.temple_name_tv.setText(nearPlaceLatLngModel.getTempleName());
        holder.temple_address_tv.setText(nearPlaceLatLngModel.getTempleAddress());

        mImageLoader.get(nearPlaceLatLngModel.getPhotoRefrence(), ImageLoader.getImageListener(holder. temple_img,
                R.drawable.locator, R.drawable.locator));
     //  holder. temple_img.setImageUrl(nearPlaceLatLngModel.getPhotoRefrence(), mImageLoader);

        holder.contact_info_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptorClickInterface.getitemClickPosition(nearPlaceLatLngModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeLatLngModelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView temple_name_tv,temple_address_tv,contact_info_tv;
        private CircleImageView temple_img;
        public DataObjectViewHolder(View itemView) {
            super(itemView);

            temple_name_tv= (TextView) itemView.findViewById(R.id.temple_name_tv);
            temple_address_tv= (TextView) itemView.findViewById(R.id.temple_address_tv);
            contact_info_tv= (TextView) itemView.findViewById(R.id.contact_info_tv);

            temple_img= (CircleImageView) itemView.findViewById(R.id.temple_img);



        }
    }
}

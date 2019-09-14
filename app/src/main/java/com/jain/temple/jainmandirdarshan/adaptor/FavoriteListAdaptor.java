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
import com.jain.temple.jainmandirdarshan.Interface.ListenerData;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.VolleyImageLoader;
import com.jain.temple.jainmandirdarshan.model.FavouriteModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 7/25/2017.
 */

public class FavoriteListAdaptor extends RecyclerView.Adapter<FavoriteListAdaptor.DataObjectViewHolder> {
    private Activity activity;
    private ArrayList<FavouriteModel> modelArrayList;
    private ImageLoader mImageLoader;
    private ListenerData listenerData;

    public FavoriteListAdaptor(Activity activity, ArrayList<FavouriteModel> modelArrayList, ListenerData  listenerData) {
        this.activity = activity;
        this.modelArrayList = modelArrayList;
        this.listenerData=listenerData;
        mImageLoader = VolleyImageLoader.getInstance(activity)
                .getImageLoader();
    }



    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView=  LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_item_row,parent,false);
        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataObjectViewHolder holder, int position) {
        final FavouriteModel favouriteModel= modelArrayList.get(position);

        holder.temple_name_tv.setText(favouriteModel.getTempleName());
        holder.temple_address_tv.setText(favouriteModel.getTempleAddress());
        holder.temple_contactNo_tv.setText(favouriteModel.getTempleContactNo());

        mImageLoader.get(favouriteModel.getPicRefrenceUrl(), ImageLoader.getImageListener(holder. temple_img,
                R.drawable.locator,   R.drawable.locator));

      /*ArrayList<String> picList=  favouriteModel.getPicUrlList();

        if (picList.size()>0) {

            String pic=picList.get(0);

        mImageLoader.get(pic, ImageLoader.getImageListener(holder. temple_img,
                R.drawable.locator,   R.drawable.locator));
       // holder. temple_img.setImageUrl(pic, mImageLoader);
        }*/

        holder.temple_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerData.onItemSelected(holder.temple_img,favouriteModel);
            }
        });

        holder.contact_info_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerData.onItemSelected(holder.contact_info_tv,favouriteModel);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerData.onItemSelected(holder.delete_iv,favouriteModel);
            }
        });

        holder.call_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerData.onItemSelected(holder.call_iv,favouriteModel);
            }
        });
        holder.share_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerData.onItemSelected(holder.share_iv,favouriteModel);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private TextView temple_name_tv,temple_address_tv,contact_info_tv,temple_contactNo_tv;
        private CircleImageView temple_img;
        private ImageView delete_iv,call_iv,share_iv;
        public DataObjectViewHolder(View itemView) {
            super(itemView);


            temple_name_tv= (TextView) itemView.findViewById(R.id.temple_name_tv);
            temple_address_tv= (TextView) itemView.findViewById(R.id.temple_address_tv);
            contact_info_tv= (TextView) itemView.findViewById(R.id.contact_info_tv);
            temple_contactNo_tv= (TextView) itemView.findViewById(R.id.temple_contactNo_tv);

            delete_iv= (ImageView) itemView.findViewById(R.id.delete_iv);
            call_iv= (ImageView) itemView.findViewById(R.id.call_iv);
            share_iv= (ImageView) itemView.findViewById(R.id.share_iv);

            temple_img= (CircleImageView) itemView.findViewById(R.id.temple_img);
        }
    }
}

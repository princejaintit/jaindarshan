package com.jain.temple.jainmandirdarshan.adaptor;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jain.temple.jainmandirdarshan.Interface.AdaptorClickInterface;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.helper.VolleyImageLoader;

import java.util.ArrayList;

/**
 * Created by admin on 7/7/2017.
 */

public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<String> image_arraylist;
    private ImageLoader mImageLoader;
    private AdaptorClickInterface adaptorClickInterface;
    private String root;

    public SliderPagerAdapter(Activity activity, ArrayList<String> image_arraylist, AdaptorClickInterface adaptorClickInterface, String root) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.adaptorClickInterface = adaptorClickInterface;
        this.root = root;
        mImageLoader = VolleyImageLoader.getInstance(activity)
                .getImageLoader();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        NetworkImageView im_slider = (NetworkImageView) view.findViewById(R.id.im_slider);

        mImageLoader.get(image_arraylist.get(position), ImageLoader.getImageListener(im_slider,
             R.drawable.place_holder,R.drawable.place_holder));

        im_slider.setImageUrl(image_arraylist.get(position), mImageLoader);
        container.addView(view);

        if (root.equalsIgnoreCase("root")) {

            im_slider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!image_arraylist.get(position).equalsIgnoreCase(""))
                    adaptorClickInterface.getitemClickPosition(position);
                }
            });
        }

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

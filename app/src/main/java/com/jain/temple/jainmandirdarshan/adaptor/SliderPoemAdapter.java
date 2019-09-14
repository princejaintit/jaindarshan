package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.model.PoemModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import java.util.ArrayList;

/**
 * Created by admin on 9/7/2017.
 */

public class SliderPoemAdapter  extends PagerAdapter {
    private Activity activity;
    private ArrayList<PoemModel> poemModelArrayList;

    public SliderPoemAdapter(Activity activity, ArrayList<PoemModel> poemModelArrayList) {
        this.activity = activity;
        this.poemModelArrayList = poemModelArrayList;

    }





    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.poem_detail_layout, container, false);

        PoemModel poemModel=  poemModelArrayList.get(position);

       TextView poem_detail_tv =view.findViewById(R.id.poem_detail_tv);

       UiUtils  uiUtils= new UiUtils();
      String content=  uiUtils.getPoemDetail(activity,poemModel.getContent());

        poem_detail_tv.setText(Html.fromHtml(content));

        container.addView(view);


        return view;
    }

    @Override
    public int getCount() {
        return poemModelArrayList.size();
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

package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.jain.temple.jainmandirdarshan.Interface.FragmentDrawerListener;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.BhavnaActivity;
import com.jain.temple.jainmandirdarshan.model.StrotraModel;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by admin on 7/28/2017.
 */

public class StrotraListAdaptor extends RecyclerView.Adapter<StrotraListAdaptor.DataObjectViewHolder> implements Filterable {
    private Activity activity;
    private ArrayList<StrotraModel> modelArrayList, modelArrayListFilter;
    private ValueFilter valueFilter;
    private String constraint = "";
    private int image;
    private FragmentDrawerListener fragmentDrawerListener;

    public StrotraListAdaptor(Activity activity, ArrayList<StrotraModel> modelArrayList, int image, FragmentDrawerListener fragmentDrawerListener) {
        this.activity = activity;
        this.modelArrayList = modelArrayList;
        this.modelArrayListFilter = modelArrayList;
        this.image = image;
        this.fragmentDrawerListener=fragmentDrawerListener;
    }

    @Override
    public DataObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strotralist_cell_model, parent, false);
        return new DataObjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataObjectViewHolder holder, final int position) {
        StrotraModel strotraModel = modelArrayList.get(position);
        holder.strotra_name_tv.setText(strotraModel.getName().toUpperCase());
        holder.strotra_nameHindi_tv.setText(position + 1 + "- " + strotraModel.getName_hindi());
        holder.strotra_image.setImageResource(image);


        if (strotraModel.getName().toLowerCase().contains(constraint)) {
            int startPos = strotraModel.getName().toLowerCase().indexOf(constraint.toString());
            int endPos = startPos + constraint.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.strotra_name_tv.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.strotra_name_tv.setText(spanString);
        }
        if (strotraModel.isFavourite()) {
            holder.favourite_iv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.selectwishlist));
        } else {
            holder.favourite_iv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.noselectwishlist));
        }

        holder.favourite_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onDrawerItemSelected(holder.favourite_iv,position);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onDrawerItemSelected(holder.layout,position);
            }
        });

        holder.strotra_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDrawerListener.onDrawerItemSelected(holder.strotra_image,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public class DataObjectViewHolder extends RecyclerView.ViewHolder {
        private ImageView strotra_image, favourite_iv;
        private TextView strotra_name_tv, strotra_nameHindi_tv;
        private LinearLayout layout;

        public DataObjectViewHolder(View itemView) {
            super(itemView);

            layout=itemView.findViewById(R.id.layout);

            strotra_image = (ImageView) itemView.findViewById(R.id.strotra_image);
            favourite_iv = itemView.findViewById(R.id.favourite_iv);

            strotra_name_tv = (TextView) itemView.findViewById(R.id.strotra_name_tv);
            strotra_nameHindi_tv = (TextView) itemView.findViewById(R.id.strotra_nameHindi_tv);
        }
    }

    @Override
    public Filter getFilter() {

        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            StrotraListAdaptor.this.constraint = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<StrotraModel> filterList = new ArrayList<>();

                for (int i = 0; i < modelArrayListFilter.size(); i++) {

                    StrotraModel strotraModel = modelArrayListFilter.get(i);


                    if (strotraModel.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {

                        filterList.add(strotraModel);

                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = modelArrayListFilter.size();

                results.values = modelArrayListFilter;

            }

            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            modelArrayList = (ArrayList<StrotraModel>) results.values;
            ((BhavnaActivity) activity).modelArrayList = (ArrayList<StrotraModel>) results.values;

            notifyDataSetChanged();


        }
    }

}

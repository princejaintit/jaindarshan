package com.jain.temple.jainmandirdarshan.adaptor;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jain.temple.jainmandirdarshan.R;

/**
 * Created by admin on 9/12/2017.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".
    private final View mWindow;

    //  private final View mContents;

     public CustomInfoWindowAdapter(Activity activity) {
        mWindow = activity.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        // mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {

        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

      /*  @Override
        public View getInfoContents(Marker marker) {

            render(marker, mContents);
            return mContents;
        }*/

    private void render(Marker marker, View view) {
        int badge;
        // Use the equals() method on a Marker to check for equals.  Do not use ==.

        badge = R.drawable.ic_info;

        ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null && snippet.length() > 12) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, snippet.length(), 0);
            // snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }
}
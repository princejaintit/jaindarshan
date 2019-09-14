package com.jain.temple.jainmandirdarshan.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.activity.AudioListActivity;
import com.jain.temple.jainmandirdarshan.activity.BhavnaActivity;
import com.jain.temple.jainmandirdarshan.activity.CalendarActivity;
import com.jain.temple.jainmandirdarshan.activity.FindDharamshalaActivity;
import com.jain.temple.jainmandirdarshan.activity.FindTempleActivity;
import com.jain.temple.jainmandirdarshan.activity.HomeActivity;
import com.jain.temple.jainmandirdarshan.activity.PoemListActivity;
import com.jain.temple.jainmandirdarshan.activity.SubMenuPujaActivity;
import com.jain.temple.jainmandirdarshan.activity.SunsetSunriseActvity;
import com.jain.temple.jainmandirdarshan.activity.TirthankarActivity;
import com.jain.temple.jainmandirdarshan.adaptor.DashboardGridAdaptor;
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel;
import com.jain.temple.jainmandirdarshan.model.CalendarMonthModel;
import com.jain.temple.jainmandirdarshan.model.DashboardModel;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 6/29/2017.
 */

public class DashBoardFragment extends Fragment {
    private TextView full_day_tv,current_date,current_month,day_tv,detail_tv;
    private RecyclerView dashboard_list_recy;
    private ArrayList<DashboardModel> dashboardModelList;
    private DashboardGridAdaptor dashboardGridAdaptor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View rootView=inflater.inflate(R.layout.fragment_dashboard,container,false);
        
        inItView(rootView);
        
        return rootView;
    }

    private void inItView(View rootView) {
        full_day_tv= (TextView) rootView.findViewById(R.id.full_day_tv);
        current_date= (TextView) rootView.findViewById(R.id.current_date);
        current_month= (TextView) rootView.findViewById(R.id.current_month);
        detail_tv= (TextView) rootView.findViewById(R.id.detail_tv);




        day_tv= (TextView) rootView.findViewById(R.id.day_tv);

        dashboard_list_recy= (RecyclerView) rootView.findViewById(R.id.dashboard_list_recy);
        dashboard_list_recy.setNestedScrollingEnabled(true);
        dashboard_list_recy.setHasFixedSize(true);
        dashboard_list_recy.setLayoutManager(new GridLayoutManager(getActivity(),2));

        rootView.findViewById(R.id.poem_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PoemListActivity.class));
            }
        });

        dashboard_list_recy.setFocusable(false);

        dashboardModelList=new ArrayList<>();

        dashboardModelList=  getData(getActivity());

        dashboardGridAdaptor=new DashboardGridAdaptor(getActivity(),dashboardModelList);

        dashboard_list_recy.setAdapter(dashboardGridAdaptor);

        UiUtils uiUtils=new UiUtils();

        Calendar calendar = Calendar.getInstance();

        full_day_tv.setText(uiUtils.getCurrentDayData(calendar));
        current_date.setText( uiUtils.getDayOfMonth(calendar)+"");
        current_month.setText(uiUtils.getMonth(calendar));
        day_tv.setText(uiUtils.getDayOfWeek(calendar)+"");

        dashboard_list_recy.setNestedScrollingEnabled(false);
        ArrayList<CalendarMonthModel> modelArrayList =new ArrayList<>();

        modelArrayList= getCurentDateTithi(uiUtils,calendar,getActivity(),modelArrayList);



        dashboard_list_recy.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(
                getActivity(), dashboard_list_recy, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fragment = null;
                String title = "";
               switch (position){
                   case 0:
                       startActivity(new Intent(getActivity(), FindTempleActivity.class));
                       break;
                   case 1:
                       startActivity(new Intent(getActivity(), FindDharamshalaActivity.class).putExtra("position",position));
                       break;
                   case 2:
                       startActivity(new Intent(getActivity(), BhavnaActivity.class).putExtra("position",position));
                       break;
                   case 3:
                       startActivity(new Intent(getActivity(), BhavnaActivity.class).putExtra("position",position));
                       break;
                   case 4:
                       startActivity(new Intent(getActivity(), SubMenuPujaActivity.class).putExtra("position",position));
                       break;
                   case 5:
                       startActivity(new Intent(getActivity(), BhavnaActivity.class).putExtra("position",position));
                       break;
                   case 6:
                       startActivity(new Intent(getActivity(), BhavnaActivity.class).putExtra("position",position));
                       break;
                   case 7:
                       startActivity(new Intent(getActivity(), CalendarActivity.class));
                       break;
                   case 8:
                       startActivity(new Intent(getActivity(), BhavnaActivity.class).putExtra("position",position));
                       break;
                   case 9:
                       startActivity(new Intent(getActivity(), BhavnaActivity.class).putExtra("position",position));
                       break;
                   case 10:
                       startActivity(new Intent(getActivity(), TirthankarActivity.class));
                       break;
                   case 11:
                       startActivity(new Intent(getActivity(), SunsetSunriseActvity.class));
                       break;
                   case 12:
                       startActivity(new Intent(getActivity(), AudioListActivity.class));
                       break;
               }

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack("null");
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((HomeActivity) getActivity()).setTitle(title);
                }

            }



            @Override
            public void onLongClick(View view, int position) {

            }
        }));





    }


    public  ArrayList<DashboardModel> getData(Activity activity) {
        int[] icon={
                R.drawable.temple_icons_cat,  R.drawable.temple_icons_cat,R.drawable.strot_bhakti_cat,R.drawable.chalisa_cat,
                R.drawable.pooja_cat,R.drawable.bhakti_cat,
                R.drawable.stuti_cat,R.drawable.tithi_darpan_cat,
                R.drawable.aarti_cat, R.drawable.bhajan_cat,R.drawable.teerathankar_cat,
                R.drawable.sunrise_home,R.drawable.ic_volume
        };

       String[] titles = activity.getResources().getStringArray(R.array.dashBoard_item);
        String[] color = activity.getResources().getStringArray(R.array.navDrawerItemsColor);

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            DashboardModel navItem = new DashboardModel();
            navItem.setName(titles[i]);
            navItem.setIcon(icon[i]);
            dashboardModelList.add(navItem);
        }
        return dashboardModelList;
    }


    public ArrayList<CalendarMonthModel> getCurentDateTithi(UiUtils uiUtils,Calendar calendar,Activity activity,ArrayList<CalendarMonthModel> monthModelArrayList) {
        // int week = calendar.get(Calendar.DAY_OF_WEEK);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        JSONObject jsonObject= uiUtils.getCalendarlistForTithi(activity);

        monthModelArrayList=  uiUtils.parseJsonforCalendar(jsonObject,monthModelArrayList, 0, 0);

        if (monthModelArrayList.size()>0) {
            CalendarMonthModel calendarMonthModel = monthModelArrayList.get(month);
            ArrayList<CalendarDayModel> calendarDayModelArrayList = calendarMonthModel.getDayModelArrayList();

            CalendarDayModel calendarDayModel = calendarDayModelArrayList.get(day - 1);
            String tithi = calendarDayModel.getTithi_e();


            if (tithi.equalsIgnoreCase("Ashtami") || tithi.equalsIgnoreCase("Chaturdashi")) {
                detail_tv.setText(Html.fromHtml( " <b>तिथि:</b> " + "<font size=55" + " color=red>" +calendarDayModel.getTithi_eh() +"</font>"+ " ,<b> पक्ष:</b> " + calendarDayModel.getPaksh_h() + "  ,<b> मास:</b> " +
                        calendarDayModel.getMahina_h() + "  ,<b>विक्रम सम्वत:</b> " + calendarDayModel.getVikram_samvat() + "  ,<b>वीर सम्वत:</b> " + calendarDayModel.getVeer_samvat()));

            } else {

                detail_tv.setText(Html.fromHtml(" <b>तिथि:</b> " + calendarDayModel.getTithi_eh() + " ,<b> पक्ष:</b> " + calendarDayModel.getPaksh_h() + "  ,<b> मास:</b> " +
                        calendarDayModel.getMahina_h() + "  ,<b>विक्रम सम्वत:</b> " + calendarDayModel.getVikram_samvat() + "  ,<b>वीर सम्वत:</b> " + calendarDayModel.getVeer_samvat()));

            }
            if (calendarDayModel.getSpecial().equalsIgnoreCase("Y")) {

                String msg = detail_tv.getText() + "<br>".toString();

                detail_tv.setText(Html.fromHtml(msg + "<font size=50" + " color=red>" + (" आज " + calendarDayModel.getDetail()) + " है|"));
            }
        }

        else{
            detail_tv.setText(Html.fromHtml("<font size=55" + " color=red>"  +getActivity().getString(R.string.calender_not_udpated)) );
        }

        return monthModelArrayList;
    }




}

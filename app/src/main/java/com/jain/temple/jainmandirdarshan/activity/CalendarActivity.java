package com.jain.temple.jainmandirdarshan.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jain.temple.jainmandirdarshan.Fragment.FragmentDrawer;
import com.jain.temple.jainmandirdarshan.R;
import com.jain.temple.jainmandirdarshan.adaptor.CalendarAdaptor;
import com.jain.temple.jainmandirdarshan.adaptor.DayDetailAdaptor;
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel;
import com.jain.temple.jainmandirdarshan.model.CalendarMonthModel;
import com.jain.temple.jainmandirdarshan.model.DayDetail;
import com.jain.temple.jainmandirdarshan.util.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 7/31/2017.
 */

public class CalendarActivity extends BaseActivity implements View.OnClickListener {
    private TextView hindiMonthStart, hindiMonthEnd;
    private TextView vikram_sarvat, veer_sarvat;
    private TextView monthName;
    private ImageView previousMonthBt, next_month_bt;
    private RecyclerView calendat_recy;
    private CalendarAdaptor calendarAdaptor;
    private int monthIndex = 0, DAY_OF_MONTH;
    private LinearLayout calendar_layout;
    private TextView calander_update_alert;

    private ArrayList<CalendarMonthModel> monthModelArrayList;
    private ArrayList<CalendarDayModel> dayModelArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.sets
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar()/*.setDisplayHomeAsUpEnabled(true)*/;
        actionBar.setDisplayHomeAsUpEnabled(true);

        hindiMonthStart = (TextView) findViewById(R.id.hindiMonthStart);
        hindiMonthEnd = (TextView) findViewById(R.id.hindiMonthEnd);
        vikram_sarvat = (TextView) findViewById(R.id.vikram_sarvat);
        veer_sarvat = (TextView) findViewById(R.id.veer_sarvat);
        monthName = (TextView) findViewById(R.id.monthName);
        calander_update_alert = (TextView) findViewById(R.id.calander_update_alert);

        next_month_bt = (ImageView) findViewById(R.id.next_month_bt);
        previousMonthBt = (ImageView) findViewById(R.id.previousMonthBt);

        calendar_layout = (LinearLayout) findViewById(R.id.calendar_layout);

        next_month_bt.setOnClickListener(this);
        previousMonthBt.setOnClickListener(this);


        calendat_recy = (RecyclerView) findViewById(R.id.calendat_recy);
        calendat_recy.setHasFixedSize(true);
        calendat_recy.setLayoutManager(new GridLayoutManager(this, 7));


        Calendar calendar = Calendar.getInstance();
        monthIndex = calendar.get(Calendar.MONTH);

        DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);

        monthModelArrayList = new ArrayList<>();


        UiUtils uiUtils = new UiUtils();
        JSONObject jsonObject = uiUtils.getCalendarlist(this);

        monthModelArrayList = uiUtils.parseJsonforCalendar(jsonObject, monthModelArrayList, monthIndex, DAY_OF_MONTH);
        if (monthModelArrayList.size() > 0) {
            calendar_layout.setVisibility(View.VISIBLE);
            calander_update_alert.setVisibility(View.GONE);
            CalendarMonthModel calendarMonthModel = monthModelArrayList.get(monthIndex);
            dayModelArrayList = calendarMonthModel.getDayModelArrayList();


            setMonthdata(calendarMonthModel);

            calendarAdaptor = new CalendarAdaptor(this, dayModelArrayList);
            calendat_recy.setAdapter(calendarAdaptor);

            calendarAdaptor.notifyDataSetChanged();
        } else {
            calendar_layout.setVisibility(View.GONE);
            calander_update_alert.setVisibility(View.VISIBLE);
        }


        calendat_recy.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(
                this, calendat_recy, new FragmentDrawer.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                CalendarDayModel calendarDayModel = dayModelArrayList.get(position);

                if (!calendarDayModel.getTithi_e().equals("")) {

                    showDatDetailonPopup(calendarDayModel);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void showDatDetailonPopup(CalendarDayModel calendarDayModel) {

        final Dialog dialog;
        DayDetailAdaptor dayDetailAdaptor;
        ArrayList<DayDetail> dayDetailArrayList;
        RecyclerView tithi_detail_recy;
        TextView today_date_tv, special_day;
        //  ImageView dialog_cross_iv;

        dialog = new Dialog(this);


        UiUtils uiUtils = new UiUtils();
        String todayDate = uiUtils.getCurrentDayData(Calendar.getInstance());


        dialog.setContentView(R.layout.tithi_detail_dialog);

        tithi_detail_recy = (RecyclerView) dialog.findViewById(R.id.tithi_detail_recy);
        today_date_tv = (TextView) dialog.findViewById(R.id.today_date_tv);
        special_day = (TextView) dialog.findViewById(R.id.special_day);
        String month="";

        switch (calendarDayModel.getMonth_d()) {
            case "1":
                month = "जनवरी";
                break;
            case "2":
                month = "फरवरी";
                break;
            case "3":
                month = "मार्च";
                break;
            case "4":
                month = "अप्रैल";
                break;
            case "5":
                month = "मई";
                break;
            case "6":
                month = "जून";
                break;
            case "7":
                month = "जुलाई";
                break;
            case "8":
                month = "अगस्त";
                break;
            case "9":
                month = "सितंबर";
                break;
            case "10":
                month = "अक्टूबर";
                break;
            case "11":
                month = "नवंबर";
                break;
            case "12":
                month = "दिसंबर";
                break;
        }


            tithi_detail_recy.setLayoutManager(new LinearLayoutManager(this));

        dayDetailArrayList = new ArrayList<>();

        dayDetailAdaptor = new DayDetailAdaptor(this, dayDetailArrayList);
        tithi_detail_recy.setAdapter(dayDetailAdaptor);

        today_date_tv.setText(calendarDayModel.getDay_d()+"-"+month+"-"+Calendar.getInstance().get(Calendar.YEAR));

        uiUtils.getDataForDayDetail(calendarDayModel, dayDetailArrayList);

        dayDetailAdaptor.notifyDataSetChanged();


        if (calendarDayModel.getSpecial().equalsIgnoreCase("Y")) {
            special_day.setVisibility(View.VISIBLE);

            special_day.setText(" आज " + calendarDayModel.getDetail() + " है ");
        } else {
            special_day.setVisibility(View.GONE);
        }


        dialog.show();
    }



    private void setMonthdata(CalendarMonthModel calendarMonthModel) {
        monthName.setText(calendarMonthModel.getMonthHindi());
        hindiMonthStart.setText(calendarMonthModel.getMonthNameHildiStart());
        hindiMonthEnd.setText(calendarMonthModel.getMonthNameHildiEnd());
        vikram_sarvat.setText("विक्रम: " + calendarMonthModel.getVikram_samvat());
        veer_sarvat.setText("वीर: " + calendarMonthModel.getVeer_samvat());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previousMonthBt:
                getPreviousMonthData();
                break;
            case R.id.next_month_bt:
                getNextMonthData();
                break;

        }

    }

    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public void getPreviousMonthData() {
        if (monthIndex > 0) {
            monthIndex--;
            CalendarMonthModel calendarMonthModel = monthModelArrayList.get(monthIndex);
            dayModelArrayList = calendarMonthModel.getDayModelArrayList();
            setMonthdata(calendarMonthModel);

            calendarAdaptor = new CalendarAdaptor(this, dayModelArrayList);
            calendat_recy.setAdapter(calendarAdaptor);
            calendarAdaptor.notifyDataSetChanged();
        }
    }

    public void getNextMonthData() {
        if (monthIndex < 11) {
            monthIndex++;
            CalendarMonthModel calendarMonthModel = monthModelArrayList.get(monthIndex);
            dayModelArrayList = calendarMonthModel.getDayModelArrayList();
            setMonthdata(calendarMonthModel);

            calendarAdaptor = new CalendarAdaptor(this, dayModelArrayList);
            calendat_recy.setAdapter(calendarAdaptor);
            calendarAdaptor.notifyDataSetChanged();
        }

    }

}

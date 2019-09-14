package com.jain.temple.jainmandirdarshan.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.jain.temple.jainmandirdarshan.R
import com.jain.temple.jainmandirdarshan.adaptor.NotificationAdaptor
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel
import com.jain.temple.jainmandirdarshan.model.CalendarMonthModel
import com.jain.temple.jainmandirdarshan.util.UiUtils
import kotlinx.android.synthetic.main.activity_notification.*
import java.util.*

class NotificationActivity : BaseActivity() {
    private var calendarDayModelArrayList: ArrayList<CalendarDayModel>? = null
    var notificationAdaptor: NotificationAdaptor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification)

        initView()

    }

    private fun initView() {
        setSupportActionBar(toolbar as Toolbar?)
        val actionBar = supportActionBar/*.setDisplayHomeAsUpEnabled(true)*/
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        calendarDayModelArrayList = ArrayList<CalendarDayModel>()
        notification_recy.setLayoutManager(LinearLayoutManager(this))
        notificationAdaptor = NotificationAdaptor(calendarDayModelArrayList)
        notification_recy.setAdapter(notificationAdaptor)


        getCalendarDayList()

    }

    private fun getCalendarDayList() {
        var dateToday: Boolean= false;

        val uiUtils = UiUtils()
        val jsonObject = uiUtils.getCalendarlist(this)

        var calendarArrayList: ArrayList<CalendarMonthModel>? = ArrayList<CalendarMonthModel>()

        val calendar = Calendar.getInstance()
        val monthIndex = calendar.get(Calendar.MONTH)

        val DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH)

        calendarArrayList = uiUtils.parseJsonforCalendar(jsonObject, calendarArrayList, monthIndex, DAY_OF_MONTH)

        for (calenderMonthModel in (calendarArrayList as ArrayList<CalendarMonthModel>?)!!) {
            if (!dateToday) {
            var dayList: ArrayList<CalendarDayModel> = calenderMonthModel.dayModelArrayList

            for (dayItem in dayList) {

                if (dayItem.special.equals("Y") || dayItem.tithi_eh.equals("अष्ठमी") || dayItem.tithi_eh.equals("चतुर्दशी")) {
                    var calendarDayModel: CalendarDayModel = CalendarDayModel()
                    var detail: String = dayItem.detail
                    if (dayItem.tithi_eh.equals("अष्ठमी") || dayItem.tithi_eh.equals("चतुर्दशी")) {
                        if (detail.length>0){
                            detail = detail + " , " + dayItem.tithi_eh
                        }
                        else{
                            detail =  dayItem.tithi_eh
                        }

                    }

                    calendarDayModel.detail = detail
                    calendarDayModel.day_d = dayItem.day_d + "-" + calenderMonthModel.monthHindi + "-" + 2019

                    calendarDayModelArrayList!!.add(calendarDayModel)


                }
                if (dayItem.isTodayDate) {
                    dateToday = true
                    break
                }
            }
        }
            else{
                break
            }
        }

        calendarDayModelArrayList!!.reverse()

        this.notificationAdaptor!!.notifyDataSetChanged()
    }

}
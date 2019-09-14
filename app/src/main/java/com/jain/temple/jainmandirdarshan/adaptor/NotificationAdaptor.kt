package com.jain.temple.jainmandirdarshan.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jain.temple.jainmandirdarshan.R
import com.jain.temple.jainmandirdarshan.model.CalendarDayModel
import kotlinx.android.synthetic.main.notification_cell.view.*
import java.util.ArrayList

class NotificationAdaptor(calendarDayModelArrayList: ArrayList<CalendarDayModel>?) : RecyclerView.Adapter<NotificationAdaptor.DataObjectViewHolder>() {

    var calendarDayModelArrayList: ArrayList<CalendarDayModel>?=calendarDayModelArrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.notification_cell, parent, false)
        return DataObjectViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: DataObjectViewHolder, position: Int) {
        holder?.bindItems(calendarDayModelArrayList?.get(position)!!)

    }



    override fun getItemCount(): Int {
      return this.calendarDayModelArrayList!!.size
    }



    class DataObjectViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bindItems(calendarDayModel: CalendarDayModel) = with(itemView) {

            date_tv.text = calendarDayModel.day_d
            notification_detail.text =  calendarDayModel.detail

        }
    }
}
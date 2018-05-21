package com.cad.ooqiadev.cadcheck_in

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import java.text.SimpleDateFormat


class LocationAdapter(val activityList: ArrayList<Activity>): RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    val byDates = activityList.groupBy { it.dueTime }

    override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {
        // Update date label
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val dateList = byDates.values.toMutableList()
        holder.date?.text = sdf.format(dateList[position][0].dueTime)
        // Create vertical Layout Manager
        holder.rv?.layoutManager = LinearLayoutManager(holder.rv.context, LinearLayout.VERTICAL, false)
        // Access RecyclerView Adapter and load the data
        var adapter = LocationActivitiesAdapter(dateList[position] as ArrayList<Activity>)
        holder.rv?.adapter = adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.location_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return byDates.count()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date = itemView.findViewById<TextView>(R.id.activityDate)
        val rv = itemView.findViewById<RecyclerView>(R.id.activitiesTasksList)
    }

}
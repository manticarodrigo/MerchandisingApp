package com.cad.ooqiadev.cadcheck_in

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent

class LocationActivitiesAdapter(val activityList: ArrayList<Activity>): RecyclerView.Adapter<LocationActivitiesAdapter.ViewHolder>() {

    companion object {
        val ACTIVITY_ID = "activityId"
        val ACTIVITY_TITLE = "activityTitle"
    }

    override fun onBindViewHolder(holder: LocationActivitiesAdapter.ViewHolder, position: Int) {
        val activity = activityList[position]
        holder.activityTitle?.text = activity.title
        holder.activityOrder?.text = "PO" + activity.orderId.toString()
        holder.activityBadge?.text = activity.pendingTasks.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, TasksActivity::class.java)
            intent.putExtra(LocationActivitiesAdapter.ACTIVITY_ID, activity.id)
            intent.putExtra(LocationActivitiesAdapter.ACTIVITY_TITLE, activity.title)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationActivitiesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.location_task_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val activityTitle = itemView.findViewById<TextView>(R.id.activityTitle)
        val activityOrder = itemView.findViewById<TextView>(R.id.activityOrder)
        val activityBadge = itemView.findViewById<TextView>(R.id.activityBadge)
    }

}
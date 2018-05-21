package com.cad.ooqiadev.cadcheck_in

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent

class LocationActivitiesAdapter(val activityList: ArrayList<Activity>): RecyclerView.Adapter<LocationActivitiesAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: LocationActivitiesAdapter.ViewHolder, position: Int) {
        val activity = activityList[position]
        holder.activityTitle?.text = activity.title
        holder.activityOrder?.text = "PO" + activity.orderId.toString()
        holder.activityBadge?.text = activity.pendingTasks.toString()
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
        companion object {
            val ACTIVITY_NAME = "activityName"
            val ACTIVITY_PENDING_TASKS = "activityPendingTasks"
        }
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, TasksActivity::class.java)
                intent.putExtra(ACTIVITY_NAME, activityTitle.text.toString())
                intent.putExtra(ACTIVITY_PENDING_TASKS, activityBadge.text.toString())
                itemView.context.startActivity(intent)
            }
        }
    }

}
package com.cad.ooqiadev.cadcheck_in

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent

class LocationAdapter(val activityList: ArrayList<Activity>): RecyclerView.Adapter<LocationAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {
        val activity = activityList[position]
        holder?.activityName?.text = activity.title
        holder?.activityBadge?.text = activity.pendingTasks.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val activityName = itemView.findViewById<TextView>(R.id.activityName)
        val activityBadge = itemView.findViewById<TextView>(R.id.activityBadge)
//        companion object {
//            val ACTIVITY_NAME = "activityName"
//            val ACTIVITY_PENDING_TASKS = "activityPendingTasks"
//        }
//        init {
//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, TasksActivity::class.java)
//                intent.putExtra(ACTIVITY_NAME, activityName.text.toString())
//                intent.putExtra(ACTIVITY_PENDING_TASKS, activityPendingTasks.text.toString())
//                itemView.context.startActivity(intent)
//            }
//        }
    }

}
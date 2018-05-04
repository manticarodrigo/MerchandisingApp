package com.cad.ooqiadev.cadcheck_in

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent

class MainAdapter(val locationList: ArrayList<Location>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        val location = locationList[position]
        holder?.locName?.text = location.name
        holder?.locDirection?.text = location.direction
        holder?.locPendingActivities?.text = location.pendingActivities.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val locName = itemView.findViewById<TextView>(R.id.locName)
        val locDirection = itemView.findViewById<TextView>(R.id.locDirection)
        val locPendingActivities = itemView.findViewById<TextView>(R.id.locBadge)
        companion object {
            val LOC_NAME = "locName"
            val LOC_DIRECTION = "locDirection"
            val LOC_PENDING_ACTIVITIES = "locPendingActivities"
        }
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, LocationActivity::class.java)
                intent.putExtra(LOC_NAME, locName.text.toString())
                intent.putExtra(LOC_DIRECTION, locDirection.text.toString())
                intent.putExtra(LOC_PENDING_ACTIVITIES, locPendingActivities.text.toString())
                itemView.context.startActivity(intent)
            }
        }
    }

}
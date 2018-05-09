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
        holder.locationName?.text = location.name
        holder.locationDirection?.text = location.direction
        holder.locationBadge?.text = location.pendingActivities.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val locationName = itemView.findViewById<TextView>(R.id.locationName)
        val locationDirection = itemView.findViewById<TextView>(R.id.locationDirection)
        val locationBadge = itemView.findViewById<TextView>(R.id.locationBadge)
        companion object {
            val LOCATION_NAME = "locationName"
            val LOCATION_DIRECTION = "locationDirection"
            val LOCATION_PENDING_ACTIVITIES = "locationPendingActivities"
        }
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, LocationActivity::class.java)
                intent.putExtra(LOCATION_NAME, locationName.text.toString())
                intent.putExtra(LOCATION_DIRECTION, locationDirection.text.toString())
                intent.putExtra(LOCATION_PENDING_ACTIVITIES, locationBadge.text.toString().toInt())
                itemView.context.startActivity(intent)
            }
        }
    }

}
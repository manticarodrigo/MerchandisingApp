package com.cad.ooqiadev.cadcheck_in

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.w3c.dom.Text

class LocationAdapter(val locationList: ArrayList<Location>): RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {
        holder?.locName?.text = locationList[position].name
        holder?.locDirection?.text = locationList[position].direction
        holder?.locPendingActivities?.text = locationList[position].pendingActivities.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.location_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val locName = itemView.findViewById<TextView>(R.id.locName)
        val locDirection = itemView.findViewById<TextView>(R.id.locDirection)
        val locPendingActivities = itemView.findViewById<TextView>(R.id.locBadge)

    }

}
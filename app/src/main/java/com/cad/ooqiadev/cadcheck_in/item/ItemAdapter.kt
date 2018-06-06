package com.cad.ooqiadev.cadcheck_in.item

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cad.ooqiadev.cadcheck_in.R
import com.cad.ooqiadev.cadcheck_in.models.Item

class ItemAdapter(val itemList: ArrayList<Item>, context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemLabel?.text = item.description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemLabel = itemView.findViewById<TextView>(R.id.itemTextLabel)
    }

}
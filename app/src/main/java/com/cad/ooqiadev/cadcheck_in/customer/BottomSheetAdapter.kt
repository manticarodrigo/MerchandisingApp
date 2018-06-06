package com.cad.ooqiadev.cadcheck_in.customer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cad.ooqiadev.cadcheck_in.R
import android.widget.*
import com.cad.ooqiadev.cadcheck_in.models.Task

class BottomSheetAdapter(val taskList: ArrayList<Task>, context: Context): RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {

    private val customerActivity = (context as CustomerActivity)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get taskCatalog and bind to view
        val task = taskList[position]
        holder.numberBadge?.text = (position + 1).toString()
        holder.descriptionLabel?.text = task.description
        // Add click listener for task item
        holder.itemView.setOnClickListener {
            customerActivity.bottomSheet?.dismiss()
            customerActivity.startTaskActivity(task)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_bottomsheet_task, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val descriptionLabel = itemView.findViewById<TextView>(R.id.descriptionLabel)
        val numberBadge = itemView.findViewById<TextView>(R.id.plusBadge)
    }

}

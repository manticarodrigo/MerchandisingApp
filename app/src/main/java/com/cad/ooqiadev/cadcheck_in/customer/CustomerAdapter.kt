package com.cad.ooqiadev.cadcheck_in.customer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cad.ooqiadev.cadcheck_in.R
import android.widget.*
import com.cad.ooqiadev.cadcheck_in.item.ItemActivity
import com.cad.ooqiadev.cadcheck_in.main.MainAdapter
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import com.cad.ooqiadev.cadcheck_in.task.TaskActivity

class CustomerAdapter(val taskCatalogList: ArrayList<TaskCatalog>, context: Context): RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    private val customerActivity = (context as CustomerActivity)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get taskCatalog and bind to view
        val taskCatalog = taskCatalogList[position]
        holder.descriptionLabel?.text = taskCatalog.description
        val pendingCount: Int = taskCatalog.textLabels?.size!! + taskCatalog.photoLabels?.size!! + taskCatalog.checkboxLabels?.size!!
        holder.pendingLabel?.text =  pendingCount.toString() + " sub-tareas."
        // Assign proper status symbols and bg color to badge
        val task = customerActivity.tasks?.find { task -> task.taskCatalogId == taskCatalog.id }
        if (task == null) {
            holder.statusBadge?.text = "..."
            holder.statusBadge?.getBackground()?.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_ATOP)
        } else if (customerActivity.isFulfilled(task)) {
            holder.statusBadge?.text = "✓"
            holder.statusBadge?.getBackground()?.setColorFilter(Color.parseColor("#6FCF97"), PorterDuff.Mode.SRC_ATOP)
        } else {
            holder.statusBadge?.text = "!"
            holder.statusBadge?.getBackground()?.setColorFilter(Color.parseColor("#F2994A"), PorterDuff.Mode.SRC_ATOP)
        }
        // Add click listener for task item
        holder.itemView.setOnClickListener {
            if(taskCatalog.isInventory == true) {
                val intent = Intent(holder.itemView.context, ItemActivity::class.java)
                holder.itemView.context.startActivity(intent)
            } else {
                val intent = Intent(holder.itemView.context, TaskActivity::class.java)
                intent.putExtra(CustomerActivity.TASK_CATALOG_ID, taskCatalog.id)
                intent.putExtra(CustomerActivity.TASK_CATALOG_DESCRIPTION, taskCatalog.description)
                intent.putExtra(MainAdapter.CUSTOMER_ID, customerActivity.customerId)
                holder.itemView.context.startActivity(intent)
            }
            customerActivity.startTaskActivity(taskCatalog, task, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.customer_task_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskCatalogList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val descriptionLabel = itemView.findViewById<TextView>(R.id.descriptionLabel)
        val pendingLabel = itemView.findViewById<TextView>(R.id.pendingLabel)
        val statusBadge = itemView.findViewById<TextView>(R.id.statusBadge)
    }

}

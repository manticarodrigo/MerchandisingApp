package com.cad.ooqiadev.cadcheck_in

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff

class TasksAdapter(val taskList: ArrayList<Task>): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: TasksAdapter.ViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName?.text = task.description
        holder.taskStatus?.text = task.status.toString()

        if (Status.valueOf(task.status as String) == Status.PENDING) {
            holder.taskBadge?.text = "..."
            holder.taskBadge?.getBackground()?.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_ATOP)
        } else if (Status.valueOf(task.status as String) == Status.PARTIAL) {
            holder.taskBadge?.text = "!"
            holder.taskBadge?.getBackground()?.setColorFilter(Color.parseColor("#F2994A"), PorterDuff.Mode.SRC_ATOP)
        } else if (Status.valueOf(task.status as String) == Status.FAILED) {
            holder.taskBadge?.text = "x"
            holder.taskBadge?.getBackground()?.setColorFilter(Color.parseColor("#EB5757"), PorterDuff.Mode.SRC_ATOP)
        } else {
            holder.taskBadge?.text = "✓"
            holder.taskBadge?.getBackground()?.setColorFilter(Color.parseColor("#6FCF97"), PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val taskName = itemView.findViewById<TextView>(R.id.taskName)
        val taskStatus = itemView.findViewById<TextView>(R.id.taskStatus)
        val taskBadge = itemView.findViewById<TextView>(R.id.taskBadge)
        companion object {
            val TASK_NAME = "taskName"
            val TASK_STATUS = "taskStatus"
        }
        init {
            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, TaskModalActivity::class.java)
//                intent.putExtra(TASK_NAME, taskName.text.toString())
//                intent.putExtra(TASK_STATUS, taskStatus.text.toString())
//                itemView.context.startActivity(intent)
            }
        }
    }

}
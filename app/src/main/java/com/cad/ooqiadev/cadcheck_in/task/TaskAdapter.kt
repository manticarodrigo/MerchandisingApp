package com.cad.ooqiadev.cadcheck_in.task

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cad.ooqiadev.cadcheck_in.R
import android.widget.*
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog

class TaskAdapter(val taskCatalog: TaskCatalog, context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val taskActivity = (context as TaskActivity)

    companion object {
        val TEXT_ROW_TYPE = 0
        val PHOTO_ROW_TYPE = 1
        val CHECKBOX_ROW_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < taskCatalog.textLabels?.size!! ) {
            TaskAdapter.TEXT_ROW_TYPE
        } else if (position < taskCatalog.photoLabels?.size!! + taskCatalog.textLabels?.size!!) {
            TaskAdapter.PHOTO_ROW_TYPE
        } else if (position < taskCatalog.checkboxLabels?.size!! + taskCatalog.textLabels?.size!! + taskCatalog.photoLabels?.size!!) {
            TaskAdapter.CHECKBOX_ROW_TYPE
        } else {
            -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TextViewHolder) {
            holder.textLabel.text = taskCatalog.textLabels!![position]
            holder.textInput.setText(taskActivity.currentTask?.textValues!![position])
            holder.textInput.onChange {
                taskActivity.currentTask?.textValues!![position] = it
            }
        } else if (holder is PhotoViewHolder) {
            val relPos = position - taskCatalog.textLabels?.size!!
            holder.photoLabel.text = taskCatalog.photoLabels!![relPos]
            holder.photoImageView.setImageBitmap(taskActivity.fetchImage(taskActivity.currentTask?.photoUrls!![relPos]))
            holder.photoImageView.setOnClickListener { v ->
                taskActivity.openCamera(relPos, holder.photoImageView)
            }
        } else if (holder is CheckboxViewHolder) {
            val relPos = position - taskCatalog.textLabels?.size!! - taskCatalog.photoLabels?.size!!
            holder.checkBox.text = taskCatalog.checkboxLabels!![relPos]
            holder.checkBox.isChecked = taskActivity.currentTask?.checkboxValues!![relPos]!!.toBoolean()
            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                taskActivity.currentTask?.checkboxValues!![relPos] = isChecked.toString()
            }
        }
    }

    fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        if (viewType === TaskAdapter.TEXT_ROW_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.task_text_list_item, parent, false)
            return TextViewHolder(view)
        } else if (viewType === TaskAdapter.PHOTO_ROW_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.task_photo_list_item, parent, false)
            return PhotoViewHolder(view)
        } else if (viewType === TaskAdapter.CHECKBOX_ROW_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.task_checkbox_list_item, parent, false)
            return CheckboxViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.task_text_list_item, parent, false)
            return TextViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return taskCatalog.textLabels?.size!! + taskCatalog.photoLabels?.size!! + taskCatalog.checkboxLabels?.size!!
    }

    class TextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textLabel = itemView.findViewById<TextView>(R.id.textLabel)
        val textInput = itemView.findViewById<EditText>(R.id.textInput)
    }

    class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val photoLabel = itemView.findViewById<TextView>(R.id.photoLabel)
        val photoImageView = itemView.findViewById<ImageView>(R.id.photoImageView)
    }

    class CheckboxViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
    }

}

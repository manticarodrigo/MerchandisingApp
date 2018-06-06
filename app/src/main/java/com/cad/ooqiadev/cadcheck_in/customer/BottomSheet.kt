package com.cad.ooqiadev.cadcheck_in.customer

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Button
import com.cad.ooqiadev.cadcheck_in.R
import com.cad.ooqiadev.cadcheck_in.models.Task
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog


class BottomSheet(private val taskCatalog: TaskCatalog,
                  private val taskList: ArrayList<Task>,
                  private val context: Context) {

    private val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(context)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_task, null)
        bottomSheetDialog.setContentView(view)

        with(view) {
            val rv = findViewById<RecyclerView>(R.id.bottomSheetTaskList)
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv.adapter = BottomSheetAdapter(taskList, context)

            val newTaskButton = findViewById<Button>(R.id.newTaskButton)
            newTaskButton.setOnClickListener {
                dismiss()
                (context as CustomerActivity).startNewTaskActivity(taskCatalog)
            }
        }
    }

    fun show() {
        bottomSheetDialog.show()
    }

    fun dismiss() {
        bottomSheetDialog.dismiss()
    }
}
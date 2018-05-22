package com.cad.ooqiadev.cadcheck_in

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_tasks.*

class TasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        // Set toolbar title to location name
        val activity = intent.getStringExtra(LocationActivitiesAdapter.ViewHolder.ACTIVITY_NAME)
        supportActionBar?.title = activity

        // Set activity title
        activityTitle.text = activity

        // Initialize test tasks
        val tasks: ArrayList<Task> = ArrayList()

        // Load locations into ArrayList
        tasks.add(Task(null, 0, "Quitar productos vencidos", Status.DONE.toString(), null, null))
        tasks.add(Task(null, 0, "Colocar productos nuevos", Status.PARTIAL.toString(), null, null))
        tasks.add(Task(null, 0, "Hacer inventario", Status.PENDING.toString(), null, null))

        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.tasksList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        var adapter = TasksAdapter(tasks)
        rv.adapter = adapter

    }
}
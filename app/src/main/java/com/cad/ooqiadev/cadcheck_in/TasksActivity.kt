package com.cad.ooqiadev.cadcheck_in

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_tasks.*

class TasksActivity : AppCompatActivity() {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Start worker thread and access db singleton
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = AppDatabase.getInstance(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        // Set toolbar title to location name
        val activityTitleString = intent.getStringExtra(LocationActivitiesAdapter.ACTIVITY_TITLE)
        supportActionBar?.title = activityTitleString

        // Set activity title
        activityTitle.text = activityTitleString

        val activityId = intent.getLongExtra(LocationActivitiesAdapter.ACTIVITY_ID, 0)
        fetchTasks(activityId)

    }

    private fun bindDataWithUi(taskData: ArrayList<Task>) {
        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.tasksList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        var adapter = TasksAdapter(taskData)
        rv.adapter = adapter
    }

    private fun fetchTasks(activityId: Long) {
        val task = Runnable {
            val taskData = mDb?.taskDao()?.getActivityTasks(activityId)
            mUiHandler.post({
                if (taskData == null || taskData.isEmpty()) {
                    println("No tasks for activities in db...")
                } else {
                    println(taskData)
                    bindDataWithUi(taskData as ArrayList<Task>)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }
}
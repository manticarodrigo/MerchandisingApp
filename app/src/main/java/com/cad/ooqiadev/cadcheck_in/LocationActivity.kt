package com.cad.ooqiadev.cadcheck_in

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import java.util.Date
import java.sql.Timestamp



class LocationActivity : AppCompatActivity() {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private var activities: ArrayList<Activity> = ArrayList()
    private var tasks: ArrayList<Task> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Start worker thread and access db singleton
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = AppDatabase.getInstance(this)

        // Init activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Set toolbar title to location name
        val locationName = intent.getStringExtra(MainAdapter.LOCATION_NAME)
        supportActionBar?.title = locationName

        val locationId = intent.getLongExtra(MainAdapter.LOCATION_ID, 0)
        fetchActivities(locationId)
    }

    private fun bindDataWithUi(activityData: ArrayList<Activity>) {
        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.activitiesDatesList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        var adapter = LocationAdapter(activityData)
        rv.adapter = adapter
    }

    private fun fetchActivities(locationId: Long) {
        val activity = Runnable {
            val activityData = mDb?.activityDao()?.getLocationActivities(locationId)
            mUiHandler.post({
                if (activityData == null || activityData.isEmpty()) {
                    println("No activities for location in db...")
                } else {
                    // println(activityData)
                    fetchTasks(activityData)
                }
            })
        }
        mDbWorkerThread.postTask(activity)
    }

    private fun fetchTasks(activities: List<Activity>) {
        val task = Runnable {
            val taskData = activities.map { mDb?.taskDao()?.getActivityTasks(it.id) }
            activities.forEachIndexed { i, activity ->
                val taskArr = taskData[i]
                activity?.pendingTasks = taskArr?.size
                this.activities.add(activity)
            }
            mUiHandler.post({
                if (taskData == null || taskData.isEmpty()) {
                    println("No tasks for activities in db...")
                } else {
                    println(taskData)
                    bindDataWithUi(this.activities)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }
}
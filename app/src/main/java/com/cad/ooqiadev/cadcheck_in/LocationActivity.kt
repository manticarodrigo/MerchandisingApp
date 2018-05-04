package com.cad.ooqiadev.cadcheck_in

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout

class LocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        // Set toolbar title to location name
        val locationName = intent.getStringExtra(MainAdapter.ViewHolder.LOCATION_NAME)
        supportActionBar?.title = locationName

        // Initialize test activities
        val activities: ArrayList<Activity> = ArrayList()

        // Load activities into ArrayList
        activities.add(Activity("Rellenar producto A - PO385", 1))
        activities.add(Activity("Organizar producto B - PO567", 10))
        activities.add(Activity("Revisar producto C - PO890", 25))

        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.activitiesList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        var adapter = LocationAdapter(activities)
        rv.adapter = adapter
    }
}
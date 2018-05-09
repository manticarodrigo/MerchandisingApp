package com.cad.ooqiadev.cadcheck_in

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {

        // Start worker thread and access db singleton
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = AppDatabase.getInstance(this)

        // Init activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Set toolbar title
        supportActionBar?.title = "Pendientes"

        // Add drawer listener
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Handle data
        insertMockData()
        fetchActivities()

    }

    override fun onDestroy() {
        // Destroy db singleton instance
        AppDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }

    private fun bindDataWithUi(locationData: ArrayList<Location>) {
        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.locationsList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        var adapter = MainAdapter(locationData)
        rv.adapter = adapter
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                // Handle the dashboard menu button action
                println("dashboard pressed")
            }
            R.id.nav_settings -> {
                println("settings pressed")
            }
            R.id.nav_sync -> {
                println("sync pressed")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun fetchActivities() {
        val activity = Runnable {
            val activityData = mDb?.activityDao()?.getUserActivities(0) // TODO: Fetch user id from cache
            mUiHandler.post({
                if (activityData == null || activityData.isEmpty()) {
                    println("No activities for user in db...")
                } else {
                    println(activityData)
                    fetchLocations(activityData)
                }
            })
        }
        mDbWorkerThread.postTask(activity)
    }

    private fun fetchLocations(activities: List<Activity>) {
        val location = Runnable {
            val locationData = activities.map { mDb?.locationDao()?.getLocationForActivity(it.locationId) }
            val locationMap = locationData.groupBy { it?.id }
            val locationArrayList: ArrayList<Location> = ArrayList()
            locationMap.forEach {
                val loc = it.value[0]
                loc?.pendingActivities = it.value.size
                locationArrayList.add(loc as Location)
            }

            mUiHandler.post({
                if (locationData == null || locationData.isEmpty()) {
                    println("No locations for activities in db...")
                } else {
                    // println(locationData)
                    bindDataWithUi(locationArrayList)
                }
            })
        }
        mDbWorkerThread.postTask(location)
    }

    private fun insertLocationDataInDb(data: Location) {
        val location = Runnable { mDb?.locationDao()?.insert(data) }
        mDbWorkerThread.postTask(location)
    }

    private fun insertActivityDataInDb(data: Activity) {
        val activity = Runnable { mDb?.activityDao()?.insert(data) }
        mDbWorkerThread.postTask(activity)
    }

    private fun insertUserDataInDb(data: User) {
        val user = Runnable { mDb?.userDao()?.insert(data) }
        mDbWorkerThread.postTask(user)
    }

    private fun insertMockData() {

        // Initialize and insert test user
        val user = User(0, "John Doe")
        insertUserDataInDb(user)

        // Initialize test locations
        val locations: ArrayList<Location> = ArrayList()

        // Load locations into ArrayList
        locations.add(Location(1, "Walmart", "7250 Carson Blvd, Long Beach CA 90808, USA"))
        locations.add(Location(2, "CVS", "7250 Carson Blvd, Long Beach CA 90808, USA"))
        locations.add(Location(3, "Whole Foods", "7250 Carson Blvd, Long Beach CA 90808, USA"))

        // Insert test locations in db
        for (location in locations) {
            insertLocationDataInDb(location)
        }

        // Initialize test activities
        val activities: ArrayList<Activity> = ArrayList()
        val time = System.currentTimeMillis()

        // Load activities into ArrayList
        activities.add(Activity(1, 0, 1, 385, "Rellenar producto A", time, null, null))
        activities.add(Activity(2, 0, 1, 385, "Organizar producto B", time, null, null))
        activities.add(Activity(3, 0, 2, 567, "Organizar producto B", time, null, null))
        activities.add(Activity(4, 0, 3, 890, "Revisar producto C", time, null, null))
        // Insert test activities in db
        for (activity in activities) {
            insertActivityDataInDb(activity)
        }
    }
}

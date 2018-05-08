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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mDb: AppDatabase? = null

    private lateinit var mDbWorkerThread: DbWorkerThread

    private val mUiHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Set toolbar title
        supportActionBar?.title = "Pendientes"

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Start db singleton thread
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = AppDatabase.getInstance(this)

        // insertLocationDataInDb(Location(0, "Walmart", "7250 Carson Blvd, Long Beach CA 90808, USA"))
        // fetchLocationsFromDb()

        // Initialize test locations
        val locations: ArrayList<Location> = ArrayList()

        // Load locations into ArrayList
        locations.add(Location(0, "Walmart", "7250 Carson Blvd, Long Beach CA 90808, USA"))
        locations.add(Location(0, "CVS", "7250 Carson Blvd, Long Beach CA 90808, USA"))
        locations.add(Location(0, "Whole Foods", "7250 Carson Blvd, Long Beach CA 90808, USA"))


        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.locationsList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        var adapter = MainAdapter(locations)
        rv.adapter = adapter

    }

    override fun onDestroy() {
        AppDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }

    private fun fetchActivitiesFromDb() {
        val activity = Runnable {
            val activityData =
                    mDb?.activityDao()?.getAll()
            mUiHandler.post({
                if (activityData == null || activityData.size == 0) {
                    Toast.makeText(this, "No hay actividades en la base de datos..!!", Toast.LENGTH_SHORT)
                } else {
                    println(activityData.get(0))
                    // bindDataWithUi(activityData = activityData?.get(0))
                }
            })
        }
        mDbWorkerThread.postActivity(activity)
    }

    private fun fetchLocationsFromDb() {
        val location = Runnable {
            val locationData =
                    mDb?.locationDao()?.getAll()
            mUiHandler.post({
                println("locations in db:")
                if (locationData == null || locationData.size == 0) {
                    Toast.makeText(this, "No hay ubicaciones en la base de datos..!!", Toast.LENGTH_SHORT)
                } else {
                    println(locationData.get(0))
                    // bindDataWithUi(locationData = locationData?.get(0))
                }
            })
        }
        mDbWorkerThread.postLocation(location)
    }

    private fun insertLocationDataInDb(data: Location) {
        val location = Runnable { mDb?.locationDao()?.insert(data) }
        mDbWorkerThread.postActivity(location)
    }

    private fun insertActivityDataInDb(data: Activity) {
        val activity = Runnable { mDb?.activityDao()?.insert(data) }
        mDbWorkerThread.postActivity(activity)
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
}

package com.cad.ooqiadev.cadcheck_in

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.view.MenuItem
import android.widget.Toast
import com.cad.ooqiadev.cadcheck_in.parsers.Location as LocationParse
import com.cad.ooqiadev.cadcheck_in.parsers.Activity as ActivityParse
import com.cad.ooqiadev.cadcheck_in.parsers.Task as TaskParse
import com.cad.ooqiadev.cadcheck_in.settings.SettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mDb: AppDatabase? = null
    private var ftpClient: FTP? = null
    private var CSVFile: CSV? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private val mCSViHandler = Handler()
    private var adapter: MainAdapter? = null
    private var locations: ArrayList<Location> = ArrayList()
    private var activities: ArrayList<Activity> = ArrayList()
    private var tasks: ArrayList<Task> = ArrayList()
    private var fileNames = listOf("LOCATIONS", "ACTIVITIES", "TASKS")

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

        //deleteActivityDataInDb()
        //deleteLocationDataInDb()
        // Handle data
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
        this.adapter = MainAdapter(locationData)
        rv.adapter = adapter
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_sync -> {
                println("sync pressed")
                fileNames.forEach {
                    syncFileData(it);
                }
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
                    Toast.makeText(applicationContext, "No activities for user in db. Please, synchronize", Toast.LENGTH_SHORT).show()
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
            locationMap.forEach {
                val loc = it.value[0]
                loc?.pendingActivities = it.value.size
                locations.add(loc as Location)
            }

            mUiHandler.post({
                if (locationData == null || locationData.isEmpty()) {
                    println("No locations for activities in db...")
                } else {
                    // println(locationData)
                    bindDataWithUi(locations)
                }
            })
        }
        mDbWorkerThread.postTask(location)
    }

    private fun deleteLocationDataInDb() {
        val location = Runnable { mDb?.locationDao()?.deleteAll() }
        mDbWorkerThread.postTask(location)
    }

    private fun deleteActivityDataInDb() {
        val activity = Runnable { mDb?.activityDao()?.deleteAll() }
        mDbWorkerThread.postTask(activity)
    }

    private fun insertLocationDataInDb(data: Location) {
        val location = Runnable { mDb?.locationDao()?.insert(data) }
        mDbWorkerThread.postTask(location)
    }

    private fun insertActivityDataInDb(data: Activity) {
        val activity = Runnable {
            mDb?.activityDao()?.insert(data)
            Toast.makeText(applicationContext, "Sync done!", Toast.LENGTH_SHORT).show()
        }
        mDbWorkerThread.postTask(activity)
    }

    private fun insertTaskDataInDb(data: Task) {
        val task = Runnable { mDb?.taskDao()?.insert(data) }
        mDbWorkerThread.postTask(task)
    }

    private fun insertUserDataInDb(data: User) {
        val user = Runnable { mDb?.userDao()?.insert(data) }
        mDbWorkerThread.postTask(user)
    }

    private fun syncFileData(fileName: String) {
        var localPathFile: String

        val dataFile = Runnable {
            this.ftpClient = FTP()
            var lp = LocationParse()
            var ap = ActivityParse()
            var tp = TaskParse()
            var res : Result

            localPathFile = applicationContext.filesDir.path + "/" + fileName + ".csv"
            res = ftpClient?.DownloadFile("/MERC/" + fileName + ".csv", localPathFile)!!

            mCSViHandler.post({

                if(res.success) {

                    CSVFile = CSV()
                    res = CSVFile?.readFile(localPathFile)!!

                    when(fileName) {
                        "LOCATIONS" -> {
                            locations = lp.parse(res.rows!!)
                            // Initialize and insert test user
                            val user = User(0, "John Doe") // TODO: Remove once user cache is working
                            insertUserDataInDb(user)

                            // Insert locations in db
                            for (location in locations) {
                                insertLocationDataInDb(location)
                            }
                        }

                        "ACTIVITIES" -> {
                            activities = ap.parse(res.rows!!)
                            // Insert activities in db
                            for (activity in activities) {
                                insertActivityDataInDb(activity)
                            }
                            locations  = ArrayList()
                            fetchLocations(activities)
                        }
                        "TASKS" -> {
                            // Parse CSV to Object
                            tasks = tp.parse(res.rows!!)
                            // Insert tasks in db
                            for (task in tasks) {
                                insertTaskDataInDb(task)
                            }
                        }
                    }

                } else {
                    Toast.makeText(applicationContext, res.message, Toast.LENGTH_SHORT).show()
                }

            })

        }
        mDbWorkerThread.postTask(dataFile)
    }
}

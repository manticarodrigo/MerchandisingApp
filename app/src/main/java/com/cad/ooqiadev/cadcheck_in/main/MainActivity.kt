package com.cad.ooqiadev.cadcheck_in.main

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
import com.cad.ooqiadev.cadcheck_in.*
import com.cad.ooqiadev.cadcheck_in.models.*
import com.cad.ooqiadev.cadcheck_in.parsers.Task as TaskParse
import com.cad.ooqiadev.cadcheck_in.parsers.Customer as CustomerParse
import com.cad.ooqiadev.cadcheck_in.parsers.TaskCatalog as TaskCatalogParse
import com.cad.ooqiadev.cadcheck_in.parsers.Item as ItemParse
import com.cad.ooqiadev.cadcheck_in.settings.SettingsActivity
import com.cad.ooqiadev.cadcheck_in.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.File
import android.R.attr.path
import com.cad.ooqiadev.cadcheck_in.task.TaskActivity


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private var ftpClient: FTP? = null
    private var CSVFile: CSV? = null
    private val mUiHandler = Handler()
    private var adapter: MainAdapter? = null
    private var customers: ArrayList<Customer> = ArrayList()
    private var taskCatalogs: ArrayList<TaskCatalog> = ArrayList()
    private var tasks: ArrayList<Task> = ArrayList()
    private var items: ArrayList<Item> = ArrayList()
    private var fileNames = listOf("CUSTOMERS", "TASKS/CATALOG", "ITEMS")

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
        supportActionBar?.title = "Clientes"

        // Add drawer listener
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Handle data
        fetchCustomers()

    }

    override fun onDestroy() {
        // Destroy db singleton instance
        AppDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }

    private fun bindDataWithUi(customerData: ArrayList<Customer>) {
        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.customerList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        this.adapter = MainAdapter(customerData)
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
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_sync -> {
                println("sync pressed")
                // syncFileData
                uploadData(fileNames)
                fileNames.forEach {
                    downloadData(it)
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun fetchCustomers() {

        val u = Helpers()

        val customerType = u.getPreferenceValue("customerType", baseContext)

        val customer = Runnable {
            val customerData = mDb?.customerDao()?.getCustomersByType(customerType) as ArrayList<Customer>
            mUiHandler.post({
                if (customerData == null || customerData.isEmpty()) {
                    Toast.makeText(applicationContext, "No se encontraron clientes. Favor syncronizar.", Toast.LENGTH_SHORT).show()
                } else {
                    bindDataWithUi(customerData)
                }
            })
        }
        mDbWorkerThread.postTask(customer)
    }

    private fun insertTaskCatalogDataInDb(data: TaskCatalog) {
        val taskCatalog = Runnable { mDb?.taskCatalogDao()?.insert(data) }
        mDbWorkerThread.postTask(taskCatalog)
    }

    private fun insertTaskDataInDb(data: Task) {
        val task = Runnable { mDb?.taskDao()?.insert(data) }
        mDbWorkerThread.postTask(task)
    }

    private fun insertCustomerDataInDb(data: Customer) {
        val customer = Runnable { mDb?.customerDao()?.insert(data) }
        mDbWorkerThread.postTask(customer)
    }

    private fun insertItemDataInDb(data: Item) {
        val item = Runnable { mDb?.itemDao()?.insert(data) }
        mDbWorkerThread.postTask(item)
    }


    private fun downloadData(fileName: String) {

        var localPathFile: String

        val dataFile = Runnable {

            this.ftpClient = FTP(baseContext)
            var cp = CustomerParse()
            var tp : TaskParse
            var ip = ItemParse()
            var tcp = TaskCatalogParse()
            var res : Result
            var targetFile: String? = null

            localPathFile = applicationContext.filesDir.path + "/" + fileName + ".csv"
            res = ftpClient?.DownloadFile("/MERC/" + fileName + ".csv", localPathFile)!!

            if(res.success) {

                CSVFile = CSV()
                res = CSVFile?.readFile(localPathFile)!!

                when(fileName) {
                    "CUSTOMERS" -> {
                        customers = cp.parse(res.rows!!)

                        // Insert customers in db
                        for(customer in customers) {
                            insertCustomerDataInDb(customer)
                        }

                        fetchCustomers()
                    }

                    "TASKS/CATALOG" -> {

                        val ftpCatalog = Runnable {

                            var resTexts : Result
                            targetFile = "/TASKS/TEXTS.csv"
                            localPathFile = applicationContext.filesDir.path + targetFile
                            ftpClient?.DownloadFile("/MERC" + targetFile, localPathFile)!!
                            resTexts = CSVFile?.readFile(localPathFile)!!

                            var resImages : Result
                            targetFile = "/TASKS/IMAGES.csv"
                            localPathFile = applicationContext.filesDir.path + targetFile
                            ftpClient?.DownloadFile("/MERC" + targetFile, localPathFile)!!
                            resImages = CSVFile?.readFile(localPathFile)!!

                            var resCheckboxes : Result
                            targetFile = "/TASKS/CHECKBOXES.csv"
                            localPathFile = applicationContext.filesDir.path + targetFile
                            ftpClient?.DownloadFile("/MERC" + targetFile, localPathFile)!!
                            resCheckboxes = CSVFile?.readFile(localPathFile)!!

                            val insertTaskCatalog = Runnable {

                                taskCatalogs = tcp.parse(res.rows!!, resTexts.rows!!, resImages.rows!!, resCheckboxes.rows!!)

                                // Insert taskCatalog in db
                                for(taskCatalog in taskCatalogs) {
                                    insertTaskCatalogDataInDb(taskCatalog)
                                }

                                val insertTask = Runnable {

                                    var resTask : Result
                                    targetFile = "/TASKS/MERCHANDISING.csv"
                                    localPathFile = applicationContext.filesDir.path + targetFile
                                    ftpClient?.DownloadFile("/MERC" + targetFile, localPathFile)!!
                                    resTask = CSVFile?.readFile(localPathFile)!!

                                    if(resTask.success) {

                                        tp = TaskParse(taskCatalogs, this)
                                        tasks = tp?.parse(resTask.rows!!)!!

                                        // Insert task in db
                                        for(task in tasks) {
                                            if(task.photoUrls.size > 0) {
                                                downloadImageFiles(task.photoUrls)
                                            }
                                            insertTaskDataInDb(task)
                                        }

                                    }

                                }

                                mDbWorkerThread.postTask(insertTask)

                            }

                            mDbWorkerThread.postTask(insertTaskCatalog)

                        }

                        mDbWorkerThread.postTask(ftpCatalog)
                    }

                    "ITEMS" -> {
                        items = ip.parse(res.rows!!)

                        // Insert items in db
                        for(item in items) {
                            insertItemDataInDb(item)
                        }

                    }
                }

            } else {
                Toast.makeText(applicationContext, res.message, Toast.LENGTH_SHORT).show()
            }

        }

        mDbWorkerThread.postTask(dataFile)

    }

    private fun uploadData(fileNames: List<String>) {

        var tasks : ArrayList<Task>? = ArrayList()
        val folder = filesDir
        val helper = Helpers()
        CSVFile = CSV(this)
        var localPath: String

        fileNames.forEach {
            helper.createDirs(folder, it)
        }

        var uploadInformation = Runnable {

            localPath = applicationContext.filesDir.path  + "/TASKS/"
            val file = File(localPath, "MERCHANDISING.csv")
            var resWrite = Result()
            var resUpload = Result()

            val taskData = mDb?.taskDao()?.getAll()
            tasks = taskData as ArrayList<Task>

            if(tasks?.size!! > 0) {

                resWrite = CSVFile!!.createTaskFile(file.toString(), tasks!!)

                if(resWrite.success) {
                    this.ftpClient = FTP(baseContext)
                    resUpload = this.ftpClient?.UploadFile("/MERC/TASKS/MERCHANDISING.csv",
                            file.toString(),false)!!
                }

                if(resUpload.success) {
                    uploadImageFiles(tasks!!)
                }

            }

        }

        mDbWorkerThread.postTask(uploadInformation)
    }

    private fun uploadImageFiles(tasks : ArrayList<Task>) {

        val localPath = baseContext.getExternalFilesDir("Evidences");

        val writeImageFile = Runnable {

            tasks.forEach{ task ->

                task.photoUrls?.forEach { taskPhotoUrl ->

                    if(taskPhotoUrl != "") {
                        val filename = taskPhotoUrl
                                ?.substring(taskPhotoUrl.lastIndexOf("/") + 1)
                        val file = File(localPath, filename)

                        if(filename != "") {
                            this.ftpClient?.UploadFile("/MERC/Evidences/" + filename,
                                    file.toString(),false)
                        }
                    }

                }

            }

        }

        mDbWorkerThread.postTask(writeImageFile)

    }

    private fun downloadImageFiles(photosURL: ArrayList<String?>) {

        var fileName: String = ""
        baseContext.getExternalFilesDir(TaskActivity.IMAGE_DIRECTORY);

        val download = Runnable {

            photosURL.forEach{photoURL ->

                fileName = photoURL!!
                fileName = fileName.substring(photoURL.lastIndexOf("/") + 1)

                if(fileName != "") {
                    ftpClient?.DownloadFile("/MERC/Evidences/"  +  fileName, photoURL!!)!!
                }

            }

        }

        mDbWorkerThread.postTask(download)
    }

}

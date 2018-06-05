package com.cad.ooqiadev.cadcheck_in.customer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.*
import com.cad.ooqiadev.cadcheck_in.*
import com.cad.ooqiadev.cadcheck_in.main.MainAdapter
import com.cad.ooqiadev.cadcheck_in.models.Task
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import com.cad.ooqiadev.cadcheck_in.task.TaskActivity
import java.util.*


class CustomerActivity : AppCompatActivity() {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    private var customerId: String? = null
    private var taskCatalogs: ArrayList<TaskCatalog>? = null
    var tasks: ArrayList<Task>? = null

    private var adapter: CustomerAdapter? = null

    companion object {
        val CUSTOMER_ID = "customerId"
        val TASK_CATALOG_ID = "taskId"
        val TASK_CATALOG_DESCRIPTION = "taskDescription"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Start worker thread and access db singleton
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = AppDatabase.getInstance(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        // Set toolbar title to location name
        val customerNameString = intent.getStringExtra(MainAdapter.CUSTOMER_NAME)
        customerId = intent.getStringExtra(MainAdapter.CUSTOMER_ID)

        supportActionBar?.title = customerNameString

        fetchTasks()
    }

    private fun bindDataWithUi(taskCatalogData: ArrayList<TaskCatalog>) {
        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.tasksList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        this.adapter = CustomerAdapter(taskCatalogData, this)
        rv.adapter = this.adapter
    }

    private fun fetchTaskCatalogs() {
        val taskCatalog = Runnable {
            val taskCatalogData = mDb?.taskCatalogDao()?.getAll()
            mUiHandler.post({
                if (taskCatalogData == null || taskCatalogData.isEmpty()) {
                    Toast.makeText(applicationContext, "No se encontraron tareas en la base de datos...", Toast.LENGTH_SHORT).show()
                } else {
                    // println(taskCatalogData)
                    this.taskCatalogs = taskCatalogData as ArrayList<TaskCatalog>
                    bindDataWithUi(taskCatalogData as ArrayList<TaskCatalog>)
                }
            })
        }
        mDbWorkerThread.postTask(taskCatalog)
    }

    private fun fetchTasks() {
        val task = Runnable {
            val taskData = mDb?.taskDao()?.getTasksForCustomer(this.customerId!!)
            mUiHandler.post({
                if (taskData == null) {
                    println("No tasks found in db")
                } else {
                    // println(taskData)
                    this.tasks = taskData as ArrayList<Task>
                    fetchTaskCatalogs()
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }

    fun isFulfilled(task: Task): Boolean {
        var text: Boolean = false
        var image: Boolean = false
        var checkBox: Boolean = false

        text = !(task.textValues?.contains("")!!)
        image = !(task.photoUrls?.contains("")!!)
        checkBox = !(task.checkboxValues?.contains("")!!)

        println(task.textValues?.contains(""))

        return (text && image && checkBox)
    }

    fun areFulfilled(): Boolean {
        this.tasks?.forEach {
            val bool = isFulfilled(it)
            if (!bool) {
                return false
            }
        }
        return true
    }

    fun startTaskActivity(taskCatalog: TaskCatalog, task: Task?, position: Int) {
        if (areFulfilled() || task != null) {
            val intent = Intent(this@CustomerActivity, TaskActivity::class.java)
            intent.putExtra(TASK_CATALOG_ID, taskCatalog.id)
            intent.putExtra(TASK_CATALOG_DESCRIPTION, taskCatalog.description)
            intent.putExtra(CUSTOMER_ID, this.customerId)
            startActivityForResult(intent, position)
        } else {
            // Initialize a new instance of
            val builder = AlertDialog.Builder(this)
            // Set the alert dialog title
            builder.setTitle("Tiene tareas pendientes.")
            builder.setMessage("Favor terminar tareas pendientes (!) antes de continuar.")
            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("OK") {
                dialog, which ->
                // Do nothing
            }
            // Make and display the alert dialog on app interface
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("received result " + requestCode)
        this.fetchTask(requestCode)
    }

    private fun fetchTask(position: Int) {
        val task = Runnable {
            val taskData = mDb?.taskDao()?.getTaskForTaskCatalogAndCustomer(this.taskCatalogs!![position].id, this.customerId!!)
            mUiHandler.post({
                if (taskData == null) {
                    println("No tasks found in db")
                } else {
                    this.tasks?.add(taskData)
                    this.adapter!!.notifyItemChanged(position)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }
}
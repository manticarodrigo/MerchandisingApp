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
import com.cad.ooqiadev.cadcheck_in.item.ItemActivity
import com.cad.ooqiadev.cadcheck_in.main.MainAdapter
import com.cad.ooqiadev.cadcheck_in.models.Task
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import com.cad.ooqiadev.cadcheck_in.task.TaskActivity
import kotlin.collections.ArrayList

class CustomerActivity : AppCompatActivity() {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    var customerId: String? = null
    private var taskCatalogs: ArrayList<TaskCatalog>? = null
    var tasks: ArrayList<Task>? = null

    private var adapter: CustomerAdapter? = null
    var bottomSheet: BottomSheet? = null

    companion object {
        val CUSTOMER_ID = "customerId"
        val TASK_CATALOG_ID = "taskCatalogId"
        val TASK_CATALOG_DESCRIPTION = "taskCatalogDescription"
        val TASK_ID = "taskId"
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

    fun startTaskActivity(task: Task) {
        println(task)
        val intent = Intent(this@CustomerActivity, TaskActivity::class.java)
        intent.putExtra(CUSTOMER_ID, this.customerId)
        intent.putExtra(TASK_CATALOG_ID, task.taskCatalogId)
        intent.putExtra(TASK_CATALOG_DESCRIPTION, task.description)
        intent.putExtra(TASK_ID, task.id)
        startActivityForResult(intent, 0)
    }

    fun startNewTaskActivity(taskCatalog: TaskCatalog) {
        val intent = Intent(this@CustomerActivity, TaskActivity::class.java)
        intent.putExtra(TASK_CATALOG_ID, taskCatalog.id)
        intent.putExtra(TASK_CATALOG_DESCRIPTION, taskCatalog.description)
        intent.putExtra(CUSTOMER_ID, this.customerId)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.fetchTasks()
        this.adapter!!.notifyDataSetChanged()
    }

    private fun fetchTasksForTaskCatalog(taskCatalog: TaskCatalog, callback: (ArrayList<Task>?) -> Unit) {
        val task = Runnable {
            val taskData = mDb?.taskDao()?.getTasksForTaskCatalogAndCustomer(taskCatalog.id, this.customerId!!)
            mUiHandler.post({
                if (taskData == null) {
                    println("No tasks found in db")
                    callback(null)
                } else {
                    callback(taskData as ArrayList<Task>)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }

    private fun showBottomSheet(taskCatalog: TaskCatalog, tasks: ArrayList<Task>) {
        this.bottomSheet = BottomSheet(taskCatalog, tasks, this@CustomerActivity)
        this.bottomSheet?.show()
    }

    fun handleTaskPressed(taskCatalog: TaskCatalog, task: Task?) {
        if (areFulfilled() || task != null) {
            if (taskCatalog.isInventory == true) {
                val intent = Intent(this@CustomerActivity, ItemActivity::class.java)
                startActivity(intent)
            } else {
                fetchTasksForTaskCatalog(taskCatalog, {
                    showBottomSheet(taskCatalog, if (it != null) it else arrayListOf<Task>())
                })
            }
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
}
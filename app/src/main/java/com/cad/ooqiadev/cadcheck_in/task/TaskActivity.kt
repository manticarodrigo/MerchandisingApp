package com.cad.ooqiadev.cadcheck_in.task

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import com.cad.ooqiadev.cadcheck_in.*
import com.cad.ooqiadev.cadcheck_in.customer.CustomerActivity
import com.cad.ooqiadev.cadcheck_in.main.MainAdapter
import com.cad.ooqiadev.cadcheck_in.models.Task
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Timestamp
import java.util.*

class TaskActivity : AppCompatActivity() {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()

    private val CAMERA = 2
    var currentTaskCatalog: TaskCatalog? = null
    var currentTask: Task? = null
    var currentPhotoPos: Int = -1
    var currentImageViewEvidence: ImageView? = null
    private var customerId: String? = null

    companion object {
        val IMAGE_DIRECTORY = "Evidences"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Start worker thread and access db singleton
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = AppDatabase.getInstance(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        customerId = intent.getStringExtra(CustomerActivity.CUSTOMER_ID)
        val taskCatalogId = intent.getStringExtra(CustomerActivity.TASK_CATALOG_ID)
        val taskId = intent.getLongExtra(CustomerActivity.TASK_ID, -1L)

        // Set toolbar title to task catalog name
        supportActionBar?.title = intent.getStringExtra(CustomerActivity.TASK_CATALOG_DESCRIPTION)

        fetchTaskCatalog(taskCatalogId, {
            this.currentTaskCatalog = it
            if (taskId != -1L) {
                fetchTask(taskId, {
                    this.currentTask = it
                    bindDataWithUi()
                })
            } else {
                this.currentTask = createNewTask(it!!)
                bindDataWithUi()
            }
        })

        val saveTaskButton = this@TaskActivity.findViewById<Button>(R.id.saveTaskButton)
        saveTaskButton.setOnClickListener {
            if (isFulfilled(this.currentTask!!)) {
                openCommentDialog()
            } else {
                insertTaskDataInDb(this.currentTask!!)
                Toast.makeText(this@TaskActivity, "Task saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindDataWithUi() {
        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.taskList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        var adapter = TaskAdapter(this.currentTaskCatalog!!, this)
        rv.adapter = adapter
    }

    private fun fetchTaskCatalog(taskCatalogId: String, callback: (TaskCatalog?) -> Unit) {
        val taskCatalog = Runnable {
            val taskCatalogData = mDb?.taskCatalogDao()?.findTaskCatalogById(taskCatalogId)
            mUiHandler.post({
                if (taskCatalogData == null) {
                    Toast.makeText(applicationContext, "No se encontro la tarea en la base de datos...", Toast.LENGTH_SHORT).show()
                    callback(null)
                } else {
                    println(taskCatalogData)
                    callback(taskCatalogData)
                }
            })
        }
        mDbWorkerThread.postTask(taskCatalog)
    }

    private fun fetchTask(taskId: Long, callback: (Task?) -> Unit) {
        val task = Runnable {
            val taskData = mDb?.taskDao()?.findTaskById(taskId)
            mUiHandler.post({
                if (taskData == null) {
                    println("No task found in db")
                    callback(null)
                } else {
                    println(taskData)
                    callback(taskData)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }

    private fun createNewTask(taskCatalog: TaskCatalog): Task {
        val stamp = Timestamp(System.currentTimeMillis()).getTime()
        var task = Task(null, taskCatalog.id, this.customerId, taskCatalog.description, arrayListOf(), arrayListOf(), arrayListOf(), "", stamp, null)
        taskCatalog.textLabels?.forEach {
            task.textValues?.add("")
        }
        taskCatalog.photoLabels?.forEach {
            task.photoUrls?.add("")
        }
        taskCatalog.checkboxLabels?.forEach {
            task.checkboxValues?.add("")
        }
        return task
    }

    private fun openCommentDialog() {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)
        var editTextComment: EditText?=null
        // Set the alert dialog title
        builder.setTitle("Agregar comentario.")
        // Set comment input
        editTextComment= EditText(this)
        editTextComment!!.hint="Agregar comentario..."
        editTextComment!!.setText(this.currentTask?.comment)
        editTextComment!!.setPadding(25, 30, 25, 30)
        val container = FrameLayout(this)
        container.setPadding(30, 30, 30, 30)
        container.addView(editTextComment)
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") {
            dialog, which ->
            // Update task comment
             currentTask!!.comment = editTextComment?.text.toString()
             insertTaskDataInDb(currentTask!!)
            Toast.makeText(this@TaskActivity, "Comment Saved!", Toast.LENGTH_SHORT).show()

        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->
            println("You cancelled the dialog.")
        }
        // Make and display the alert dialog on app interface
        val dialog: AlertDialog = builder.create()
        dialog.setView(container)
        dialog.show()
    }

    private fun insertTaskDataInDb(data: Task) {
        val stamp = Timestamp(System.currentTimeMillis()).getTime()
        data.updatedAt = stamp
        val task = Runnable {
            mDb?.taskDao()?.insert(data)
        }
        mDbWorkerThread.postTask(task)
    }

    fun openCamera(arrPos: Int, currentImageViewEvidence: ImageView) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        currentPhotoPos = arrPos
        this.currentImageViewEvidence = currentImageViewEvidence
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != 0 && requestCode == CAMERA) {
            val proofBitmap = data!!.extras!!.get("data") as Bitmap
            currentTask?.photoUrls!![currentPhotoPos] = saveImage(proofBitmap)
            if(currentTask?.photoUrls!![currentPhotoPos] != "") {
                currentImageViewEvidence?.setImageBitmap(proofBitmap)
                Toast.makeText(this@TaskActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImage(myBitmap: Bitmap) : String {

        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val dirMerchandiseObj = baseContext.getExternalFilesDir(TaskActivity.IMAGE_DIRECTORY);

        try {
            val merchandiseDir = File(dirMerchandiseObj.toString())
            // have the object build the directory structure, if needed.
            Log.d("externalDirMerchandise",merchandiseDir.toString())
            if (!merchandiseDir.exists()) {
                merchandiseDir.mkdirs()
            }

            val f = File(merchandiseDir, ( // TODO: Create a different name, not based milliseconds
                    (Calendar.getInstance().getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())

            MediaScannerConnection.scanFile(
                    this, arrayOf(f.getPath()), arrayOf("image/jpeg"), null)

            fo.close()
            Log.d("IMAGE_PATH", f.absolutePath)

            return f.absolutePath
        }
        catch (e1: IOException) { e1.printStackTrace() }

        return ""
    }

    fun fetchImage(url: String?) : Bitmap? {
        val imgFile = File(url)
        if (imgFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            return bitmap
        }
        return null
    }

    private fun isFulfilled (task: Task): Boolean {
        var text: Boolean = false
        var image: Boolean = false
        var checkBox: Boolean = false

        text = !(task.textValues?.contains("")!!)
        image = !(task.photoUrls?.contains("")!!)
        checkBox = !(task.checkboxValues?.contains("")!!)

        return (text && image && checkBox)
    }

}
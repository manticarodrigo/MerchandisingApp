package com.cad.ooqiadev.cadcheck_in.item

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import com.cad.ooqiadev.cadcheck_in.AppDatabase
import com.cad.ooqiadev.cadcheck_in.DbWorkerThread
import com.cad.ooqiadev.cadcheck_in.R
import com.cad.ooqiadev.cadcheck_in.models.Item
import java.util.ArrayList

class ItemActivity: AppCompatActivity() {

    private var mDb: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    var items: ArrayList<Item>? = null
    var adapter: ItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        // Start worker thread and access db singleton
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mDb = AppDatabase.getInstance(this)

        // Init activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        // Set toolbar title
        supportActionBar?.title = "Productos"

        fetchItem()
    }

    override fun onDestroy() {
        // Destroy db singleton instance
        AppDatabase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }


    private fun bindDataWithUi(itemData: ArrayList<Item>) {
        // Create vertical Layout Manager
        val rv = findViewById<RecyclerView>(R.id.productItemList)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        // Access RecyclerView Adapter and load the data
        val adapter = ItemAdapter(itemData, this)
        rv.adapter = adapter

        this.adapter = adapter
    }

    private fun fetchItem() {
        val taskCatalog = Runnable {
            val itemData = mDb?.itemDao()?.getAll() as ArrayList<Item>
            mUiHandler.post({
                if (itemData == null || itemData.isEmpty()) {
                    Toast.makeText (applicationContext, "No item(s) found in db, Toast.LENGTH_SHORT", Toast.LENGTH_SHORT).show()
                } else {
                    bindDataWithUi(itemData)
                }
            })
        }
        mDbWorkerThread.postTask(taskCatalog)
    }

}
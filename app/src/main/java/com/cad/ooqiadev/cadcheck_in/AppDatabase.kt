package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.cad.ooqiadev.cadcheck_in.models.*
import com.cad.ooqiadev.cadcheck_in.models.DAO.*
import com.cad.ooqiadev.cadcheck_in.utils.Converters

@Database(entities = arrayOf(Customer::class, TaskCatalog::class, Task::class, Item::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun taskCatalogDao(): TaskCatalogDao
    abstract fun taskDao(): TaskDao
    abstract fun itemDao(): ItemDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase::class.java, "app.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
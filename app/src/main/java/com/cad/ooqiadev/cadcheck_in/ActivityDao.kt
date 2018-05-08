package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.*

@Dao
interface ActivityDao {

    @Query("SELECT * from activities")
    fun getAll(): List<Activity>

    @Query("select * from activities where id = :id")
    fun findActivityById(id: Long): Activity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activity: Activity)

    @Query("DELETE from activities")
    fun deleteAll()

    @Delete
    fun deleteActivity(activity: Activity)
}
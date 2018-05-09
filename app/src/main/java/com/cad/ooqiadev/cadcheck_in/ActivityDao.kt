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

    @Query("SELECT * FROM activities WHERE owner_id = :id")
    fun getUserActivities(id: Long): List<Activity>

    @Query("SELECT * FROM activities WHERE location_id = :id")
    fun getLocationActivities(id: Long): List<Activity>
}
package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.*

@Dao
interface LocationDao {

    @Query("SELECT * from locations")
    fun getAll(): List<Location>

    @Query("select * from locations where id = :id")
    fun findLocationById(id: Long): Location

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location)

    @Query("DELETE from locations")
    fun deleteAll()

    @Delete
    fun deleteLocation(location: Location)
}
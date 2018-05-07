package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update

@Dao
interface LocationDao {
    @Query("select * from location")
    fun getAllLocations(): List<Location>

    @Query("select * from location where id = :id")
    fun findLocationById(id: Long): Location

//    @Insert(onConflict = REPLACE)
//    fun insertLocation(location: Location)
//
//    @Update(onConflict = REPLACE)
//    fun updateLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)
}
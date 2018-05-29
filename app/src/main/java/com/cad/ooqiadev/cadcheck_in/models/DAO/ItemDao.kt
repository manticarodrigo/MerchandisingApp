package com.cad.ooqiadev.cadcheck_in.models.DAO

import android.arch.persistence.room.*
import com.cad.ooqiadev.cadcheck_in.models.Item

@Dao
interface ItemDao {

    @Query("SELECT * FROM items")
    fun getAll(): List<Item>

    @Query("SELECT * FROM items WHERE number = :id")
    fun findTaskById(id: Long): Item

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Query("DELETE FROM items")
    fun deleteAll()

    @Delete
    fun deleteTask(item: Item)
}
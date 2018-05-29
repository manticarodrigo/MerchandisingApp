package com.cad.ooqiadev.cadcheck_in.models.DAO

import android.arch.persistence.room.*
import com.cad.ooqiadev.cadcheck_in.models.TaskCatalog

@Dao
interface TaskCatalogDao {

    @Query("SELECT * from task_catalogs")
    fun getAll(): List<TaskCatalog>

    @Query("select * from task_catalogs where id = :id")
    fun findTaskCatalogById(id: String): TaskCatalog

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskCatalog: TaskCatalog)

    @Query("DELETE from task_catalogs")
    fun deleteAll()

    @Delete
    fun deleteTaskCatalog(taskCatalog: TaskCatalog)
}
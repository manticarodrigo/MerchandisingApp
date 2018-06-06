package com.cad.ooqiadev.cadcheck_in.models.DAO

import android.arch.persistence.room.*
import com.cad.ooqiadev.cadcheck_in.models.Task

@Dao
interface TaskDao {

    @Query("SELECT * from tasks")
    fun getAll(): List<Task>

    @Query("select * from tasks where id = :id")
    fun findTaskById(id: Long): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Query("DELETE from tasks")
    fun deleteAll()

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE task_catalog_id = :taskCatalogId AND customer_id = :customerId")
    fun getTaskForTaskCatalogAndCustomer(taskCatalogId: String, customerId: String): Task

    @Query("SELECT * FROM tasks WHERE task_catalog_id = :taskCatalogId AND customer_id = :customerId")
    fun getTasksForTaskCatalogAndCustomer(taskCatalogId: String, customerId: String): List<Task>

    @Query("SELECT * FROM tasks WHERE customer_id = :id")
    fun getTasksForCustomer(id: String?): List<Task>
}
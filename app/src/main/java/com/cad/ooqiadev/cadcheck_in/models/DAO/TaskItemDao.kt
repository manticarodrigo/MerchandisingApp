package com.cad.ooqiadev.cadcheck_in.models.DAO

import android.arch.persistence.room.*
import com.cad.ooqiadev.cadcheck_in.models.TaskItem

@Dao
interface TaskItemDao {

    @Query("SELECT * from task_items")
    fun getAll(): List<TaskItem>

    @Query("SELECT * from task_items where id = :id")
    fun findTaskItemById(id: Long): TaskItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskItem: TaskItem)

    @Query("DELETE from task_items")
    fun deleteAll()

    @Delete
    fun deleteTaskItem(taskItem: TaskItem)

}
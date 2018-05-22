package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.*

@Dao
interface TaskDao {

    @Query("SELECT * from tasks")
    fun getAll(): List<Task>

    @Query("select * from tasks where id = :id")
    fun findTaskById(id: Long): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activity: Task)

    @Query("DELETE from tasks")
    fun deleteAll()

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE activity_id = :id")
    fun getActivityTasks(id: Long?): List<Task>
}
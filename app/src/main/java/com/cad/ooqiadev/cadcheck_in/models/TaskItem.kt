package com.cad.ooqiadev.cadcheck_in.models
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "task_items")
data class TaskItem(

        @PrimaryKey var id: String = "",
        @ColumnInfo(name = "description") var description: String? = null,
        @ColumnInfo(name = "quantity") var quantity: Int? = 0,
        @ColumnInfo(name = "start_time") var startTime: Long? = null,
        @ColumnInfo(name = "end_time") var endTime: Long? = null

)
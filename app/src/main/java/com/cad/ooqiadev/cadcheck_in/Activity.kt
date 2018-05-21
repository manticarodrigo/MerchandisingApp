package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.*

@Entity(tableName = "activities")
data class Activity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "owner_id") var ownerId: Long? = null,
        @ColumnInfo(name = "location_id") var locationId: Long? = null,
        @ColumnInfo(name = "order_id") var orderId: Long? = null,
        @ColumnInfo(name = "title") var title: String? = null,
        @ColumnInfo(name = "due_date") var dueTime: Long? = null,
        @ColumnInfo(name = "start_time") var startTime: Long? = null,
        @ColumnInfo(name = "end_time") var endTime: Long? = null,
        @Ignore var pendingTasks: Int? = 0
)
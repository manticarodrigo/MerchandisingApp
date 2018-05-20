package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey
import java.util.*

@Entity(tableName = "activities")
data class Activity(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "owner_id") var ownerId: Long? = null,
        @ColumnInfo(name = "location_id") var locationId: Long? = null,
        @ColumnInfo(name = "order_id") var orderId: Long? = null,
        @ColumnInfo(name = "title") var title: String? = null,
        @ColumnInfo(name = "due_date") var dueTime: Long? = null,
        @ColumnInfo(name = "start_time") var startTime: Long? = null,
        @ColumnInfo(name = "end_time") var endTime: Long? = null
)
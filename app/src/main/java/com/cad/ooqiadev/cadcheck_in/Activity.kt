package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "activity")
data class Activity(
        @PrimaryKey(autoGenerate = true) val id: Long?,
        @ColumnInfo(name = "owner_id") val ownerId: Long,
        @ColumnInfo(name = "location_id") val locationId: Long,
        @ColumnInfo(name = "owner_id") val orderId: Long,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "due_date") val dueDate: Date,
        @ColumnInfo(name = "start_time") val startTime: Date?,
        @ColumnInfo(name = "end_time") val endTime: Date?
)
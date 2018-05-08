package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey
import java.util.*

@Entity(tableName = "activities")
// , foreignKeys = @ForeignKey(entity = Location.class, parentColumns = "id", childColumns = "location_id")
data class Activity(
        @PrimaryKey(autoGenerate = true) val id: Long?,
        @ColumnInfo(name = "owner_id") val ownerId: Long,
        @ColumnInfo(name = "location_id") val locationId: Long,
        @ColumnInfo(name = "order_id") val orderId: Long,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "due_date") val dueTime: Long,
        @ColumnInfo(name = "start_time") val startTime: Long?,
        @ColumnInfo(name = "end_time") val endTime: Long?
)
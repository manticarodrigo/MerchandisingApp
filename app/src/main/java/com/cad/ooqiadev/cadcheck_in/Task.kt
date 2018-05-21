package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
// import android.arch.persistence.room.TypeConverter

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "activity_id") var activityId: Long? = null,
        @ColumnInfo(name = "description") var description: String? = null,
        @ColumnInfo(name = "status") var status: String? = null,
        @ColumnInfo(name = "comment") var comment: String? = null,
        @ColumnInfo(name = "end_time") var endTime: Long? = null
)

//class StatusConverter {
//    companion object {
//
//        @TypeConverter
//        @JvmStatic
//        fun statusToString(status: Status): String? =
//                status.string
//
//        @TypeConverter
//        @JvmStatic
//        fun stringToStatus(status: String): Status? =
//                Status.valueOf(status)
//    }
//}
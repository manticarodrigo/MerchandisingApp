package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Ignore

@Entity(tableName = "locations")
data class Location(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "name") var name: String = "",
        @ColumnInfo(name = "direction") var direction: String = "",
        @Ignore var pendingActivities: Int? = 0
)
package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
data class User(
        @PrimaryKey(autoGenerate = true) val id: Long?,
        @ColumnInfo(name = "name") val name: String?
)
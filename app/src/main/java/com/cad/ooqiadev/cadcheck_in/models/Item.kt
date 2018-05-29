package com.cad.ooqiadev.cadcheck_in.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "items")
data class Item(

    @PrimaryKey var number: String = "",
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "um") var um: String? = null,
    @ColumnInfo(name = "upcode") var upcode: String? = null,
    @ColumnInfo(name = "reg_price") var regPrice: Double? = 0.0,
    @ColumnInfo(name = "retail_price") var retailPrice: Double? = 0.0

)
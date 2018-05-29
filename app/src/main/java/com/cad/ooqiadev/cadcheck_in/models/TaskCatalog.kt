package com.cad.ooqiadev.cadcheck_in.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "task_catalogs")
data class TaskCatalog(
        @PrimaryKey var id: String = "",
        @ColumnInfo(name = "description") var description: String? = null,
        @ColumnInfo(name = "is_inventory") var isInventory: Boolean? = false,
        @ColumnInfo(name = "text_labels") var textLabels: ArrayList<String?> = ArrayList(),
        @ColumnInfo(name = "photo_labels") var photoLabels: ArrayList<String?> = ArrayList(),
        @ColumnInfo(name = "checkbox_labels") var checkboxLabels: ArrayList<String?> = ArrayList()
)
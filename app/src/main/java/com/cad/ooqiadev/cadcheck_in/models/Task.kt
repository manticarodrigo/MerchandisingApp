package com.cad.ooqiadev.cadcheck_in.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "task_catalog_id") var taskCatalogId: String? = null,
        @ColumnInfo(name = "customer_id") var customerId: String? = null,
        @ColumnInfo(name = "description") var description: String? = null,
        @ColumnInfo(name = "text_values") var textValues: ArrayList<String?> = ArrayList(),
        @ColumnInfo(name = "photo_urls") var photoUrls: ArrayList<String?> = ArrayList(),
        @ColumnInfo(name = "checkbox_values") var checkboxValues: ArrayList<String?> = ArrayList(),
        @ColumnInfo(name = "comment") var comment: String? = null,
        @ColumnInfo(name = "created_at") var createdAt: Long? = null,
        @ColumnInfo(name = "updated_at") var updatedAt: Long? = null
)
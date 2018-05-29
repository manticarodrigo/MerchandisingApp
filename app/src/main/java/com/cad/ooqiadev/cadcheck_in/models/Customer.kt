package com.cad.ooqiadev.cadcheck_in.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(

    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "address1") var address1: String? = null,
    @ColumnInfo(name = "address2") var address2: String? = null,
    @ColumnInfo(name = "city") var city: String? = null,
    @ColumnInfo(name = "state") var state: String? = null,
    @ColumnInfo(name = "zip_code") var zipCode: String? = null,
    @ColumnInfo(name = "phone") var phone: String? = null,
    @ColumnInfo(name = "fax") var fax: String? = null,
    @ColumnInfo(name = "contact_name") var contactName: String? = null,
    @ColumnInfo(name = "sales_person") var salesPerson: String? = null,
    @ColumnInfo(name = "type") var type: String? = null

)
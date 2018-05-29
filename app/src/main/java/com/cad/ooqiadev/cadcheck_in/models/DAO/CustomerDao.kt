package com.cad.ooqiadev.cadcheck_in.models.DAO

import android.arch.persistence.room.*
import com.cad.ooqiadev.cadcheck_in.models.Customer

@Dao
interface CustomerDao {

    @Query("SELECT * from customers")
    fun getAll(): List<Customer>

    @Query("SELECT * FROM customers WHERE type = :id")
    fun getCustomersByType(id: String?): List<Customer>

    @Query("SELECT * from customers where id = :id")
    fun findCustomerById(id: Long): Customer

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customer: Customer)

    @Query("DELETE from customers")
    fun deleteAll()

    @Delete
    fun deleteCustomer(customer: Customer)

}
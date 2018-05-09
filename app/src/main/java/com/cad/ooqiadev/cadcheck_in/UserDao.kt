package com.cad.ooqiadev.cadcheck_in

import android.arch.persistence.room.*

@Dao
interface UserDao {

    @Query("SELECT * from users")
    fun getAll(): List<User>

    @Query("select * from users where id = :id")
    fun findUserById(id: Long): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("DELETE from users")
    fun deleteAll()

    @Delete
    fun deleteUser(user: User)

}
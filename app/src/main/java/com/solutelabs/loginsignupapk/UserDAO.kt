package com.solutelabs.loginsignupapk

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

//    @Query("SELECT * FROM user")
//    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user ORDER BY id DESC")
    fun getAllUsers(): List<User>

    @Query("DELETE FROM user")
    fun deleteAllUsers()

    @Query("SELECT * FROM user WHERE db_email = :email AND db_password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE db_email = :email")
    suspend fun checkUserIsExist(email: String): User?

    @Query("SELECT * FROM user WHERE db_email = :email")
    fun getDetailsFromEmail(email: String): User?

    @Query("DELETE FROM user WHERE db_email = :email")
    fun deleteUserFromEmail(email: String)



}
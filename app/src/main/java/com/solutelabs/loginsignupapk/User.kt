package com.solutelabs.loginsignupapk

import androidx.room.*
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val db_firstName: String,
    val db_lastName: String,
    val db_email: String,
    val db_password: String,
    val db_gender: String,
    val db_dateOfBirth: String,
    val db_age: Int,
    val db_hobby: String
)


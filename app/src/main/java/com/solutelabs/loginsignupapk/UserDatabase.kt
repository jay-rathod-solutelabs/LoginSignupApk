package com.solutelabs.loginsignupapk

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.InputStreamReader


@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao() : UserDAO

    companion object{
        @Volatile
        private var INSTANCE : UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            if(INSTANCE == null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,UserDatabase::class.java,"userDB").build()
                }
            }
            return INSTANCE!!
        }

    }



}
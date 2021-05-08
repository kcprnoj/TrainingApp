package com.main.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [Run::class],
        version = 1
)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDao(): RunDAO

    companion object {
        @Volatile
        private var INSTANCE: RunDatabase? = null

        fun getDatabase(context: Context): RunDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = Room.databaseBuilder(
                        context.applicationContext,
                        RunDatabase::class.java,
                        "run_database"
                    ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
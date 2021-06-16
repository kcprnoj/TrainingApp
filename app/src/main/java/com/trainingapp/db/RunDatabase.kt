package com.trainingapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(
        entities = [Run::class],
        version = 1
)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDao(): RunDAO

    companion object {
        @Volatile
        private var INSTANCE: RunDatabase? = null

        private class RunDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                    scope.launch {

                    }
                }
            }
        }


        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RunDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = Room.databaseBuilder(
                        context.applicationContext,
                        RunDatabase::class.java,
                        "run_database"
                    )
                    .addCallback(RunDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
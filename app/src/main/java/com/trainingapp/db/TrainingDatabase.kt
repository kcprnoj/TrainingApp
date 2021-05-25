package com.trainingapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(
        entities = [Training::class],
        version = 1
)
abstract class TrainingDatabase : RoomDatabase() {
    abstract fun getTrainingDao(): TrainingDAO

    companion object {
        @Volatile
        private var INSTANCE: TrainingDatabase? = null

        private class TrainingDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        val dao = database.getTrainingDao()
                        dao.insert(Training(Calendar.getInstance().timeInMillis, "TEST", 180000L, 180))
                        dao.insert(Training(Calendar.getInstance().timeInMillis, "TEST", 180000L, 180))
                        dao.insert(Training(Calendar.getInstance().timeInMillis, "TEST", 180000L, 180))
                    }
                }
            }
        }


        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): TrainingDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = Room.databaseBuilder(
                        context.applicationContext,
                        TrainingDatabase::class.java,
                        "training_database"
                    )
                    .addCallback(TrainingDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
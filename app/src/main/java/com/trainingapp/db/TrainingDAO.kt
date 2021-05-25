package com.trainingapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDAO {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insert(training: Training)

    @Delete
    suspend fun delete(training: Training)

    @Query("SELECT * FROM training_table ORDER BY timestamp DESC")
    fun getAllTrainingsByDate(): Flow<List<Training>>

    @Query("SELECT SUM(time) FROM training_table")
    fun getTotalTime(): Flow<Long>

    @Query("SELECT SUM(calories) FROM training_table")
    fun getTotalCalories(): Flow<Int>
}
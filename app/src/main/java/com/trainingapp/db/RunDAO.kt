package com.trainingapp.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDAO {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insert(run: Run)

    @Delete
    suspend fun delete(run: Run)

    @Query("SELECT * FROM run_table ORDER BY timestamp DESC")
    fun getAllRunsByDate(): Flow<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY time DESC")
    fun getAllRunsByTime(): Flow<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY calories DESC")
    fun getAllRunsByCalories(): Flow<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY distance DESC")
    fun getAllRunsByDistance(): Flow<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY avgSpeed DESC")
    fun getAllRunsByAvgSpeed(): Flow<List<Run>>

    @Query("SELECT SUM(time) FROM run_table")
    fun getTotalTime(): Flow<Long>

    @Query("SELECT SUM(calories) FROM run_table")
    fun getTotalCalories(): Flow<Int>

    @Query("SELECT SUM(distance) FROM run_table")
    fun getTotalDistance(): Flow<Float>

    @Query("SELECT AVG(avgSpeed) FROM run_table")
    fun getTotalAvgSpeed(): Flow<Float>
}
package com.trainingapp.repositories

import com.trainingapp.db.Run
import com.trainingapp.db.RunDAO
import kotlinx.coroutines.flow.Flow

class RunRepository(private val runDAO: RunDAO) {

    val totalTime: Flow<Long> = runDAO.getTotalTime()

    val totalCalories: Flow<Int> = runDAO.getTotalCalories()

    val totalDistance: Flow<Float> = runDAO.getTotalDistance()

    val totalAvgSpeed: Flow<Float> = runDAO.getTotalAvgSpeed()

    val allRunsByDate: Flow<List<Run>> = runDAO.getAllRunsByDate()

    val allRunsByTime: Flow<List<Run>> = runDAO.getAllRunsByTime()

    val allRunsByCalories: Flow<List<Run>> = runDAO.getAllRunsByCalories()

    val allRunsByDistance: Flow<List<Run>> = runDAO.getAllRunsByDistance()

    val allRunsByAvgSpeed: Flow<List<Run>> = runDAO.getAllRunsByAvgSpeed()

    suspend fun insert(run: Run) {
        runDAO.insert(run)
    }

    suspend fun delete(run: Run) {
        runDAO.delete(run)
    }
}
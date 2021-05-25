package com.trainingapp.repositories

import com.trainingapp.db.Training
import com.trainingapp.db.TrainingDAO
import kotlinx.coroutines.flow.Flow

class TrainingRepository(private val trainingDAO: TrainingDAO) {

    val totalTime: Flow<Long> = trainingDAO.getTotalTime()

    val totalCalories: Flow<Int> = trainingDAO.getTotalCalories()

    val allTrainingsByDate: Flow<List<Training>> = trainingDAO.getAllTrainingsByDate()


    suspend fun insert(training: Training) {
        trainingDAO.insert(training)
    }

    suspend fun delete(training: Training) {
        trainingDAO.delete(training)
    }
}
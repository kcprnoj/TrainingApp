package com.trainingapp.model.repository

import com.trainingapp.model.data.Training
import com.trainingapp.model.data.TrainingCreate
import com.trainingapp.model.webservice.TrainingService

class TrainingRepository (private val service: TrainingService) {
    fun getAllTrainings(username: String, authorizationKey: String): List<Training> {
        return service.getTrainings(username, authorizationKey)
    }
    fun addTraining(training: TrainingCreate, authorizationKey: String) {
        return service.addTraining(training, authorizationKey)
    }
}
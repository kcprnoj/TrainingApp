package com.trainingapp.model.repository

import com.trainingapp.model.data.Training
import com.trainingapp.model.data.TrainingCreate
import com.trainingapp.model.webservice.TrainingService

class TrainingRepository (private val service: TrainingService, private val pref: PrefRepository) {
    fun getAllTrainings(username: String): List<Training> {
        return service.getTrainings(username, pref.getAuthorizationKey())
    }
    fun addTraining(training: TrainingCreate) {
        return service.addTraining(training, pref.getAuthorizationKey())
    }
}
package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.Training
import com.trainingapp.model.webservice.TrainingService

class HistoryViewModel(private val service: TrainingService) : ViewModel(){
    fun getAllTrainings(username: String, key: String): List<Training> {
        return service.getTrainings(username, key)
    }
}
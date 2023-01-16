package com.trainingapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.Training
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.webservice.TrainingService

class HistoryViewModel(private val service: TrainingService, private val repository: PrefRepository) : ViewModel(){



    fun getAllTrainings(): List<Training> {
        return service.getTrainings(repository.getUsername(), repository.getAuthorizationKey())
    }
}
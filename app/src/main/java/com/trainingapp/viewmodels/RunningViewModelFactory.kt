package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.webservice.TrainingService

class RunningViewModelFactory(private val service: TrainingService,  private val repository: PrefRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((RunningViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return RunningViewModel(service, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
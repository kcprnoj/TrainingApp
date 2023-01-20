package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.repository.PreferencesRepository
import com.trainingapp.model.repository.TrainingRepository

class RunningViewModelFactory(private val trainingRepository: TrainingRepository,  private val repository: PreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((RunningViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return RunningViewModel(trainingRepository, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
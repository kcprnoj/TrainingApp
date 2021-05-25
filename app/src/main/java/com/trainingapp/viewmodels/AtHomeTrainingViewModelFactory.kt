package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.repositories.TrainingRepository

class AtHomeTrainingViewModelFactory (private val repository: TrainingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((AtHomeTrainingViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return AtHomeTrainingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

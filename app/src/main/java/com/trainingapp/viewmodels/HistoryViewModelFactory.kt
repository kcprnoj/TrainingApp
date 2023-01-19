package com.trainingapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.TrainingRepository
import com.trainingapp.model.webservice.TrainingService

class HistoryViewModelFactory (private val trainingRepository: TrainingRepository, private val repository: PrefRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((HistoryViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(trainingRepository, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

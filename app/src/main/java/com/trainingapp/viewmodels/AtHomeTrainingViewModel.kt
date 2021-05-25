package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingapp.db.Training
import com.trainingapp.repositories.TrainingRepository
import kotlinx.coroutines.launch

class AtHomeTrainingViewModel(private val repository: TrainingRepository) : ViewModel() {


    fun insert(training: Training) = viewModelScope.launch {
        repository.insert(training)
    }

}

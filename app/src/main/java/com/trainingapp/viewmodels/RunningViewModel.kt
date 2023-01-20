package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.TrainingCreate
import com.trainingapp.model.repository.PreferencesRepository
import com.trainingapp.model.repository.TrainingRepository

class RunningViewModel(private val trainingRepository: TrainingRepository,  private val preferences: PreferencesRepository) : ViewModel() {


    fun addTraining(training: TrainingCreate) {
        training.username = preferences.getUsername()
        trainingRepository.addTraining(training, preferences.getAuthorizationKey())
    }

}

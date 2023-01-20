package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.Training
import com.trainingapp.model.repository.PreferencesRepository
import com.trainingapp.model.repository.TrainingRepository

class HistoryViewModel(private val trainingRepository: TrainingRepository, private val preferences: PreferencesRepository) : ViewModel(){

    private val _trainings = MutableLiveData<List<Training>>()
    val trainings : LiveData<List<Training>>
        get() = _trainings

    fun refreshAllTrainings() {
        _trainings.value = trainingRepository.getAllTrainings(preferences.getUsername(), preferences.getAuthorizationKey())
    }
}
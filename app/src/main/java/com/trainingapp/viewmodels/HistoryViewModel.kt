package com.trainingapp.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trainingapp.model.data.Training
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.TrainingRepository
import com.trainingapp.model.webservice.TrainingService

class HistoryViewModel(private val trainingRepository: TrainingRepository, private val repository: PrefRepository) : ViewModel(){

    private val _trainings = MutableLiveData<List<Training>>()
    val trainings : LiveData<List<Training>>
        get() = _trainings

    fun refreshAllTrainings() {
        _trainings.value = trainingRepository.getAllTrainings(repository.getUsername())
    }
}
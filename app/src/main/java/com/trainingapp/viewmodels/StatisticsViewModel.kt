package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.trainingapp.repositories.RunRepository

class StatisticsViewModel(private val repository: RunRepository) : ViewModel() {
    val totalDistance: LiveData<Float> = repository.totalDistance.asLiveData()
}
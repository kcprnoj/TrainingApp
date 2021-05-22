package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.trainingapp.db.Run
import com.trainingapp.repositories.RunRepository
import kotlinx.coroutines.launch
import java.util.*

class RunningViewModel(private val repository: RunRepository) : ViewModel() {

    val totalTime: LiveData<Long> = repository.totalTime.asLiveData()

    val totalCalories: LiveData<Int> = repository.totalCalories.asLiveData()

    val totalDistance: LiveData<Float> = repository.totalDistance.asLiveData()

    val totalAvgSpeed: LiveData<Float> = repository.totalAvgSpeed.asLiveData()

    val allRunsByDate: LiveData<List<Run>> = repository.allRunsByDate.asLiveData()

    val allRunsByTime: LiveData<List<Run>> = repository.allRunsByTime.asLiveData()

    val allRunsByCalories: LiveData<List<Run>> = repository.allRunsByCalories.asLiveData()

    val allRunsByDistance: LiveData<List<Run>> = repository.allRunsByDistance.asLiveData()

    val allRunsByAvgSpeed: LiveData<List<Run>> = repository.allRunsByAvgSpeed.asLiveData()

    fun insert(run: Run) = viewModelScope.launch {
        repository.insert(run)
    }

    fun delete(run: Run) = viewModelScope.launch {
        repository.delete(run)
    }

}
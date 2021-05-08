package com.main

import androidx.lifecycle.*
import com.main.database.Run
import com.main.database.RunRepository
import kotlinx.coroutines.launch

class RunViewModel(private val repository: RunRepository) : ViewModel() {

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

class RunViewModelFactory(private val repository: RunRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((RunViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return RunViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
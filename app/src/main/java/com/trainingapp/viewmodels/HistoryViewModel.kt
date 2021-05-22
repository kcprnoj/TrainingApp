package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.trainingapp.db.Run
import com.trainingapp.repositories.RunRepository

class HistoryViewModel(private val repository: RunRepository) : ViewModel(){
    val allRunsByDate: LiveData<List<Run>> = repository.allRunsByDate.asLiveData()
}
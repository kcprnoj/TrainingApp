package com.trainingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.trainingapp.db.Run
import com.trainingapp.repositories.RunRepository
import kotlinx.coroutines.launch

class RunningViewModel(private val repository: RunRepository) : ViewModel() {


    fun insert(run: Run) = viewModelScope.launch {
        repository.insert(run)
    }



}

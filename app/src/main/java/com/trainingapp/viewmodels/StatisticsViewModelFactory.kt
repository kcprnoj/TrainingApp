package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StatisticsViewModelFactory () : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((StatisticsViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
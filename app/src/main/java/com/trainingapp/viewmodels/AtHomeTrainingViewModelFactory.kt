package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AtHomeTrainingViewModelFactory () : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((AtHomeTrainingViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return AtHomeTrainingViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

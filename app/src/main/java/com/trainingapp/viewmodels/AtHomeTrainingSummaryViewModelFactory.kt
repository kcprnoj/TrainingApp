package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AtHomeTrainingSummaryViewModelFactory (private val calories : Int, private val numberOfExercises : Int, private val time : Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((AtHomeTrainingSummaryViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return AtHomeTrainingSummaryViewModel(calories,numberOfExercises,time) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
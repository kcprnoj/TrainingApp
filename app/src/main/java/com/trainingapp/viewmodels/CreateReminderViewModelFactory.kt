package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trainingapp.repositories.TrainingRepository

class CreateReminderViewModelFactory () : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((CreateReminderViewModel::class.java))) {
            @Suppress("UNCHECKED_CAST")
            return CreateReminderViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

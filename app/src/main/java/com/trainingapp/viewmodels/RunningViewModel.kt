package com.trainingapp.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.trainingapp.model.data.TrainingCreate
import com.trainingapp.model.repository.PrefRepository
import com.trainingapp.model.repository.TrainingRepository
import com.trainingapp.model.webservice.TrainingService

class RunningViewModel(private val trainingRepository: TrainingRepository,  private val repository: PrefRepository) : ViewModel() {


    fun addTraining(training: TrainingCreate) {
        training.username = repository.getUsername()
        trainingRepository.addTraining(training)
    }

}

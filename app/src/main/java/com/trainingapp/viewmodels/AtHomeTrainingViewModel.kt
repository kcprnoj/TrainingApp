package com.trainingapp.viewmodels

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trainingapp.db.Training
import com.trainingapp.repositories.TrainingRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class AtHomeTrainingViewModel(private val repository: TrainingRepository) : ViewModel() {

    data class Exercise(
        val text: String,
        val repeats: Int,
        val pic : ImageView
    )

    private lateinit var exercisesList: MutableList<Exercise>

    private val _exerciseText = MutableLiveData<String>()
    val exerciseText : LiveData<String>
        get() = _exerciseText

    private val _exerciseRepeats = MutableLiveData<Int>()
    val exerciseRepeats : LiveData<Int>
        get() = _exerciseRepeats

    private val _exerciseImage = MutableLiveData<ImageView>()
    val exerciseImage : LiveData<ImageView>
        get() = _exerciseImage

    init {
        createExerciseList()
    }

    private fun createExerciseList() {
        TODO("Not yet implemented")
    }


    fun insert(training: Training) = viewModelScope.launch {
        repository.insert(training)
    }



}

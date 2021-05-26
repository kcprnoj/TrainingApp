package com.trainingapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AtHomeTrainingSummaryViewModel(calories : Int, numberOfExercises: Int, time: Long) : ViewModel(){
    private val _calories = MutableLiveData<Int>()
    val calories: LiveData<Int>
        get() = _calories
    private val _exercisesNumber = MutableLiveData<Int>()
    val exercisesNumber: LiveData<Int>
        get() = _exercisesNumber
    private val _time = MutableLiveData<Long>()
    val time: LiveData<Long>
        get() = _time

    init {
        _calories.value = calories
        _exercisesNumber.value = numberOfExercises
        _time.value = time
    }
}
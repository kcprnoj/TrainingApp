package com.trainingapp.viewmodels

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.main.R
import com.trainingapp.db.Training
import com.trainingapp.repositories.TrainingRepository
import com.trainingapp.ui.MainActivity
import kotlinx.coroutines.launch
import java.util.*

class AtHomeTrainingViewModel(private val repository: TrainingRepository) : ViewModel() {

    data class Exercise(
            val text: Int,
            val repeats: Int,
            val picId: Int
    )

    private lateinit var exercisesList: MutableList<Exercise>

    private val _exerciseText = MutableLiveData<Int>()
    val exerciseText : LiveData<Int>
        get() = _exerciseText

    private val _exerciseRepeats = MutableLiveData<Int>()
    val exerciseRepeats : LiveData<Int>
        get() = _exerciseRepeats

    private val _exerciseImage = MutableLiveData<Int>()
    val exerciseImage : LiveData<Int>
        get() = _exerciseImage

    private val _eventTrainingFinished = MutableLiveData<Boolean>()
    val eventTrainingFinished : LiveData<Boolean>
        get() = _eventTrainingFinished

    private val _calories = MutableLiveData<Int>()
    val calories : LiveData<Int>
        get() = _calories

    private val _repeatsSum = MutableLiveData<Int>()
    val repeatsSum : LiveData<Int>
        get() = _repeatsSum

    private val _duration = MutableLiveData<Long>()
    val duration : LiveData<Long>
        get() = _duration

    private lateinit var currentExercise: Exercise
    private var exerciseIndex = 0
    private var exercisesString = ""
    private var startTime = 0L
    private var endTime = 0L
    private var repSum = 0

    init {
        createExerciseList()
        startTime = System.currentTimeMillis()
        nextExercise()
        _eventTrainingFinished.value = false
        _repeatsSum.value = 0
    }

    fun nextExercise() {
        if (exerciseIndex == exercisesList.size) _eventTrainingFinished.value = true
        else {
            currentExercise = exercisesList[exerciseIndex]
            _exerciseImage.value = currentExercise.picId
            _exerciseRepeats.value = currentExercise.repeats
            _exerciseText.value = currentExercise.text
            repSum += 10
            exercisesString.plus(currentExercise.text)
            exerciseIndex++
        }
    }

    private fun createExerciseList() {
        exercisesList = mutableListOf(
                Exercise(text= R.string.exercise1,repeats = R.string.numberOfExercises, picId = R.drawable.exercise1),
                Exercise(text= R.string.exercise2,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise2),
                Exercise(text= R.string.exercise3,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise3),
                Exercise(text= R.string.exercise4,repeats = R.string.numberOfExercises, picId = R.drawable.exercise4),
                Exercise(text= R.string.exercise5,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise5),
                Exercise(text= R.string.exercise6,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise6),
                Exercise(text= R.string.exercise7,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise7),
                Exercise(text= R.string.exercise8,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise8),
                Exercise(text= R.string.exercise9,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise9),
                Exercise(text= R.string.exercise10,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise10),
                Exercise(text= R.string.exercise11,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise11),
                Exercise(text= R.string.exercise12,repeats =  R.string.numberOfExercises, picId = R.drawable.exercise12)
        )
        exercisesList.shuffle()
        exerciseIndex = 0
    }


    fun insert(training: Training) = viewModelScope.launch {
        repository.insert(training)
    }

    fun onTrainingFinished(){
        endTime = System.currentTimeMillis()
        val duration = (endTime - startTime)/1000
        _duration.value = duration
        _repeatsSum.value = repSum
        //TODO waga z profilu
        val weight = 73F
        val cal = calculateCalories(duration/60, weight)
        _calories.value = cal
        val training = Training(
                Calendar.getInstance().timeInMillis,
                exercisesString,
                duration,
                cal
                )

        insert(training)
        _eventTrainingFinished.value = false
        exerciseIndex = 0
        exercisesString = ""
    }

    private fun calculateCalories(duration: Long, weight: Float): Int{
        val met = 5F
        return (duration*met*3.5*weight/200).toInt()
    }
}

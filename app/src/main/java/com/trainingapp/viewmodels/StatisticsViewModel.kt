package com.trainingapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.trainingapp.model.data.Training

class StatisticsViewModel() : ViewModel() {
    //val allRunsByDate: LiveData<List<Training>> = repository.allRunsByDate.asLiveData()

     fun calculateBMI(height: Float, weight: Float): Float{
        val heightVal = height / 100
        return weight / (heightVal * heightVal)
    }

}

class MyValueFormatter(private val xValsDateLabel: ArrayList<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return if (value.toInt() >= 0 && value.toInt() <= xValsDateLabel.size - 1) {
            xValsDateLabel[value.toInt()]
        } else {
            ("").toString()
        }
    }
}
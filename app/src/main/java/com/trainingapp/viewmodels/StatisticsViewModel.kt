package com.trainingapp.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.trainingapp.db.Run
import com.trainingapp.repositories.RunRepository
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class StatisticsViewModel(repository: RunRepository) : ViewModel() {
    val allRunsByDate: LiveData<List<Run>> = repository.allRunsByDate.asLiveData()

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
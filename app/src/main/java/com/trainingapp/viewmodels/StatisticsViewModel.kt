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

    private val _entryList: ArrayList<Entry> = ArrayList()
    val entryList: ArrayList<Entry>
        get() = _entryList

    private val _stringDateList: ArrayList<String> = ArrayList()
    val stringDateList: ArrayList<String>
        get() = _stringDateList

    private lateinit var _lineData : LineData
    val lineData : LineData
        get() = _lineData

    fun createChartData(it: List<Run>) {
        for (i in it.reversed().indices) {
            _entryList.add(Entry(i.toFloat(), it[i].distance))
            val currentDate = Instant.ofEpochMilli(it[i].timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            _stringDateList.add(currentDate.toString())
        }
        val lineDataSet  = LineDataSet(_entryList, "Distance")
        lineDataSet.color = Color.RED
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(true)
        _lineData = LineData(lineDataSet)
        lineData.notifyDataChanged()
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
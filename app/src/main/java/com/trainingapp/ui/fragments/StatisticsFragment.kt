package com.trainingapp.ui.fragments
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.main.R
import com.example.main.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.trainingapp.RunApplication
import com.trainingapp.db.Run
import com.trainingapp.viewmodels.MyValueFormatter
import com.trainingapp.viewmodels.StatisticsViewModel
import com.trainingapp.viewmodels.StatisticsViewModelFactory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var viewModel : StatisticsViewModel
    private lateinit var viewModelFactory: StatisticsViewModelFactory
    private lateinit var lineChart : LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_statistics,container,false)
        viewModelFactory = StatisticsViewModelFactory((activity?.application as RunApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StatisticsViewModel::class.java)
        binding.statisticsViewModel = viewModel
        binding.lifecycleOwner = this

       createChart()
        return binding.root
    }

    private fun createChart() {
        lineChart = binding.lineChart
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.description.text = ""
        lineChart.description.textColor = Color.parseColor("#516260")
        lineChart.axisLeft.textColor = Color.parseColor("#516260")
        lineChart.axisRight.textColor =Color.parseColor("#516260")
        lineChart.legend.textColor = Color.parseColor("#516260")

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.parseColor("#516260")

        viewModel.allRunsByDate.observe(viewLifecycleOwner, {
            val entryList = ArrayList<Entry>()
            val stringDateList = ArrayList<String>()
            val newArray = it.reversed()
            for (i in newArray.indices) {
                entryList.add(Entry(i.toFloat(), newArray[i].distance))
                val currentDate = Instant.ofEpochMilli(newArray[i].timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                stringDateList.add(currentDate.toString())
            }

            val lineDataSet  = LineDataSet(entryList, resources.getString(R.string.distance))
            lineDataSet.color = Color.RED
            lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
            lineDataSet.setDrawValues(false)
            lineDataSet.setDrawCircles(true)

            val lineData = LineData(lineDataSet)
            lineData.notifyDataChanged()
            lineChart.data = lineData
            xAxis.labelCount = 3
            xAxis.valueFormatter = MyValueFormatter(stringDateList)

            lineChart.invalidate()
            lineChart.setVisibleXRangeMaximum(3F)

        })
    }

    private fun calculateBMI(){

    }

}



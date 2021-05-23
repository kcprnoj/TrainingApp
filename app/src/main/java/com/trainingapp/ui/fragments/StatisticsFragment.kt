package com.trainingapp.ui.fragments
import android.os.Bundle
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
import com.trainingapp.RunApplication
import com.trainingapp.viewmodels.MyValueFormatter
import com.trainingapp.viewmodels.StatisticsViewModel
import com.trainingapp.viewmodels.StatisticsViewModelFactory


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
        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.description.text = ""

        viewModel.allRunsByDate.observe(viewLifecycleOwner, {
            viewModel.createChartData(it)
            lineChart.data = viewModel.lineData
            xAxis.labelCount = viewModel.entryList.size - 1
            xAxis.valueFormatter = MyValueFormatter(viewModel.stringDateList)
            lineChart.invalidate()
        })
    }
}



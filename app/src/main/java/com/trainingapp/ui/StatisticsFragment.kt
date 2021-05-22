package com.trainingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.main.R
import com.example.main.databinding.FragmentStatisticsBinding
import com.trainingapp.RunApplication
import com.trainingapp.viewmodels.StatisticsViewModel
import com.trainingapp.viewmodels.StatisticsViewModelFactory


class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var viewModel : StatisticsViewModel
    private lateinit var viewModelFactory: StatisticsViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_statistics,container,false)
        viewModelFactory = StatisticsViewModelFactory((activity?.application as RunApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StatisticsViewModel::class.java)
        binding.statisticsViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.totalDistance.observe(viewLifecycleOwner, {
            //TODO chart
        })

        return binding.root
    }
}
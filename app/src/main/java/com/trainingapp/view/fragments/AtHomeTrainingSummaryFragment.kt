package com.trainingapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.main.R
import com.example.main.databinding.FragmentAtHomeTrainingSummaryBinding
import com.trainingapp.viewmodels.AtHomeTrainingSummaryViewModel
import com.trainingapp.viewmodels.AtHomeTrainingSummaryViewModelFactory


class AtHomeTrainingSummaryFragment : Fragment() {
    private lateinit var binding: FragmentAtHomeTrainingSummaryBinding
    private lateinit var viewModel: AtHomeTrainingSummaryViewModel
    private lateinit var viewModelFactory: AtHomeTrainingSummaryViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_at_home_training_summary,container,false)
        val fragmentArgs by navArgs<AtHomeTrainingSummaryFragmentArgs>()
        viewModelFactory = AtHomeTrainingSummaryViewModelFactory(fragmentArgs.calories,fragmentArgs.exercisesNumber,fragmentArgs.time)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AtHomeTrainingSummaryViewModel::class.java)
        binding.atHomeTrainingSummaryViewModel = viewModel
        binding.lifecycleOwner = this
        binding.endButton.setOnClickListener {
            findNavController().navigate(AtHomeTrainingSummaryFragmentDirections.actionAtHomeTrainingSummaryFragmentToTrainingFragment())
        }
        return binding.root
    }

}
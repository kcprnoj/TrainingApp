package com.trainingapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.main.R
import com.example.main.databinding.FragmentAtHomeTrainingBinding
import com.trainingapp.RunApplication
import com.trainingapp.viewmodels.AtHomeTrainingViewModel
import com.trainingapp.viewmodels.AtHomeTrainingViewModelFactory

class AtHomeTrainingFragment : Fragment() {
    private lateinit var binding: FragmentAtHomeTrainingBinding
    private lateinit var viewModel : AtHomeTrainingViewModel
    private lateinit var viewModelFactory : AtHomeTrainingViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_at_home_training,container,false)
        viewModelFactory = AtHomeTrainingViewModelFactory((activity?.application as RunApplication).repositoryTraining)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AtHomeTrainingViewModel::class.java)
        binding.atHomeTrainingViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
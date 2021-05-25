package com.trainingapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.R
import com.example.main.databinding.FragmentAtHomeTrainingBinding
import com.trainingapp.RunApplication
import com.trainingapp.db.Training
import com.trainingapp.viewmodels.*
import java.util.*

class AtHomeTrainingFragment  : Fragment() {
    private lateinit var binding: FragmentAtHomeTrainingBinding
    private lateinit var viewModel : AtHomeTrainingViewModel
    private lateinit var viewModelFactory: AtHomeTrainingViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_at_home_training,container,false)
        viewModelFactory = AtHomeTrainingViewModelFactory((activity?.application as RunApplication).repositoryTraining)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AtHomeTrainingViewModel::class.java)
        binding.atHomeTrainingViewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.insert(Training(Calendar.getInstance().timeInMillis, "TEST", 180000L, 180))

        return binding.root
    }

}
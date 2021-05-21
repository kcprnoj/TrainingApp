package com.trainingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentTrainingBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trainingapp.viewmodels.TrainingViewModel

class TrainingFragment : Fragment() {
    private lateinit var binding: FragmentTrainingBinding
    private lateinit var viewModel : TrainingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_training,container,false)
        viewModel = ViewModelProvider(this).get(TrainingViewModel::class.java)

        binding.startRunningTrainingButton.setOnClickListener {
            findNavController().navigate(TrainingFragmentDirections.actionTrainingFragmentToRunningFragment())
        }
        return binding.root
    }
}
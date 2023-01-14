package com.trainingapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {
    private lateinit var binding: FragmentTrainingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View{

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_training,container,false)

        binding.startRunningTrainingButton.setOnClickListener {
            findNavController().navigate(TrainingFragmentDirections.actionTrainingFragmentToRunningFragment())
        }

        binding.startAthomeTrainingButton.setOnClickListener {
            findNavController().navigate(TrainingFragmentDirections.actionTrainingFragmentToAtHomeTrainingFragment())
        }

        return binding.root
    }


}
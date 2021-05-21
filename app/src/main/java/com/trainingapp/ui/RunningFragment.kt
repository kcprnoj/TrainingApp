package com.trainingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.main.R
import com.example.main.databinding.FragmentRunningBinding
import com.trainingapp.viewmodels.RunningViewModel

class RunningFragment : Fragment() {
    private lateinit var binding: FragmentRunningBinding
    private lateinit var viewModel : RunningViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_running,container,false)
        viewModel = ViewModelProvider(this).get(RunningViewModel::class.java)
        binding.runningViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}
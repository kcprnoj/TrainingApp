package com.trainingapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.main.R
import com.example.main.databinding.FragmentHistoryBinding
import com.trainingapp.RunApplication
import com.trainingapp.model.repository.TrainingRepository
import com.trainingapp.model.webservice.TrainingService
import com.trainingapp.model.webservice.UserService
import com.trainingapp.view.MainActivity
import com.trainingapp.viewmodels.HistoryViewModel
import com.trainingapp.viewmodels.HistoryViewModelFactory
import com.trainingapp.viewmodels.RunListAdapter

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel : HistoryViewModel
    private lateinit var viewModelFactory: HistoryViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_history,container,false)
        viewModelFactory = HistoryViewModelFactory(TrainingRepository(TrainingService()),
            (requireActivity().application as RunApplication).perfRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)


        val recyclerView = binding.recyclerview
        val adapter = RunListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.refreshAllTrainings()

        viewModel.trainings.observe(viewLifecycleOwner) {
            it.let { adapter.submitList(it) }
        }

        return binding.root
    }
}
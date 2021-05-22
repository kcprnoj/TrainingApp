package com.trainingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.main.R
import com.example.main.databinding.FragmentHistoryBinding
import com.trainingapp.RunApplication
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
        viewModelFactory = HistoryViewModelFactory((activity?.application as RunApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)
        binding.historyViewModel = viewModel
        binding.lifecycleOwner = this

        val recyclerView = binding.recyclerview
        val adapter = RunListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.allRunsByDate.observe(viewLifecycleOwner, { runs ->
            runs.let { adapter.submitList(it) }
        })


        return binding.root
    }
}
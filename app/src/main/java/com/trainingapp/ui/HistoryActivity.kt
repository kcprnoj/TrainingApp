package com.trainingapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.main.R
import com.trainingapp.RunApplication
import com.trainingapp.viewmodels.RunListAdapter
import com.trainingapp.viewmodels.RunViewModel
import com.trainingapp.viewmodels.RunViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private val runViewModel: RunViewModel by viewModels {
        RunViewModelFactory((application as RunApplication).repository)
    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RunListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        runViewModel.allRunsByDate.observe(owner = this) { runs ->
            runs.let { adapter.submitList(it) }
        }
        
    }*/
}
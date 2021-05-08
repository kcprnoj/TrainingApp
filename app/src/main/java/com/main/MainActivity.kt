package com.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.main.R
import com.main.database.RunDAO

class MainActivity : AppCompatActivity() {

    lateinit var runDAO: RunDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
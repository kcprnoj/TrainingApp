package com.trainingapp.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.main.R
import com.example.main.databinding.ActivityMainBinding
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.lang.Exception



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration : AppBarConfiguration
    lateinit var stompClient: StompClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()
        connectToServer()
    }

    private fun setupNavigation(){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val drawerLayout = binding.drawerLayout
        val bottomNavView = binding.bottomNavigation
        bottomNavView.visibility = View.GONE
        val topNavigation = binding.topNavigation
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.trainingFragment, R.id.historyFragment, R.id.statisticsFragment -> {
                    bottomNavView.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
                else -> {
                    bottomNavView.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
        NavigationUI.setupWithNavController(bottomNavView, navController)
        NavigationUI.setupWithNavController(topNavigation, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    @SuppressLint("CheckResult")
    private fun connectToServer(): Boolean {
        try{
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/chat")
            stompClient.connect()

        } catch (e: Exception) {
            Log.e("Connection", e.stackTrace.toString())
            return false
        }
        Log.i("Connection", "Connected")
        return true
    }

    override fun onDestroy() {
        if (stompClient.isConnected) {
            stompClient.disconnect()
        }
        super.onDestroy()
    }

}